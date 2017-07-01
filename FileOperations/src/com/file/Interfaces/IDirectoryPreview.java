package com.file.Interfaces;

import java.io.File;

import com.file.pojo.FolderDetailVO;

import javafx.collections.ObservableList;



public interface IDirectoryPreview {

	public ObservableList<FolderDetailVO> loadPreview();
	public boolean isFilePresent(String path);
	public ObservableList<?> downloadCloudFile(String path);
	public void setPreviewFile(File file);
	public File getPreviewFile();
}