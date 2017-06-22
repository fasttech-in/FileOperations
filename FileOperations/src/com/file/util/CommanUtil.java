package com.file.util;

import java.io.File;

import com.file.action.FileTreeViewAction;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class CommanUtil {

	private static String userRootDirectory;
	public static Image folderNodeImg = null;
	public static Image fileNodeImg = null;
	public static Image jpgNodeImg = null;
	public static Image pngNodeImg = null;
	public static Image pdfNodeImg = null;
	public static Image docNodeImg = null;
	public static Image xlsNodeImg = null;
	public static Image zipNodeImg = null;
	public static Image rarNodeImg = null;
	public static Image txtNodeImg = null;
	public static Image csvNodeImg = null;
	public static Image gifNodeImg = null;
	public static Image loginNodeImg = null;
	public static Image emailNodeImg = null;
	public static Image deleteNodeImg = null;
	public static Image downloadNodeImg = null;
	public static Image refreshNodeImg = null;
	public static Image searchNodeImg = null;
	public static Image uploadNodeImg = null;
	public static Image onefingerNodeImg = null;
	public static Image addfolderNodeImg = null;
	public static Image cutNodeImg = null;
	public static Image copyNodeImg = null;
	public static Image pasteNodeImg = null;
	public static Image folderOpenNodeImg = null;
	
	private static String p = "/com/file/ui/images/";
	private static FileTreeViewAction cloudTreeAction;
	private static FileTreeViewAction localTreeAction;
	private static FXMLLoader loader;
	
	public CommanUtil() {
		
	}

	static {
		userRootDirectory = System.getProperty("user.dir");
		new File(userRootDirectory+File.separator+"Temp").mkdir();
		folderNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"folder-yellow24.png"));
		 fileNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"file24.png"));
		 jpgNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"jpg24.png"));
		 pngNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"png24.png"));
		 pdfNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"pdf24.png"));
		 docNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"doc24.png"));
		 xlsNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"excel24.png"));
		 zipNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"zip24.png"));
		 rarNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"rar24.png"));
		 txtNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"text24.png"));
		 csvNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"excel24.png"));
		 gifNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"gif24.png"));
		 loginNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"login-32.png"));
		 emailNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"email.png"));
		 deleteNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"delete24.png"));
		 downloadNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"download24.png"));
		 refreshNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"refresh24.png"));
		 searchNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"search24.png"));
		 uploadNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"upload24.png"));
		 onefingerNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"onefinger24.png"));
		 addfolderNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"addfolder24.png"));
		 cutNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"cut24.png"));
		 copyNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"copy24.png"));
		 pasteNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"paste24.png"));
		 folderOpenNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"folderOpen24.png"));
		 
	}

	public static String getUserRootDirectory() {
		return userRootDirectory;
	}
	
	public static String getTempDirectory() {
		return userRootDirectory+File.separator+"Temp";
	}
	
	public static Image getNodeImage(String fileName) {
		if(fileName!=null && fileName.contains(".")) {
			String extension = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toLowerCase();
			switch (extension) {
			case "jpg": return jpgNodeImg;
			case "png": return pngNodeImg;
			case "pdf": return pdfNodeImg;
			case "doc": return docNodeImg;
			case "docx": return docNodeImg;
			case "xls": return xlsNodeImg;
			case "zip": return zipNodeImg;
			case "rar": return rarNodeImg;
			case "txt": return txtNodeImg;
			case "csv": return csvNodeImg;
			case "gif": return gifNodeImg;
			}
		}
		return fileNodeImg;
	}

	public static void setCloudTreeAction(FileTreeViewAction cTreeAction) {
		cloudTreeAction = cTreeAction;
	}

	public static void setLocalTreeAction(FileTreeViewAction lTreeAction) {
		localTreeAction = lTreeAction;
	}
	
	public static void loadCloudTreeData() {
		cloudTreeAction.loadServerdata();
	}
	
	public static void loadLocalTreeData() {
		localTreeAction.loadLocalData();
	}

	public static void setFXMLLoader(FXMLLoader _loader) {
		loader = _loader;
	}
	
	public static FXMLLoader getFXMLLoader() {
		return loader;
	}
}

