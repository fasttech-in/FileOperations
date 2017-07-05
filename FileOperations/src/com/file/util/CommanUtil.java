package com.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.file.action.FileTreeViewAction;
import com.file.constant.FileConstants;
import com.file.pojo.FolderDetailVO;
import com.user.info.UserVO;

public class CommanUtil {
	static Logger log = Logger.getLogger(CommanUtil.class.getName());

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
	public static Image synchronizeNodeImg = null;
	public static Image aboutNodeImg = null;
	
	private static UserVO userVO;
	private static Properties funcationalityPropertiesFile;
	private static String p = "/com/file/ui/images/";
	private static FileTreeViewAction cloudTreeAction;
	private static FileTreeViewAction localTreeAction;
	private static ObservableList<FolderDetailVO> pendingTableContentList;
	private static ObservableList<FolderDetailVO> recentTableContentList;

	private static FXMLLoader loader;
	
	public CommanUtil() {
		
	}

	static {
		userRootDirectory = System.getProperty("user.dir");
		new File(userRootDirectory+File.separator+"Temp").mkdir();
		new File(userRootDirectory+File.separator+"ProductFiles").mkdir();
		PropertyConfigurator.configure(getPropertiesFilePath());
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
		 synchronizeNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"synchronize24.png"));
		 aboutNodeImg = new Image(CommanUtil.class.getResourceAsStream(p+"about.png"));
	}

	public static String getUserRootDirectory() {
		return userRootDirectory;
	}
	
	public static String getPropertyValue(String key) {
		if(funcationalityPropertiesFile == null) {
			funcationalityPropertiesFile= new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(getPropertiesFilePath());
				funcationalityPropertiesFile.load(input);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (input != null) {
					try {
						input.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return funcationalityPropertiesFile.getProperty(key);
	}

	public static String getTempDirectory() {
		return userRootDirectory+File.separator+"Temp";
	}
	
	public static String getProductFileDirectory() {
		return userRootDirectory+File.separator+"ProductFiles";
	}
	
	public static String getPropertiesFilePath() {
		return userRootDirectory+File.separator+"PropertyFiles"+File.separator+"Functionality.properties";
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

	public static ObservableList<FolderDetailVO> getPendingTableContentList() {
		return pendingTableContentList;
	}

	public static void setPendingTableContentList(
			ObservableList<FolderDetailVO> pendingTableContentList) {
		CommanUtil.pendingTableContentList = pendingTableContentList;
	}

	public static ObservableList<FolderDetailVO> getRecentTableContentList() {
		return recentTableContentList;
	}

	public static void setRecentTableContentList(
			ObservableList<FolderDetailVO> recentTableContentList) {
		CommanUtil.recentTableContentList = recentTableContentList;
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
	
	public static void marshal(UserVO vo) {
		try {

			File file = new File(getProductFileDirectory() + File.separator
					+ vo.getClientName()+FileConstants.ClientSettingsFile.EXTENSION);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(UserVO.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(vo, file);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UserVO unmarshall(String fileName) {
		try {

			File file = new File(getProductFileDirectory() + File.separator
					+ fileName);
			if(file.exists()) {
				JAXBContext jaxbContext = JAXBContext.newInstance(UserVO.class);
	
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				
				UserVO vo =  (UserVO) jaxbUnmarshaller.unmarshal(file);
				if (vo != null) {
					byte[] decodedBytes = Base64.getDecoder().decode(
							vo.getPassword());
					String decodedString = new String(decodedBytes);
					vo.setPassword(decodedString);
					decodedBytes = Base64.getDecoder().decode(
							vo.getUserAccessKey());
					decodedString = new String(decodedBytes);
					vo.setUserAccessKey(decodedString);
				}
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setUserSettingsVO(UserVO vo) {
		userVO = vo;
	}
	
	public static UserVO getUserSettingsVO() {
		return userVO;
	}

	public static List<UserVO> loadAvailableUsers() {
		List<UserVO> volist = new ArrayList<UserVO>();
		File productDir = FileUtils.getFile(CommanUtil
				.getProductFileDirectory());
		for (File f : productDir.listFiles()) {
			if (f.getName().endsWith(FileConstants.ClientSettingsFile.EXTENSION)) {
				UserVO vo = unmarshall(f.getName());
				if (vo != null) {
					volist.add(vo);
				}
			}
		}
		return volist;
	}
}

