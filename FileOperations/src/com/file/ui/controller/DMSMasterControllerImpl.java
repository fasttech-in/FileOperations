package com.file.ui.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;

import com.file.action.DatabaseDAOAction;
import com.file.constant.FileConstants;
import com.file.email.EmailVO;
import com.file.email.SendMail;
import com.file.pojo.FolderDetailVO;
import com.file.ui.EmailInputDialog;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
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
				CommanUtil.loadLocalTreeData();
				CommanUtil.loadCloudTreeData();
				
				ObservableList<FolderDetailVO> data =
			            CommanUtil.getRecentTableContentList();
				
				data.add(new FolderDetailVO(FileUtils.getFile(destination),FileConstants.FileOperationAction.UPLOAD_ACTION));
				DatabaseDAOAction.updateRecent(data);
			} else {
				PopupNotification.showError("Upload-Error", "Upload failed, please contact administrator.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			PopupNotification.showError("Upload-Error", "Upload failed, please contact administrator.");
		} 
	}
	
	public void downloadButtonActionPerformed(String source, String destination) {
		try {
			String downPath = OperationUtil.getFileOperations().download(source, destination);
			File f = FileUtils.getFile(downPath);
			if(f!=null && f.exists()) {
				PopupNotification.showSuccess("Download-Success", "Files downloaded successfully.");
				
				ObservableList<FolderDetailVO> data =
			            CommanUtil.getRecentTableContentList();
				
				data.add(new FolderDetailVO(f,FileConstants.FileOperationAction.DOWNLOAD_ACTION));
				DatabaseDAOAction.updateRecent(data);
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
				
				ObservableList<FolderDetailVO> data =
			            CommanUtil.getRecentTableContentList();
				
				data.add(new FolderDetailVO(f,FileConstants.FileOperationAction.ZIP_ACTION));
				DatabaseDAOAction.updateRecent(data);
			} else {
				PopupNotification.showError("Zip-Error", "Zip failed, please contact administrator.");
			}
		} catch (Exception e) {
			PopupNotification.showError("Zip-Error", "Zip failed, please contact administrator.");
		} 
	}
	
	public File showDirectoryChooser(TextField textFld) {
		DirectoryChooser dirChooser = getDirectoryChooser();
        File file = dirChooser.showDialog(null);
        if(file!=null) {
        	if(textFld!=null)
        	textFld.setText(file.getAbsolutePath());
        	dirChooser.setInitialDirectory(file);
        }
        return file;
	}

	protected DirectoryChooser getDirectoryChooser() {
		if (dirChooser == null && OperationUtil.getFileOperations()!=null) {
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
		if (fileChooser == null && OperationUtil.getFileOperations()!=null) {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Choose File");
			fileChooser.setInitialDirectory(new File(
					OperationUtil.getFileOperations().getUserRootDirectory()));
		}
		return fileChooser;
	}

	public void multiZipBtnActionPerformed(List<String> observableList) {
		if (observableList != null && observableList.size() > 0) {
			try {
				File toFile = showDirectoryChooser(null);
				if (toFile != null) {
					String toPath = toFile.getAbsolutePath() + File.separator+ "download";
					for (String fromPath : observableList) {
						OperationUtil.getFileOperations().download(fromPath,toPath);
					}
					String downPath = OperationUtil.getFileOperations().zip(toPath, toFile.getAbsolutePath());
					FileUtils.forceDelete(new File(toPath));
					File f = FileUtils.getFile(downPath);
					if(f!=null && f.exists()) {
						PopupNotification.showSuccess("Zip-Success", "Zipped successfully.");
						
						ObservableList<FolderDetailVO> data =
					            CommanUtil.getRecentTableContentList();
						
						data.add(new FolderDetailVO(f,FileConstants.FileOperationAction.ZIP_ACTION));
						DatabaseDAOAction.updateRecent(data);
					} else {
						PopupNotification.showError("Zip-Error", "Zip failed, please contact administrator.");
					}
				}
			} catch (Exception e) {
				PopupNotification.showError("Zip-Error","Zip failed, please contact administrator.");
			}
		}
	}
	
	public void multiDownloadBtnActionPerformed(List<String> observableList) {
		if (observableList != null && observableList.size() > 0) {
			try {
				File toFile = showDirectoryChooser(null);
				if (toFile != null) {
					String toPath = toFile.getAbsolutePath() + File.separator+ "download";
					for (String fromPath : observableList) {
						OperationUtil.getFileOperations().download(fromPath,toPath);
					}
					PopupNotification.showSuccess("Download-Success", "Files downloaded successfully.");
					
					ObservableList<FolderDetailVO> data =
				            CommanUtil.getRecentTableContentList();
					
					data.add(new FolderDetailVO(FileUtils.getFile(toPath),FileConstants.FileOperationAction.DOWNLOAD_ACTION));
					DatabaseDAOAction.updateRecent(data);
				}
			} catch (Exception e) {
				PopupNotification.showError("Download-Error", "Download failed, please contact administrator.");
			}
		}
	}

	public void emailBtnActionPerformed(List<String> items) {
		try {
			if (items.size() > 0) {
				EmailInputDialog email = new EmailInputDialog();
				String toString = email.getToString();
				String messageString = email.getMessageString();
				String subjectString = email.getSubjectString();

				String to[] = toString.split(";");
				if (to != null && to.length > 0) {
					EmailVO vo = new EmailVO(Arrays.asList(to), items,
							messageString, subjectString);

					String result = vo.sendEmail(vo);
					if(SendMail.SUCCESS_MESSAGE.equals(result)) {
						PopupNotification.showSuccess("Email-Success", "Mail Sent");
						ObservableList<FolderDetailVO> data =
					            CommanUtil.getRecentTableContentList();
						items.forEach((path)->{
							data.add(new FolderDetailVO(FileUtils.getFile(path),
									FileConstants.FileOperationAction.EMAIL_ACTION));
						});
						
						DatabaseDAOAction.updateRecent(data);
					} else {
						PopupNotification.showError("Email-Error", result);
					}
				} else {
					PopupNotification.showError("Email-Error", "Please add recipients.");
				}
			} else {
				PopupNotification.showError("Email-Error", "please add attachments.");
			}
		} catch (Exception e) {
			PopupNotification.showError("Email-Error", "please contact administrator.");
		}
	}
}