package com.file.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.file.abstracts.Operations;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;

public class FileOperations extends Operations {

	public FileOperations(UserInfo userInfo) {
		super(userInfo);
	}

	@Override
	protected void initializeUploadProcess(String fromPath, String toPath) {

	}

	@Override
	protected String uploadProcess(String fromPath, String toPath)
			throws IOException {
		File fromFile = FileUtils.getFile(fromPath);
		File toFile = null;
		if(fromFile.isDirectory()) {
			toFile = FileUtils.getFile(toPath+File.separator+fromFile.getName());
		} else {
		    toFile = FileUtils.getFile(toPath);
		}
		if (fromFile != null && fromFile.isDirectory()) {
			if(!toFile.exists()) {
				toFile.mkdirs();
			}
			FileUtils.copyDirectory(fromFile, toFile, false);
		} else if (fromFile != null && fromFile.isFile()) {
			if(!toFile.getParentFile().exists()) {
				toFile.getParentFile().mkdirs();
			}
			FileUtils.copyFileToDirectory(fromFile, toFile, false);
		}
		System.out.println("File uploaded to local at :"+toFile.getAbsolutePath());
		return toFile.getAbsolutePath();
	}

	@Override
	protected boolean uploadDropboxProcess(String fromPath) throws DbxException, IOException {
		if (userInfo.isDropboxSupported()) {
			File file = new File(fromPath);
			if (file.isDirectory()) {
				if (file.listFiles().length > 0) {
					File[] files = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						if(files[i].getAbsoluteFile().isFile()) {
							uploadingFiles(userInfo.getClient(), files[i].getAbsoluteFile());
						} else {
							uploadDropboxProcess(files[i].getAbsolutePath());
						}
					}
				}
			} else if(file.isFile()) {
				uploadingFiles(userInfo.getClient(), file);
			}
 		}

		return false;
	}

	private void uploadingFiles(DbxClient client, File inputFile) throws DbxException, IOException {
		FileInputStream inputStream=null;
		try {
			String uploadPath = convertToDropboxPath(inputFile.getAbsolutePath());
			if(uploadPath!=null) {
				System.out.println("Uploading to dropbox : "+uploadPath);
				inputStream = new FileInputStream(inputFile);
				
				DbxEntry.File uploadedFile = client.uploadFile(uploadPath, DbxWriteMode.add(),
						inputFile.length(), inputStream);
				System.out.println("Uploaded: " + uploadedFile.toString());
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	@Override
	protected String downloadProcess(String fromPath, String toPath)
			throws Exception {
		if(FileUtils.getFile(fromPath).exists()) {
			return uploadProcess(fromPath, toPath);
		} else {
			if(FileUtils.getFile(toPath).isDirectory()) {
				if(!toPath.endsWith("/")){
					toPath+=File.separator;
				}
				toPath = toPath+fromPath.substring(fromPath.lastIndexOf("/")+1, fromPath.length());
			}
			File f = new File(toPath);
			File parent = f.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}	
			if(f.exists()) {
				if(compareMetadata(f, fromPath)) {
					return toPath;
				}
				FileUtils.forceDelete(f);
			}
			
			f.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(f);
			try {
				DbxClient client = userInfo.getClient();
				DbxEntry.File downloadedFile = client.getFile(fromPath, null,
						outputStream);
				System.out.println("Metadata: " + downloadedFile.toString());
				return toPath;
			} finally {
				outputStream.close();
			}
		}
	}

	@Override
	protected String zipProcess(String fromPath, String toPath) throws IOException, ZipException {
		File fromFile = FileUtils.getFile(fromPath);
		File toFile = FileUtils.getFile(toPath);
		if(!toFile.exists()) {
			toFile.mkdirs();
		}
		File f = new File(toFile.getAbsolutePath(), fromFile.getName()+".zip");

		ZipParameters params = new ZipParameters();
		params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		
		ZipFile zipper = new ZipFile(f.getAbsolutePath());
		if(fromFile.isDirectory()) { 
			zipper.addFolder(fromFile, params);
		} else {
			zipper.addFile(fromFile, params);
		}
		
		return f.getAbsolutePath();
	}

	@Override
	protected void syncLocalToDropbox(String localRootPath) throws Exception {
		if(userInfo.isDropboxSupported()) { 
			File file = new File(localRootPath);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if(files[i].getAbsoluteFile().isFile()) {
						File localFile = files[i].getAbsoluteFile();
						String dropboxPath = convertToDropboxPath(localFile.getAbsolutePath());
						if(!compareMetadata(localFile, dropboxPath)) {
							System.out.println("Upload dropbox :"+localFile.getAbsolutePath());
							uploadingFiles(userInfo.getClient(), localFile);
						}
					} else {
						syncLocalToDropbox(files[i].getAbsolutePath());
					}
				}
			} 
		}
	}

	@Override
	protected void syncDropboxToLocal(String dropboxRootPath) throws Exception {
		System.out.println("syncDropboxToLocal " + dropboxRootPath);
		if (userInfo.isDropboxSupported()) {
			DbxEntry.WithChildren listing = userInfo.getClient()
					.getMetadataWithChildren(dropboxRootPath);
			if (listing != null) {
				for (DbxEntry child : listing.children) {
					if (child.isFolder()) {
						syncDropboxToLocal(child.path);
					} else {
						String localPath = convertToLocalPath(child.path);
						if (!FileUtils.getFile(localPath).exists()) {
							OperationUtil.getFileOperations().download(
									child.path, localPath);
						}
					}
				}
			}
		}
	}


}
