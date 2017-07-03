package com.file.ui;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.file.action.FilePreviewAction;
import com.file.action.FileTreeViewAction;
import com.file.action.RecentAndPendingAction;
import com.file.constant.FileConstants;
import com.file.operations.DataSynchonizationService;
import com.file.operations.FileOperations;
import com.file.pojo.FolderDetailVO;
import com.file.ui.controller.DMSMasterController;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;
import com.user.info.UserVO;

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
        stage.setTitle("Precise - Data Management System");
        stage.getIcons().add(CommanUtil.fileNodeImg);
        stage.show();
        List<UserVO> voList = loadUserData();
        
        LoginDialog login = new LoginDialog(voList);
		if(login.isSuccess()) {
			UserVO vo = login.getUser();
			CommanUtil.setUserSettingsVO(vo);
			initUserSettings(loader, vo);
			UserInfo userInfo = new UserInfo(vo);
	        FileOperations ops = new FileOperations(userInfo);
	        OperationUtil.setFileOperations(ops);
	        CommanUtil.setFXMLLoader(loader);
	        initRecentPendingTables(loader);
	        FilePreviewAction prev = initFilePreview(loader);
	        initLocalTab(loader, userInfo, prev);
	        initCloudTab(loader, userInfo, prev);
	        initAutoSyncService(userInfo);
		} else {
			PopupNotification.showError("Login - Failed", "Invalid username or password");
		}
	}

	private List<UserVO> loadUserData() {
		return CommanUtil.loadAvailableUsers();
	}

	private void initUserSettings(FXMLLoader loader, UserVO vo) {
		DMSMasterController controller = loader.getController();
		
		GridPane serverSetttingsGridPane = controller.getServerSettingsGridPane();
		SwitchButton s1 = new SwitchButton();
		SwitchButton s2 = new SwitchButton();
		SwitchButton s3 = new SwitchButton();
		s1.setSwitchOn(vo.isServerService());
		s2.setSwitchOn(vo.isServerLoadOnStartup());
		s3.setSwitchOn(vo.isServerAutoSync());
		UserVO.getInstance().setServerService(vo.isServerService());
		UserVO.getInstance().setServerLoadOnStartup(vo.isServerLoadOnStartup());
		UserVO.getInstance().setServerAutoSync(vo.isServerAutoSync());
		serverSetttingsGridPane.add(s1, 1, 0);
		serverSetttingsGridPane.add(s2, 1, 1);
		serverSetttingsGridPane.add(s3, 1, 2);
		
		
		Button b1 = (Button)s1.getGraphic();
		b1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				s1.buttonActionPerformed();
				UserVO.getInstance().setServerService(s1.isButtonOn());
			}
		});
		
		Button b2 = (Button)s2.getGraphic();
		b2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				s2.buttonActionPerformed();
				UserVO.getInstance().setServerLoadOnStartup(s2.isButtonOn());
			}
		});
		
		Button b3 = (Button)s3.getGraphic();
		b3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				s3.buttonActionPerformed();
				UserVO.getInstance().setServerAutoSync(s3.isButtonOn());
			}
		});
		
		controller.getClientNameTxtFld().setText(vo.getClientName());
		controller.getContactNoTxtFld().setText(vo.getClientContactNo());
		controller.getUserNameTxtFld().setText(vo.getUserName());
		controller.getPasswordTxtFld().setText(vo.getPassword());
		controller.getAccessKeyTxtFld().setText(vo.getUserAccessKey());
		controller.getDataRootTxtFld().setText(vo.getUserRootDirectory());
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
        Parent treeBorderPane = cloudTree.getTreePane();
        controller.getServerTab().setContent(treeBorderPane);
        CommanUtil.setCloudTreeAction(cloudTree);
	}
	
	protected void initLocalTab(FXMLLoader loader, UserInfo userInfo, FilePreviewAction dirView ) {
		DMSMasterController controller = loader.getController();
        FileTreeViewAction localTree = new FileTreeViewAction(userInfo, userInfo.getUserRootDirectory());
        localTree.setDirView(dirView);
        localTree.setFilterField(controller.getSearchTxtFld());
        Parent treeBorderPane = localTree.getTreePane();
        controller.getLocalTab().setContent(treeBorderPane);
        CommanUtil.setLocalTreeAction(localTree);
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
		
		RecentAndPendingAction pendingAction = new RecentAndPendingAction(FileConstants.PendingRecentAction.PENDING_ACTION);
		pendingAction.loadTableData(loader.getController());
		RecentAndPendingAction recentAction = new RecentAndPendingAction(FileConstants.PendingRecentAction.RECENT_ACTION);
		recentAction.loadTableData(loader.getController());
	}
	
	private void initAutoSyncService(UserInfo userInfo) {
		DataSynchonizationService service = new DataSynchonizationService(OperationUtil.getFileOperations());
		try {
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}