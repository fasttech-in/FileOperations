package com.file.operations;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.file.abstracts.Preview;
import com.file.operations.pdf.PDFPageViewer;
import com.file.operations.pdf.PageAsImageExtraction;

public class PdfPreviewOperations extends Preview {
	
	public PdfPreviewOperations(Tab previewTab) {
		super(previewTab);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Parent loadContents(String path) {
		path = getFilePath(path);
		System.out.println("Loading pdf :"+path);
		
		TabPane tabpane = new TabPane();
		Tab pageAsImgTab = getPagesAsImagesTab(tabpane, path);
		Tab singlePageTab = getSinglePageTab(tabpane, path);
		
		return tabpane;
	}

	private Tab getPagesAsImagesTab(TabPane tabpane, String path) {
		Tab pageAsImgTab = new Tab("Thumbnails");
		pageAsImgTab.setClosable(false);
		
		PageAsImageExtraction pageImageExtraction = new PageAsImageExtraction();
        pageImageExtraction.pageImageExtraction(pageAsImgTab, path, 20);
		
		tabpane.getTabs().add(pageAsImgTab);
		return pageAsImgTab;
	}

	private Tab getSinglePageTab(TabPane tabpane, String path) {
		Tab singlePageTab = new Tab("Pages");
		singlePageTab.setClosable(false);
		
		PDFPageViewer pageViewer = new PDFPageViewer();
		pageViewer.loadViewer(singlePageTab, path, 2);
		
		tabpane.getTabs().add(singlePageTab);
		return singlePageTab;
	}
	
	protected String getFilePath(String path) {
		if(!isFilePresent(path)) {
			path = downloadCloudFile(path);
		}
		return path;
	}
}
