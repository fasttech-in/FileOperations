package com.file.operations;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.file.constant.FileConstants;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;
import com.user.info.UserVO;

public class DataSynchonizationService {
	static Logger log = Logger.getLogger(DataSynchonizationService.class.getName());
	private FileOperations ops;
	private boolean isViaDMS = true;
	
	public DataSynchonizationService(String clientDetailsFilePath) {
		CommanUtil.getTempDirectory(); // just to initialize util class
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
				log.info("Loading auto sync client file: "+clientFile.getAbsolutePath());
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
				log.info("Running auto sync TimerTask ");
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call()  {
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
							log.info("Unable to start. check client settings file and auto sync flags.");
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
