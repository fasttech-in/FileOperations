package com.file.ui.controller;

import java.io.File;
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
import javafx.scene.input.KeyEvent;

import com.file.pojo.FolderDetailVO;
import com.file.util.OperationUtil;

public class DMSMasterController {
	DMSMasterControllerImpl dmsImpl;
	
	public DMSMasterController() {
		dmsImpl = new DMSMasterControllerImpl();
		disableOperationButtons();
	}

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
}
