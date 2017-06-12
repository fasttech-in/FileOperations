package com.file.abstracts;

import java.io.File;

import javafx.scene.Parent;
import javafx.scene.control.Tab;

import org.apache.commons.io.FileUtils;

import com.file.Interfaces.IFilePreview;
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
		Parent content = loadContents(path);
		previewTab.setContent(content);
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
