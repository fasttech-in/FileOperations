package com.file.ui.controller;

import java.io.File;
import java.util.Base64;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import com.file.pojo.FolderDetailVO;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserVO;

public class DMSMasterController {
	DMSMasterControllerImpl dmsImpl;
	
	public DMSMasterController() {
		dmsImpl = new DMSMasterControllerImpl();
		disableOperationButtons();
		loadAboutUs();
	}

	@FXML
	private Button serverSettingsSaveButton;
	@FXML
	private GridPane serverSettingsGridPane;

    @FXML
    private Tab localTab;

    @FXML
    private Tab pendingTab;

    @FXML
    private Tab downloadTab;

    @FXML
    private Tab filesTab;

    @FXML
    private Tab serverTab;

    @FXML
    private Tab previewTab;

    @FXML
    private Tab recentTab;

    @FXML
    private Tab uploadTab;

    @FXML
    private TextField downloadDestinationTxtFld;
    
    @FXML
    private TextField downloadSourceTxtFld;
    
    @FXML
    private TextField searchTxtFld;
    
    @FXML
    private TextField clientNameTxtFld;

    @FXML
    private TextField contactNoTxtFld;
    
    @FXML
    private TextField userNameTxtFld;
    
    @FXML
    private TextField passwordTxtFld;
    
    @FXML
    private TextField accessKeyTxtFld;
    
    @FXML
    private TextField dataRootTxtFld;
    
    @FXML
    private ImageView aboutImageView;
    
    @FXML
    private TableView<FolderDetailVO> pendingTable;
    
    @FXML
    private TableView<FolderDetailVO> recentTable;
  
    @FXML
    private TableColumn<FolderDetailVO, String> pendingTableNameColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> recentTableSizeColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> pendingTableSizeColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> recentTableNameColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> recentTableDateColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> pendingTableDateColumn;

    @FXML
    private TableColumn<FolderDetailVO, String> recentTableActionColumn;
    
    @FXML
    private TableColumn<FolderDetailVO, String> pendingTableActionColumn;
    
    @FXML
    private ListView<String> emailFilesListView;
    
    @FXML
    private Button downloadBtn;
    
    @FXML
    private Button zipDownloadBtn;
    
    @FXML
    private Button addBtn;
    
    @FXML
    private Button removeBtn;
    
    @FXML
    private Button listClearBtn;
    
    @FXML
    private Button emailBtn;
    
    @FXML
    private Button multiZipBtn;
    
    @FXML
    private Button uploadBtn;
    
    @FXML
    private Button uploadDestinationChooserBtn;
    
    @FXML
    private Button uploadSourceChooserBtn;
    
    @FXML
    private Button uploadNewBtn;
    
    @FXML
    private Button uploadClearBtn;
    
    @FXML
    private TextField uploadDestinationTxtFld;
    
    @FXML
    private TextField uploadSourceTxtFld;
    
    @FXML
    private Button downloadListBtn;
    
    @FXML
    void searchTxtFldKeyReleased(KeyEvent event) {
//    	List<String> searchResults = dmsImpl.search(searchTxtFld.getText(),localTab.isSelected());
    }
    
    @FXML
    void localTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void serverTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void filesTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void pendingTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void recentTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void previewTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void uploadTabSelectionChanged(ActionEvent event) {

    }

    @FXML
    void downloadTabSelectionChanged(ActionEvent event) {

    }
    
    @FXML
    void uploadSourceChooserBtnAction(ActionEvent event) {
    	getDmsImpl().showDirectoryChooser(uploadSourceTxtFld);
    }

    @FXML
    void uploadDestinationChooserBtnAction(ActionEvent event) {
    	getDmsImpl().showDirectoryChooser(uploadDestinationTxtFld);
    }

    @FXML
    void uploadNewBtnAction(ActionEvent event) {
    	uploadSourceTxtFld.clear();
    	uploadDestinationTxtFld.clear();
    }

    @FXML
    void uploadBtnAction(ActionEvent event) {
    	getDmsImpl().uploadButtonActionPerformed(
    			uploadSourceTxtFld.getText(), 
    			uploadDestinationTxtFld.getText());
    }

    @FXML
    void uploadClearBtnAction(ActionEvent event) {
    	uploadSourceTxtFld.clear();
    	uploadDestinationTxtFld.clear();
    }

    @FXML
    void downloadSourceChooserBtnAction(ActionEvent event) {
    	getDmsImpl().showDirectoryChooser(downloadSourceTxtFld);
    }

    @FXML
    void downloadDestinationChooserBtnAction(ActionEvent event) {
    	getDmsImpl().showDirectoryChooser(downloadDestinationTxtFld);
    }

    @FXML
    void downloadNewBtnAction(ActionEvent event) {
    	downloadSourceTxtFld.clear();
    	downloadDestinationTxtFld.clear();
    }

    @FXML
    void downloadBtnAction(ActionEvent event) {
    	getDmsImpl().downloadButtonActionPerformed(
    			downloadSourceTxtFld.getText(), 
    			downloadDestinationTxtFld.getText());
    }

    @FXML
    void zipDownloadBtnAction(ActionEvent event) {
    	getDmsImpl().downloadZipButtonActionPerformed(
    			downloadSourceTxtFld.getText(), 
    			downloadDestinationTxtFld.getText());
    }

	public DMSMasterControllerImpl getDmsImpl() {
		return dmsImpl;
	}


	public Tab getLocalTab() {
		return localTab;
	}


	public Tab getPendingTab() {
		return pendingTab;
	}


	public Tab getDownloadTab() {
		return downloadTab;
	}


	public Tab getFilesTab() {
		return filesTab;
	}


	public Tab getServerTab() {
		return serverTab;
	}


	public Tab getPreviewTab() {
		return previewTab;
	}


	public Tab getRecentTab() {
		return recentTab;
	}


	public Tab getUploadTab() {
		return uploadTab;
	}


	public TextField getSearchTxtFld() {
		return searchTxtFld;
	}

	public TableView<FolderDetailVO> getPendingTable() {
		return pendingTable;
	}

	public TableView<FolderDetailVO> getRecentTable() {
		return recentTable;
	}

	public TableColumn<FolderDetailVO, String> getPendingTableNameColumn() {
		return pendingTableNameColumn;
	}

	public TableColumn<FolderDetailVO, String> getRecentTableSizeColumn() {
		return recentTableSizeColumn;
	}

	public TableColumn<FolderDetailVO, String> getPendingTableSizeColumn() {
		return pendingTableSizeColumn;
	}

	public TableColumn<FolderDetailVO, String> getRecentTableNameColumn() {
		return recentTableNameColumn;
	}

	public TableColumn<FolderDetailVO, String> getRecentTableDateColumn() {
		return recentTableDateColumn;
	}

	public TableColumn<FolderDetailVO, String> getPendingTableDateColumn() {
		return pendingTableDateColumn;
	}

	public TableColumn<FolderDetailVO, String> getRecentTableActionColumn() {
		return recentTableActionColumn;
	}

	public TableColumn<FolderDetailVO, String> getPendingTableActionColumn() {
		return pendingTableActionColumn;
	}
	

    public GridPane getServerSettingsGridPane() {
		return serverSettingsGridPane;
	}

	@FXML
    void multiZipBtnAction(ActionEvent event) {
    	getDmsImpl().multiZipBtnActionPerformed(emailFilesListView.getItems());
    }

    @FXML
    void emailBtnAction(ActionEvent event) {
    	getDmsImpl().emailBtnActionPerformed(emailFilesListView.getItems());
    }

    @FXML
    void addBtnAction(ActionEvent event) {
    	File file = getDmsImpl().showFileChooser();
    	if(file!=null) {
    		emailFilesListView.getItems().add(file.getAbsolutePath());
    	}
    }
    
    

    public TextField getClientNameTxtFld() {
		return clientNameTxtFld;
	}

	public TextField getContactNoTxtFld() {
		return contactNoTxtFld;
	}

	public TextField getUserNameTxtFld() {
		return userNameTxtFld;
	}

	public TextField getPasswordTxtFld() {
		return passwordTxtFld;
	}

	public TextField getAccessKeyTxtFld() {
		return accessKeyTxtFld;
	}

	public TextField getDataRootTxtFld() {
		return dataRootTxtFld;
	}

	@FXML
    void removeBtnAction(ActionEvent event) {
    	ObservableList<String> selectedItems = emailFilesListView.getSelectionModel().getSelectedItems();
    	for (Iterator iterator = selectedItems.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			emailFilesListView.getItems().remove(string);
		}
    }

    @FXML
    void listClearBtnAction(ActionEvent event) {
    	emailFilesListView.getItems().clear();
    }
	
    @FXML
    void downloadListBtnAction(ActionEvent event) {
    	getDmsImpl().multiDownloadBtnActionPerformed(emailFilesListView.getItems());
    }
    
    @FXML
    void serverSettingsSaveButtonAction(ActionEvent event) {
    	saveSettingsCode();
    }
    
    @FXML
    void userSettingsButtonAction(ActionEvent event) {
    	saveSettingsCode();
    }

	protected void saveSettingsCode() {
		UserVO.getInstance().setClientName(clientNameTxtFld.getText());
    	UserVO.getInstance().setClientContactNo(contactNoTxtFld.getText());
    	UserVO.getInstance().setUserName(userNameTxtFld.getText());
    	UserVO.getInstance().setPassword(passwordTxtFld.getText());
    	UserVO.getInstance().setUserAccessKey(accessKeyTxtFld.getText());
    	UserVO.getInstance().setUserRootDirectory(dataRootTxtFld.getText());
    	
    	UserVO.getInstance().setPassword(
    			Base64.getEncoder().encodeToString
    			(UserVO.getInstance().getPassword().getBytes()));
    	UserVO.getInstance().setUserAccessKey(
    			Base64.getEncoder().encodeToString
    			(UserVO.getInstance().getUserAccessKey().getBytes()));
    	
    	CommanUtil.marshal(UserVO.getInstance());
    	PopupNotification.showSuccess("Save - Settings", "Saved successfully.");
	}
    
    protected void disableOperationButtons() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				uploadBtn.setDisable(true);
				downloadBtn.setDisable(true);
				multiZipBtn.setDisable(true);
				downloadListBtn.setDisable(true);
				emailBtn.setDisable(true);
				zipDownloadBtn.setDisable(true);
				listClearBtn.setDisable(true);
				
				if(OperationUtil.getFileOperations()!=null) {
					uploadSourceTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
					    uploadBtn.setDisable(newValue.trim().isEmpty());
					    uploadBtn.setDisable(uploadDestinationTxtFld.getText().trim().isEmpty());
					});
					
					uploadDestinationTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
					    uploadBtn.setDisable(newValue.trim().isEmpty());
					    uploadBtn.setDisable(uploadSourceTxtFld.getText().trim().isEmpty());
					});
					
					downloadSourceTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
					    downloadBtn.setDisable(newValue.trim().isEmpty());
					    downloadBtn.setDisable(downloadDestinationTxtFld.getText().trim().isEmpty());
					});
					
					downloadDestinationTxtFld.textProperty().addListener((observable, oldValue, newValue) -> {
						downloadBtn.setDisable(newValue.trim().isEmpty());
						downloadBtn.setDisable(downloadSourceTxtFld.getText().trim().isEmpty());
					});
					
					downloadBtn.disabledProperty().addListener((observable, oldValue, newValue) -> {
						zipDownloadBtn.setDisable(downloadBtn.isDisabled());
					});
					
					emailFilesListView.getItems().addListener((ListChangeListener<? super String>) change -> {
						multiZipBtn.setDisable(emailFilesListView.getItems().size()==0);
						emailBtn.setDisable(emailFilesListView.getItems().size()==0);
						downloadListBtn.setDisable(emailFilesListView.getItems().size()==0);
						listClearBtn.setDisable(emailFilesListView.getItems().size()==0);
					});
				}
			}
		});
	}
    
    private void loadAboutUs() {
		Platform.runLater(()->{
			aboutImageView.setImage(CommanUtil.aboutNodeImg);
			aboutImageView.setFitWidth(800);
			aboutImageView.setFitHeight(500);
			aboutImageView.setPreserveRatio(true);
		});
	}
}
