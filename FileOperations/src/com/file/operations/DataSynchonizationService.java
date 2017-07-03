package com.file.operations;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;

import com.file.constant.FileConstants;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;
import com.user.info.UserVO;

public class DataSynchonizationService {
	private FileOperations ops;
	private boolean isViaDMS = true;
	
	public DataSynchonizationService(String clientDetailsFilePath) {
		isViaDMS = false;
		init(clientDetailsFilePath);
	}
	
	public DataSynchonizationService(FileOperations ops) {
		isViaDMS = true;
		this.ops = ops;
		if(OperationUtil.getFileOperations()==null) {
			OperationUtil.setFileOperations(ops);
		}
	}

	private void init(String clientDetailsFilePath) {
		if(clientDetailsFilePath!=null && clientDetailsFilePath.endsWith(FileConstants.ClientSettingsFile.EXTENSION)) {
			File clientFile = FileUtils.getFile(clientDetailsFilePath);
			if(clientFile.exists()) {
				UserVO user = CommanUtil.unmarshall(clientFile.getName());
				CommanUtil.setUserSettingsVO(user);
				UserInfo userInfo = new UserInfo(user);
				ops = new FileOperations(userInfo);
				OperationUtil.setFileOperations(ops);
			}
		}
	}
	
	public void start() throws Exception{
		
		TimerTask task = new TimerTask() {
			long fileCount = 0;
			@Override
			public void run() {
				System.out.println("running TimerTask...");
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call()  {
						System.out.println("running FXTask ...");
						if(ops!=null && ops.getUserInfo().isDropboxSupported() && 
								ops.getUserInfo().getUserVO().isServerAutoSync()) {
							try {
								long currentFileCnt = ops.getFileCount(ops.getUserInfo().getUserRootDirectory());
								if(fileCount != currentFileCnt) {
									fileCount = currentFileCnt;
									ops.syncFiles(ops.getUserInfo().getUserRootDirectory());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if(!isViaDMS) {
								PopupNotification.showError("Sync - Error", "Unable to start auto sync.");
							}
						}
						return null;
					}
				};
				new Thread(task).start();
			}
		};
		Timer timer = new Timer();
		//every 60 seconds, with a 10 second delay for the first time of execution
    	timer.schedule(task, 10000,60000);
	}
	
	

}
