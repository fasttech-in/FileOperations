package com.file.util;

import java.io.File;

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
	private static String p = "/com/file/ui/images/";
	
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
	
}
