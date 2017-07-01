package com.file.ui;

import java.awt.Toolkit;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.file.action.MobileFileTreeViewAction;
import com.file.operations.FileOperations;
import com.file.ui.controller.DMSMobileController;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;
import com.user.info.UserVO;

public class DMSMobileUI extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException
	{	
		FXMLLoader loader = new FXMLLoader();
		loader.setRoot(new BorderPane());
        loader.setLocation(DMSMasterPage.class.getResource("/com/file/ui/fxml/mastermobilepage.fxml"));
        BorderPane rootLayout = (BorderPane) loader.load();
        double hgt = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double wdt = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Scene scene = new Scene(rootLayout, wdt-20, hgt-70);
        stage.setScene(scene);
        
        stage.show();
        
//      LoginDialog login = new LoginDialog();
//		System.out.println("after login");
//		if(login.isSuccess()) {
        
			UserInfo userInfo = new UserInfo(UserVO.getInstance());
	        FileOperations ops = new FileOperations(userInfo);
	        OperationUtil.setFileOperations(ops);
	        initTree(loader, userInfo);
//		}
	}

	protected void initTree(FXMLLoader loader, UserInfo userInfo) {
		DMSMobileController controller = loader.getController();
		MobileFileTreeViewAction cloudTree = new MobileFileTreeViewAction(userInfo, userInfo.getUserDropboxRoot());
        cloudTree.setFilterField(controller.getSearchTxtFld());
        Parent treeBorderPane = cloudTree.getTreePane();
        controller.getMainPane().setCenter(treeBorderPane);
	}
	
}

	