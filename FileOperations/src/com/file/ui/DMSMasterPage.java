package com.file.ui;

import java.awt.Toolkit;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.file.action.FilePreviewAction;
import com.file.action.FileTreeViewAction;
import com.file.action.RecentAndPendingAction;
import com.file.constant.FileConstants;
import com.file.operations.FileOperations;
import com.file.pojo.FolderDetailVO;
import com.file.ui.controller.DMSMasterController;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;

public class DMSMasterPage extends Application
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
        loader.setLocation(DMSMasterPage.class.getResource("/com/file/ui/fxml/masterpage.fxml"));
        BorderPane rootLayout = (BorderPane) loader.load();
        double hgt = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double wdt = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Scene scene = new Scene(rootLayout, wdt-20, hgt-70);
        stage.setScene(scene);
        
        UserInfo userInfo = new UserInfo("Testuser", "C:\\TEMP\\test\\dest\\Testuser",null);
        FileOperations ops = new FileOperations(userInfo);
        OperationUtil.setFileOperations(ops);
        initRecentPendingTables(loader);
        FilePreviewAction prev = initFilePreview(loader);
        initLocalTab(loader, userInfo, prev);
        initCloudTab(loader, userInfo, prev);
        
        stage.show();
	}

	private void initRecentPendingTables(FXMLLoader loader) {
		DMSMasterController controller = loader.getController();
		TableColumn<FolderDetailVO, String> pendingTableNameColumn = controller.getPendingTableNameColumn();
		TableColumn<FolderDetailVO, String> pendingTableActionColumn = controller.getPendingTableActionColumn();
		TableColumn<FolderDetailVO, String> pendingTableDateColumn = controller.getPendingTableDateColumn();
		TableColumn<FolderDetailVO, String> pendingTableSizeColumn = controller.getPendingTableSizeColumn();
		
		TableColumn<FolderDetailVO, String> recentTableNameColumn = controller.getRecentTableNameColumn();
		TableColumn<FolderDetailVO, String> recentTableActionColumn = controller.getRecentTableActionColumn();
		TableColumn<FolderDetailVO, String> recentTableDateColumn = controller.getRecentTableDateColumn();
		TableColumn<FolderDetailVO, String> recentTableSizeColumn = controller.getRecentTableSizeColumn();
		
		pendingTableNameColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("name"));
		pendingTableActionColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("action"));
		pendingTableDateColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("lastModified"));
		pendingTableSizeColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("size"));
		recentTableNameColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("name"));
		recentTableActionColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("action"));
		recentTableDateColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("lastModified"));
		recentTableSizeColumn.setCellValueFactory(new PropertyValueFactory<FolderDetailVO, String>("size"));
		
		
		RecentAndPendingAction action = new RecentAndPendingAction(FileConstants.PendingRecentAction.PENDING_ACTION);
		action.loadTableData(loader.getController());
		RecentAndPendingAction action2 = new RecentAndPendingAction(FileConstants.PendingRecentAction.RECENT_ACTION);
		action2.loadTableData(loader.getController());
	}

	private FilePreviewAction initFilePreview(FXMLLoader loader) {
		DMSMasterController controller = loader.getController();
		FilePreviewAction prev = new FilePreviewAction(controller.getPreviewTab());
		return prev;
	}

	protected void initCloudTab(FXMLLoader loader, UserInfo userInfo, FilePreviewAction dirView ) {
		DMSMasterController controller = loader.getController();
        FileTreeViewAction cloudTree = new FileTreeViewAction(userInfo, userInfo.getUserDropboxRoot());
        cloudTree.setDirView(dirView);
        cloudTree.setFilterField(controller.getSearchTxtFld());
        Parent treeBorderPane = cloudTree.getTree();
        controller.getServerTab().setContent(treeBorderPane);
	}
	
	protected void initLocalTab(FXMLLoader loader, UserInfo userInfo, FilePreviewAction dirView ) {
		DMSMasterController controller = loader.getController();
        FileTreeViewAction localTree = new FileTreeViewAction(userInfo, userInfo.getUserRootDirectory());
        localTree.setDirView(dirView);
        localTree.setFilterField(controller.getSearchTxtFld());
        Parent treeBorderPane = localTree.getTree();
        controller.getLocalTab().setContent(treeBorderPane);
	}
}