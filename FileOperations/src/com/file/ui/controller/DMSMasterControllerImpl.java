package com.file.ui.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import com.file.ui.PopupNotification;
import com.file.util.OperationUtil;


public class DMSMasterControllerImpl {
	private DirectoryChooser dirChooser;
	private FileChooser fileChooser;

	public List<String> search(String text, boolean isLocalSelected) {
		return null;
	}
	
	public void uploadButtonActionPerformed(String source, String destination) {
		try {
			if(OperationUtil.getFileOperations().upload(source, destination)) {
				PopupNotification.showSuccess("Upload-Success", "Files uploaded successfully.");
			} else {
				PopupNotification.showError("Upload-Error", "Upload failed, please contact administrator.");
			}
		} catch (Exception e) {
			PopupNotification.showError("Upload-Error", "Upload failed, please contact administrator.");
		} 
	}
	
	public void downloadButtonActionPerformed(String source, String destination) {
		try {
			String downPath = OperationUtil.getFileOperations().download(source, destination);
			File f = FileUtils.getFile(downPath);
			if(f!=null && f.exists()) {
				PopupNotification.showSuccess("Download-Success", "Files downloaded successfully.");
			} else {
				PopupNotification.showError("Download-Error", "Download failed, please contact administrator.");
			}
		} catch (Exception e) {
			PopupNotification.showError("Download-Error", "Download failed, please contact administrator.");
		} 
	}
	
	public void downloadZipButtonActionPerformed(String source, String destination) {
		try {
			String downPath = OperationUtil.getFileOperations().zip(source, destination);
			File f = FileUtils.getFile(downPath);
			if(f!=null && f.exists()) {
				PopupNotification.showSuccess("Zip-Success", "Zipped successfully.");
			} else {
				PopupNotification.showError("Zip-Error", "Zip failed, please contact administrator.");
			}
		} catch (Exception e) {
			PopupNotification.showError("Zip-Error", "Zip failed, please contact administrator.");
		} 
	}
	
	public void showDirectoryChooser(TextField textFld) {
		DirectoryChooser dirChooser = getDirectoryChooser();
        File file = dirChooser.showDialog(null);
        if(file!=null) {
        	textFld.setText(file.getAbsolutePath());
        	dirChooser.setInitialDirectory(file);
        }
	}

	protected DirectoryChooser getDirectoryChooser() {
		if (dirChooser == null) {
			dirChooser = new DirectoryChooser ();
			dirChooser.setTitle("Choose location");
			dirChooser.setInitialDirectory(new File(
					OperationUtil.getFileOperations().getUserRootDirectory()));
		}
		return dirChooser;
	}
	
	public File showFileChooser() {
		FileChooser dirChooser = getFileChooser();
        File file = dirChooser.showOpenDialog(null);
        if(file!=null) {
        	dirChooser.setInitialDirectory(file.getParentFile());
        }
        return file;
	}

	protected FileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Choose File");
			fileChooser.setInitialDirectory(new File(
					OperationUtil.getFileOperations().getUserRootDirectory()));
		}
		return fileChooser;
	}
}