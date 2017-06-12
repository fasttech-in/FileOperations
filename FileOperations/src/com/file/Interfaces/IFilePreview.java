package com.file.Interfaces;

import java.io.File;



public interface IFilePreview {

	public void loadPreview(String path);
	public boolean isFilePresent(String path);
	public String downloadCloudFile(String path);
	public void setPreviewFile(File file);
	public File getPreviewFile();
}