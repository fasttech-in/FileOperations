package com.file.abstracts;

import java.io.File;





import java.util.Collections;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Tab;






import org.apache.commons.io.FileUtils;






import com.file.Interfaces.IFilePreview;
import com.file.action.DatabaseDAOAction;
import com.file.constant.FileConstants;
import com.file.pojo.FolderDetailVO;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;

public abstract class Preview implements IFilePreview {

	private Tab previewTab;
	protected File previewFile;
	
	public Preview(Tab previewTab) {
		this.previewTab = previewTab;
	}

	@Override
	public void loadPreview(String path) {
		previewTab.setContent(null);
		final Task task = new Task<Parent>() {
			@Override
			protected Parent call() {
				return loadContents(path);
			}
		};
		task.setOnSucceeded(event->{
			previewTab.setContent((Parent)task.getValue());
			ObservableList<FolderDetailVO> data =
		            CommanUtil.getRecentTableContentList();
			
			data.add(new FolderDetailVO(getPreviewFile(),FileConstants.FileOperationAction.VIEW_ACTION));
			DatabaseDAOAction.updateRecent(data);
		});
		new Thread(task).start();
	}

	@Override
	public boolean isFilePresent(String path) {
		return FileUtils.getFile(path).exists();
	}

	@Override
	public String downloadCloudFile(String fromPath) {
		String tempFilePath = CommanUtil.getTempDirectory();
		try {
			return OperationUtil.getFileOperations().download(fromPath, tempFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setPreviewFile(File file) {
		previewFile = file;
	}

	@Override
	public File getPreviewFile() {
		return previewFile;
	}
	
	protected abstract Parent loadContents(String path);
}
