package com.file.action;

import java.io.IOException;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.file.ui.FileTreeView;
import com.file.ui.FilterableTreeItem;
import com.file.ui.MobileFileTreeView;
import com.file.ui.PopupNotification;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;

public class MobileFileTreeViewAction extends MobileFileTreeView {

	public MobileFileTreeViewAction(UserInfo userInfo, String loadPath) {
		super(userInfo, loadPath);
	}
	
	protected void getSelectInViewMenuItemAction(
			TreeView<Resource> treeView) {
		 TreeItem<Resource> item = treeView.getSelectionModel().getSelectedItem();
    	 filterField.clear();
    	 treeView.getSelectionModel().select(item);
	}
	
	protected void getServerReconnectMenuItemAction() {
		if(userInfo.reconnectDropbox()) {
    		loadServerdata(root);
    	} else {
    		PopupNotification.showError("Failure-Unable to connect", "Please check internet connection.");
    	}
	}
	
	protected FilterableTreeItem<Resource> loadServerData(DbxClient client,
			String loadingPath, FilterableTreeItem<Resource> node)
			throws DbxException {
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
		return node;
	}

	@Override
	protected void getDeleteLocalMenuItemAction(TreeView<Resource> treeView) {
	}

	@Override
	protected void loadDirectoryView(String path) {
		try {
			if(path.contains(".")) {
				String tempFilePath = CommanUtil.getTempDirectory();
				String file = OperationUtil.getFileOperations().download(path, tempFilePath);
				OperationUtil.getFileOperations().openFile(file);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
