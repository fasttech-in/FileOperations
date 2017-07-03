package com.file.abstracts;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.file.Interfaces.IFileOperations;
import com.file.action.DatabaseDAOAction;
import com.file.constant.FileConstants;
import com.file.pojo.FolderDetailVO;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;

public abstract class Operations implements IFileOperations {

	protected UserInfo userInfo;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public Operations(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	@Override
	public boolean upload(String fromPath, String toPath) throws IOException, DbxException {
		initializeUploadProcess(fromPath, toPath);
		String uploadPath = uploadProcess(fromPath, toPath);
		if (uploadPath!=null && userInfo.getUserVO().isServerAutoSync()) {
			uploadDropboxProcess(uploadPath);
		} else {
			ObservableList<FolderDetailVO> data =
		            CommanUtil.getPendingTableContentList();
			
			data.add(new FolderDetailVO(FileUtils.getFile(uploadPath),FileConstants.FileOperationAction.CLOUD_ACTION));
			DatabaseDAOAction.updatePending(data);
		}
		return uploadPath!=null;
	}

	@Override
	public String download(String fromPath, String toPath) throws Exception {

		return downloadProcess(fromPath, toPath);
	}

	@Override
	public List<String> search(String searchStr, String searchFolder) {
		List<String> matchPaths = new ArrayList<String>();
		File folder = new File(searchFolder);
		matchPaths = search(matchPaths, searchStr, folder);
		
		return matchPaths;
	}

	private List<String> search(List<String> matchPaths, String searchStr,
			File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				search(matchPaths, searchStr, files[i].getAbsoluteFile());
			}
		} else {
			if(file.getName().contains(searchStr)){
				matchPaths.add(file.getAbsolutePath());
			}
		}
		
		return matchPaths;
	}

	@Override
	public String zip(String fromPath, String toPath) throws IOException, ZipException {
		
		return zipProcess(fromPath, toPath);
	}

	@Override
	public boolean openFile(String filePath) throws Exception, InterruptedException {
		File file = FileUtils.getFile(filePath);
		if(!file.exists() && isCloudPath(filePath)) {
			file = FileUtils.getFile(download(filePath, CommanUtil.getTempDirectory()));
		}
		if (file.exists()) {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			} else {
				Process p = Runtime.getRuntime().exec(
						"rundll32 url.dll, FileProtocolHandler " + filePath);
				p.waitFor();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteFile(String filePath) {
		File file = FileUtils.getFile(filePath);
		if(file.exists()) {
			return FileUtils.deleteQuietly(file);
		} else if(isCloudPath(filePath)) {
			if(userInfo.isDropboxSupported()) {
				try {
					userInfo.getClient().delete(filePath);
					return true;
				} catch (DbxException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
	
	public String getUserRootDirectory() {
		return userInfo.getUserRootDirectory();
	}
	
	protected String convertToDropboxPath(String localPath) {
		if(localPath.startsWith(userInfo.getUserRootDirectory())) {
			if(localPath.contains("/")){
				localPath = localPath.replaceAll("\\/", "\\\\");
			}
			localPath = localPath.replace(userInfo.getUserRootDirectory(), "").replaceAll("\\\\","/");
			if(!localPath.startsWith("/")){
				localPath ="/"+localPath;
			}
			return userInfo.getUserDropboxRoot() + localPath;
		} 
		return null;
	}
	
	protected String convertToLocalPath(String dropboxPath) {
		if(dropboxPath.startsWith(userInfo.getUserDropboxRoot())) {
			dropboxPath = dropboxPath.replace(userInfo.getUserDropboxRoot(), "").replaceAll("\\\\","/");
			if(dropboxPath.contains("/")){
				dropboxPath = dropboxPath.replaceAll("\\/", "\\\\");
			}
			return userInfo.getUserRootDirectory() + dropboxPath;
		} 
		return null;
	}

	public String getLastModifiedDate(File file) {
		return sdf.format(file.lastModified());
	}

	public String getSize(File file,String inFormat, String uptoDecimal) {
		double bytes = file.length();
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);
		if(null == inFormat) {
		if(kilobytes < 1000) {
			return String.format("%."+uptoDecimal+"f", kilobytes) +" KB";
		} else if(megabytes>1 && megabytes< 100) {
			return String.format("%."+uptoDecimal+"f", megabytes) +" MB";
		} else {
			return String.format("%."+uptoDecimal+"f", gigabytes) +" GB";
		}
		} else {
			if(inFormat.equals("B")) {
				return String.valueOf(bytes);
			} else if(inFormat.equals("KB")) {
				return String.valueOf(kilobytes);
			} else if(inFormat.equals("GB")) {
				return String.valueOf(gigabytes);
			} else {
				return null;
			}
		}
	}
	
	public long getFileCount(String path) throws IOException {
		return Files.walk(Paths.get(path)).parallel().filter(p -> !p.toFile().isDirectory()).count();
	}
	
	protected boolean compareMetadata(File localFile, String dropboxPath) throws Exception{
		String bytes = OperationUtil.getFileOperations().getSize(localFile,"B","0");
		DbxClient client = userInfo.getClient();
		DbxEntry entry = client.getMetadata(dropboxPath,true);
		if(entry == null) {
			return false;
		}
		if(entry.isFolder()) {
			return true;
		}
		DbxEntry.File metadata = (DbxEntry.File)entry;
		return bytes.equals(String.valueOf(Double.valueOf(String.valueOf(metadata.numBytes))));
	}
	
	public boolean isCloudPath(String path) {
		return path.startsWith("/");
	}
	
	public boolean syncFiles(String loadPath) throws Exception {
		final Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				updateMessage("");
				if(isCloudPath(loadPath)) {
					syncDropboxToLocal(loadPath);
				} else {
					syncLocalToDropbox(loadPath);
				}
				System.out.println("Sync done. returning true");
				return true;
			}
		};

		task.stateProperty().addListener(new ChangeListener<Worker.State>() {

	        @Override
	        public void changed(ObservableValue<? extends State> observable,
	                State oldValue, Worker.State newState) {
	            if(newState==Worker.State.SUCCEEDED) {
	    			PopupNotification.showSuccess("Auto Sync - Success", "Sync completed successfully.");
	            }
	            else if(newState==Worker.State.RUNNING) {
	    			PopupNotification.showInfo("Auto Sync - Running", "Please wait, might take upto a minute.");
	            }
	            else if(newState==Worker.State.FAILED) {
	    			PopupNotification.showError("Auto Sync - Failed", "Check internet connection & settings.");
	            }
	        }
	    });
		
		new Thread(task).start();
		return true;
	}
	
	abstract protected void initializeUploadProcess(String fromPath, String toPath);

	abstract protected String uploadProcess(String fromPath, String toPath) throws IOException;

	abstract protected boolean uploadDropboxProcess(String fromPath) throws DbxException, IOException;
	
	abstract protected String downloadProcess(String fromPath, String toPath) throws Exception;
	
	abstract protected String zipProcess(String fromPath, String toPath) throws IOException, ZipException;
	
	abstract protected void syncLocalToDropbox(String localRootPath) throws Exception;
	
	abstract protected void syncDropboxToLocal(String dropboxRootPath) throws Exception;
	
}
