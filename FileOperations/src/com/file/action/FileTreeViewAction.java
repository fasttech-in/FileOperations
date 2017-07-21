package com.file.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.file.constant.FileConstants;
import com.file.pojo.FolderDetailVO;
import com.file.ui.DMSMasterPage;
import com.file.ui.FileTreeView;
import com.file.ui.FilterableTreeItem;
import com.file.ui.PopupNotification;
import com.file.ui.controller.DMSMasterController;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;

public class FileTreeViewAction extends FileTreeView {
	static Logger log = Logger.getLogger(FileTreeViewAction.class.getName());
	FilePreviewAction previewAction;

	public FileTreeViewAction(UserInfo userInfo, String loadPath) {
		super(userInfo, loadPath);
	}
	
	protected void getSelectInViewMenuItemAction(
			TreeView<Resource> treeView) {
		 TreeItem<Resource> item = treeView.getSelectionModel().getSelectedItem();
    	 filterField.clear();
    	 collapseTreeView(treeView.getRoot());
    	 treeView.getSelectionModel().select(item);
	}
	
	protected void getServerReconnectMenuItemAction() {
		if(userInfo.reconnectDropbox()) {
    		loadServerdata();
    	} else {
    		PopupNotification.showError("Failure-Unable to connect", "Please check internet connection.");
    	}
	}
	
	protected FilterableTreeItem<Resource> loadServerData(DbxClient client,
			String loadingPath, FilterableTreeItem<Resource> node)
			throws DbxException {
		if(userInfo.isDropboxSupported()) {
			if (!loadingPath.startsWith("/")) {
				loadingPath = "/" + loadingPath;
			}
			DbxEntry.WithChildren listing = client
					.getMetadataWithChildren(loadingPath);
	
			if (listing != null) {
				for (DbxEntry child : listing.children) {
					// System.out.println(child.name);
					if (child.isFolder()) {
						FilterableTreeItem<Resource> folder = new FilterableTreeItem<>(new Resource(child.name), new ImageView(CommanUtil.folderNodeImg));
						node.getInternalChildren().add(folder);
	
						loadServerData(client, child.path, folder);
						treeView.refresh();
					} else {
						FilterableTreeItem<Resource> file = new FilterableTreeItem<>(new Resource(child.name), new ImageView(CommanUtil.getNodeImage(child.name)));
						node.getInternalChildren().add(file);
						treeView.refresh();
					}
				}
			}
		}
		return node;
	}

	public void setDirView(FilePreviewAction previewAction) {
		this.previewAction = previewAction;
	}

	@Override
	protected void loadDirectoryView(String path) {
		previewAction.preview(path);
	}

	@Override
	protected void getDeleteMenuItemAction(TreeView<Resource> treeView) {
		TreeItem<Resource> item =(TreeItem) treeView.getSelectionModel().getSelectedItem();
		String treeSelectedPath = getTreeSelectedPath(treeView);

		if(treeSelectedPath.equals(userInfo.getUserRootDirectory()) ||
				treeSelectedPath.equals(userInfo.getUserDropboxRoot())) {
			PopupNotification.showError("Error-Delete", "Root folder cannot be deleted.");
		} else {
			if(OperationUtil.getFileOperations().deleteFile(treeSelectedPath)) {
				FilterableTreeItem parent = (FilterableTreeItem)item.getParent();
				if(parent!=null) {
					parent.getInternalChildren().remove(item);
					treeView.refresh();
				} else {
					loadLocalData();
				}
				PopupNotification.showSuccess("Success-Delete", "Deleted successfully.");
				ObservableList<FolderDetailVO> data =
			            CommanUtil.getRecentTableContentList();
				
				data.add(new FolderDetailVO(FileUtils.getFile(treeSelectedPath),FileConstants.FileOperationAction.DELETE_ACTION));
				DatabaseDAOAction.updateRecent(data);
			}
		}
	}

	@Override
	protected void getUploadLocalMenuItemAction(TreeView<Resource> treeView) {
		if(CommanUtil.getFXMLLoader()!=null) {
			DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
			
			File toFile = controller.getDmsImpl().showDirectoryChooser(null);
			if (toFile != null) {
				controller.getDmsImpl().uploadButtonActionPerformed(toFile.getAbsolutePath(),
						getTreeSelectedPath(treeView));
			}
			
		}
	}

	@Override
	protected void getDownloadLocalMenuItemAction(TreeView<Resource> treeView) {
		if(CommanUtil.getFXMLLoader()!=null) {
			DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
			
			File toFile = controller.getDmsImpl().showDirectoryChooser(null);
			if (toFile != null) {
				controller.getDmsImpl().downloadButtonActionPerformed(getTreeSelectedPath(treeView), 
					toFile.getAbsolutePath());
			}
		}
		
	}

	@Override
	protected void getLocalReloadMenuItemAction(TreeView<Resource> treeView) {
		loadLocalData();
	}

	@Override
	protected void getPasteMenuItemAction(TreeView<Resource> treeView) {
		String pastePath = getTreeSelectedPath(treeView);
		DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
		controller.getDmsImpl().uploadButtonActionPerformed(copyPath, pastePath);
		if(isCutFlag) {
			FilterableTreeItem parent = (FilterableTreeItem)cutItem.getParent();
			if(OperationUtil.getFileOperations().deleteFile(copyPath)) {
				if(parent!=null) {
				parent.getInternalChildren().remove(cutItem);
					treeView.refresh();
				} else {
					loadLocalData();
				}
			}
		}
		modifyMenuItem(treeView, "Paste", false);
		copyPath = "";
		isCutFlag = false;
	}

	@Override
	protected void getCopyMenuItemAction(TreeView<Resource> treeView) {
		copyPath = getTreeSelectedPath(treeView);
		isCutFlag = false;
		modifyMenuItem(treeView, "Paste", true);
	}

	@Override
	protected void getCutMenuItemAction(TreeView<Resource> treeView) {
		isCutFlag = true;
		cutItem =(TreeItem) treeView.getSelectionModel().getSelectedItem();
		copyPath = getTreeSelectedPath(treeView);
		
		modifyMenuItem(treeView, "Paste", true);
	}

	@Override
	protected void getOpenMenuItemAction(TreeView<Resource> treeView) {
		String treeSelectedPath = getTreeSelectedPath(treeView);
		try {
			OperationUtil.getFileOperations().openFile(treeSelectedPath);
		} catch (Exception e) {
			PopupNotification.showError("Error-Open", "Unable to open.");
			log.info(e);
		} 
	}

	@Override
	protected void getSynchronizeMenuItemAction(TreeView<Resource> treeView) {
		try {
			if(OperationUtil.getFileOperations().syncFiles(loadPath)) {
				CommanUtil.loadLocalTreeData();
				CommanUtil.loadCloudTreeData();
			}
		} catch (Exception e) {
			log.info(e);
		}
	}

	@Override
	protected void registerDoubleClickAction(TreeView<Resource> treeView) {
		treeView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		        	getSelectInViewMenuItemAction(treeView);
		        }
		    }
		});
	}

	@Override
	protected void getCollapseMenuItemAction(TreeView<Resource> treeView) {
		collapseTreeView(treeView.getRoot());
		treeView.getRoot().setExpanded(true);
	}
	
	private void collapseTreeView(TreeItem<?> item) {
	    if(item != null && !item.isLeaf()) {
	        item.setExpanded(false);
	        for(TreeItem<?> child:item.getChildren()){
	            collapseTreeView(child);
	        }
	    }
	    
	}

	@Override
	protected void getAttachmentMenuItemAction(TreeView<Resource> treeView) {
		String treeSelectedPath = getTreeSelectedPath(treeView);
		DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
		controller.addAttachmentToEmail(treeSelectedPath);
	}

	@Override
	protected void getSendEmailMenuItemAction(TreeView<Resource> treeView) {
		DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
		controller.getDmsImpl().emailBtnActionPerformed(controller.getEmailFilesListView().getItems());
		controller.clearAttachments();
	}

	@Override
	protected void clearAttachments() {
		DMSMasterController controller = CommanUtil.getFXMLLoader().getController();
		controller.clearAttachments();
	}
}
