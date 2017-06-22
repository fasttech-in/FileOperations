package com.file.action;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.control.Tab;

import org.apache.commons.io.FileUtils;

import com.file.Interfaces.IFilePreview;
import com.file.operations.ImagePreviewOperations;
import com.file.operations.PdfPreviewOperations;
import com.file.util.OperationUtil;

public class FilePreviewAction {
	
	private Tab previewTab;
	private Map<String,IFilePreview> processorMap = new ConcurrentHashMap<>();
	public String PDF_PROCESSOR="pdfProcessor";
	public String IMG_PROCESSOR="ImageProcessor";
	
	public FilePreviewAction(Tab previewTab) {
		this.previewTab = previewTab;
	}

	public void preview(String loadPath) {
		String extension = loadPath.substring(loadPath.lastIndexOf(".")+1, loadPath.length()).toLowerCase();
		IFilePreview prev = getPreviewProcessor(extension);
		if(prev != null) {
			File previewFile = FileUtils.getFile(loadPath);
			if(!previewFile.exists() && OperationUtil.getFileOperations().isCloudPath(loadPath)) {
				previewFile = FileUtils.getFile(prev.downloadCloudFile(loadPath));
			}
			prev.setPreviewFile(previewFile);
			prev.loadPreview(loadPath);
		} else {
			previewTab.setContent(null);
		}
	}

	protected IFilePreview getPreviewProcessor(String extension) {
		IFilePreview prev = null;
		if(extension.equals("pdf")){
			if(processorMap.get(PDF_PROCESSOR) == null) {
				prev = new PdfPreviewOperations(previewTab);
				processorMap.put(PDF_PROCESSOR, prev);
			} else {
				prev = processorMap.get(PDF_PROCESSOR);
			}
		} else if(extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png")
				|| extension.equals("gif")) {
			if(processorMap.get(IMG_PROCESSOR) == null ) {
				prev = new ImagePreviewOperations(previewTab);
				processorMap.put(IMG_PROCESSOR, prev);
			} else {
				prev = processorMap.get(IMG_PROCESSOR);
			}
		} 
		return prev;
	}
	
	
}
