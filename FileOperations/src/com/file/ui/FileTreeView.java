package com.file.ui;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;
import com.user.info.UserInfo;


public abstract class FileTreeView {

	protected TextField filterField;
	protected UserInfo userInfo;
	protected FilterableTreeItem<Resource> root;
	protected TreeView<Resource> treeView;
	protected String loadPath = "";
	private TitledPane treeViewTitlePane;
	protected String CLOUD_UNAVAILABLE = "No Internet Connection.";
	protected boolean isCutFlag = false;
	protected String copyPath = "";
	protected TreeItem<Resource> cutItem;
	
	public FileTreeView(UserInfo userInfo, String loadPath) {
		this.loadPath = loadPath;
		this.userInfo = userInfo;
		 
	}

	public Parent getTreePane() {
		VBox vbox = new VBox(6);
		Node demoPane = createDemoPane();
		VBox.setVgrow(demoPane, Priority.ALWAYS);
		vbox.getChildren().add(demoPane);
		return new BorderPane(vbox);
	}

	private Node createDemoPane() {
		HBox hbox = new HBox(6);
		Node filteredTree = createFilteredTree();
		HBox.setHgrow(filteredTree, Priority.ALWAYS);
		hbox.getChildren().add(filteredTree);
		return hbox;
	}

	private Node createFilteredTree() {
		treeView = getTreeModel();
		root.predicateProperty().bind(
				Bindings.createObjectBinding(() -> {
					if (filterField.getText() == null|| filterField.getText().isEmpty())
						return null;
					return TreeItemPredicate.create(actor -> actor.toString().toLowerCase().contains(filterField.getText().toLowerCase()));
				}, filterField.textProperty()));
		
		root.setExpanded(true);
		addContextMenu(treeView);
		treeViewTitlePane = new TitledPane(loadPath, treeView);
		treeViewTitlePane.setCollapsible(false);
		treeViewTitlePane.setMaxHeight(Double.MAX_VALUE);
		return treeViewTitlePane;
	}
 
	private void addContextMenu(TreeView<Resource> treeView) {
		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
		    getTreeSelectedPath(event, treeView);
		};	
		treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
		
		ContextMenu rootContextMenu = new ContextMenu();
		
		if(loadPath!=null) {
			if (isCloudPath(loadPath)) {
				MenuItem serverReconnect = getServerReconnectMenuItem(treeView,"Refresh");
				rootContextMenu.getItems().add(serverReconnect);
				MenuItem selectInView = getSelectInViewMenuItem(treeView);
				rootContextMenu.getItems().add(selectInView);
				MenuItem openMenu = getOpenMenuItem(treeView);
				rootContextMenu.getItems().add(openMenu);
				MenuItem downloadMenu = getDownloadMenuItem(treeView);
				rootContextMenu.getItems().add(downloadMenu);
				MenuItem deleteMenu = getDeleteMenuItem(treeView);
				rootContextMenu.getItems().add(deleteMenu);
				MenuItem synchronizeMenu = getSynchronizeMenuItem(treeView);
				rootContextMenu.getItems().add(synchronizeMenu);
			} else {
				MenuItem localReload = getLocalReloadMenuItem(treeView);
				rootContextMenu.getItems().add(localReload);
				MenuItem selectInView = getSelectInViewMenuItem(treeView);
				rootContextMenu.getItems().add(selectInView);
//				MenuItem addFolderMenu = getAddFolderMenuItem(treeView);
//				rootContextMenu.getItems().add(addFolderMenu);
				MenuItem openMenu = getOpenMenuItem(treeView);
				rootContextMenu.getItems().add(openMenu);
				MenuItem uploadMenu = getUploadLocalMenuItem(treeView);
				rootContextMenu.getItems().add(uploadMenu);
				MenuItem downloadMenu = getDownloadMenuItem(treeView);
				rootContextMenu.getItems().add(downloadMenu);
				MenuItem deleteMenu = getDeleteMenuItem(treeView);
				rootContextMenu.getItems().add(deleteMenu);
				MenuItem copyMenu = getCopyMenuItem(treeView);
				rootContextMenu.getItems().add(copyMenu);
				MenuItem cutMenu = getCutMenuItem(treeView);
				rootContextMenu.getItems().add(cutMenu);
				MenuItem pasteMenu = getPasteMenuItem(treeView);
				rootContextMenu.getItems().add(pasteMenu);
				MenuItem synchronizeMenu = getSynchronizeMenuItem(treeView);
				rootContextMenu.getItems().add(synchronizeMenu);
			}
		} else {
			if(CLOUD_UNAVAILABLE.equals(treeView.getRoot().getValue().resourceName)) {
				MenuItem serverReconnect = getServerReconnectMenuItem(treeView,"Reconnect");
				rootContextMenu.getItems().add(serverReconnect);
			}
		}
		if (rootContextMenu.getItems().size() > 0)
			treeView.setContextMenu(rootContextMenu);
	
	}
	
	private MenuItem getSynchronizeMenuItem(TreeView<Resource> treeView2) {
		MenuItem synchronizeMenu = new MenuItem("Synchronize",new ImageView(CommanUtil.synchronizeNodeImg));
		synchronizeMenu.setOnAction(t -> getSynchronizeMenuItemAction(treeView));
		return synchronizeMenu;
	}

	private MenuItem getOpenMenuItem(TreeView<Resource> treeView2) {
		MenuItem openMenu = new MenuItem("Open",new ImageView(CommanUtil.folderOpenNodeImg));
		openMenu.setOnAction(t -> getOpenMenuItemAction(treeView));
		return openMenu;
	}

	private MenuItem getPasteMenuItem(TreeView<Resource> treeView2) {
		MenuItem pasteMenu = new MenuItem("Paste",new ImageView(CommanUtil.pasteNodeImg));
		pasteMenu.setOnAction(t -> getPasteMenuItemAction(treeView));
		pasteMenu.setDisable(true);
		return pasteMenu;
	}

	private MenuItem getCopyMenuItem(TreeView<Resource> treeView2) {
		MenuItem copyMenu = new MenuItem("Copy",new ImageView(CommanUtil.copyNodeImg));
		copyMenu.setOnAction(t -> getCopyMenuItemAction(treeView));
		return copyMenu;
	}

	private MenuItem getCutMenuItem(TreeView<Resource> treeView2) {
		MenuItem cutMenu = new MenuItem("Cut",new ImageView(CommanUtil.cutNodeImg));
		cutMenu.setOnAction(t -> getCutMenuItemAction(treeView));
		return cutMenu;
	}

	private MenuItem getLocalReloadMenuItem(TreeView<Resource> treeView) {
		MenuItem localReload = new MenuItem("Refresh",new ImageView(CommanUtil.refreshNodeImg));
		localReload.setOnAction(t -> getLocalReloadMenuItemAction(treeView));
		return localReload;
	}

	private MenuItem getAddFolderMenuItem(TreeView<Resource> treeView2) {
		MenuItem addFolderMenu = new MenuItem("New folder",new ImageView(CommanUtil.addfolderNodeImg));
//		addFolderMenu.setOnAction(t -> getAddFolderMenuItemAction(treeView));
		return addFolderMenu;
	}

	private MenuItem getSelectInViewMenuItem(TreeView<Resource> treeView) {
		MenuItem selectInView = new MenuItem("Select in tree",new ImageView(CommanUtil.onefingerNodeImg));
		selectInView.setOnAction(t -> getSelectInViewMenuItemAction(treeView));
		return selectInView;
	}
	
	private MenuItem getServerReconnectMenuItem(TreeView<Resource> treeView, String menuName) {
		MenuItem serverReconnect = new MenuItem(menuName,new ImageView(CommanUtil.refreshNodeImg));
		serverReconnect.setOnAction(t -> getServerReconnectMenuItemAction());
		return serverReconnect;
	}
	
	private MenuItem getDeleteMenuItem(TreeView<Resource> treeView) {
		MenuItem deleteMenu = new MenuItem("Delete",new ImageView(CommanUtil.deleteNodeImg));
		deleteMenu.setOnAction(t -> getDeleteMenuItemAction(treeView));
		return deleteMenu;
	}
	
	private MenuItem getUploadLocalMenuItem(TreeView<Resource> treeView) {
		MenuItem localDelete = new MenuItem("Upload",new ImageView(CommanUtil.uploadNodeImg));
		localDelete.setOnAction(t -> getUploadLocalMenuItemAction(treeView));
		return localDelete;
	}
	
	private MenuItem getDownloadMenuItem(TreeView<Resource> treeView) {
		MenuItem localDelete = new MenuItem("Download",new ImageView(CommanUtil.downloadNodeImg));
		localDelete.setOnAction(t -> getDownloadLocalMenuItemAction(treeView));
		return localDelete;
	}

	private TreeView<Resource> getTreeModel() {
		String rootName = getRootName(loadPath);
		if(rootName==null){
			rootName = CLOUD_UNAVAILABLE;
			PopupNotification.showError("Failure-Unable to connect", "Please check internet connection.");
		}
		root = new FilterableTreeItem<>(new Resource(
				rootName), new ImageView(CommanUtil.folderNodeImg));
		TreeView<Resource> treeView = new TreeView<>(root);
		treeView.setShowRoot(true);
		if(loadPath!=null) {
			final Task task = new Task<Boolean>() {
				@Override
				protected Boolean call() {
					File rootFolder = new File(loadPath);
					if (rootFolder.exists()) {
						loadLocalData();
					} else {
						if( userInfo.getUserVO().isServerLoadOnStartup()) {
							loadServerdata();
						} 
					}
					return true;
				}
			};
			new Thread(task).start();
		}
		return treeView; 
	}

	public void loadServerdata() {
		final Task task = new Task<Void>() {
			
			@Override
			public Void call() {
				try {
					if(userInfo.isDropboxSupported()) {
						root.getInternalChildren().clear();
						loadServerData(userInfo.getClient(), loadPath, root);
					}
				} catch (DbxException e) {
					PopupNotification.showError("Failure-Unable to connect", "Please check internet connection.");
				}
				return null;
			}
		};
		new Thread(task).start();		
	}

	private String getRootName(String loadPath) {
		String rootName=null;
		if(loadPath!=null) {
			if(loadPath.contains("\\")) {
				rootName = loadPath.substring(loadPath.lastIndexOf("\\")+1,loadPath.length());
			} else {
				rootName = loadPath.substring(loadPath.lastIndexOf("/")+1, loadPath.length());
			}
		}
		return rootName;
	}
	
	public void loadLocalData() {
		root.getInternalChildren().clear();
		loadData(loadPath,root);
	}

	private FilterableTreeItem<Resource> loadData(String loadingPath, FilterableTreeItem<Resource> root) {
		File rootFolder = new File(loadingPath);	
		for(File f : rootFolder.listFiles()) {
			if(f.isDirectory()) {
				FilterableTreeItem<Resource> folder = new FilterableTreeItem<>(new Resource(f.getName()), new ImageView(CommanUtil.folderNodeImg));
				root.getInternalChildren().add(folder);
				treeView.refresh();
				loadData(f.getAbsolutePath(), folder);
			} else {
				FilterableTreeItem<Resource> file = new FilterableTreeItem<>(new Resource(f.getName()), new ImageView(CommanUtil.getNodeImage(f.getName())));
				root.getInternalChildren().add(file);
				treeView.refresh();
			}
		}
		return root;
	}

	public static class Resource {
		public String resourceName;

		public Resource(String string) {
			this.resourceName = string;
		}

		@Override
		public String toString() {
			return resourceName;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String getTreeSelectedPath(MouseEvent event, TreeView<Resource> treeView) {
		String path ="";
		try { 
		    path = getTreeSelectedPath(treeView);
		    loadDirectoryView(path);
		    path = userInfo.getUserDisplayPath(path);
		    treeViewTitlePane.setText(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return path;
	}

	protected String getTreeSelectedPath(TreeView<Resource> treeView) {
		String path;
		StringBuilder pathBuilder = new StringBuilder();	
		for (TreeItem<Resource> item =(TreeItem) treeView.getSelectionModel().getSelectedItem();
			    item != null ; item = item.getParent()) {
			    pathBuilder.insert(0, item.getValue());
			    pathBuilder.insert(0, File.separator);
		}
		path = pathBuilder.toString();
		if(loadPath!=null) {
			path = loadPath+(path.replace(File.separator+root.getValue().resourceName, ""));
			if(!path.startsWith("/")) {
				path = path.replace("/", "\\");
			} else {
				path = path.replace("\\", "/");
			}
		}
		return path;
	}

	public void setFilterField(TextField filterField) {
		this.filterField = filterField;
	}
	
	private boolean isCloudPath(String path) {
		return OperationUtil.getFileOperations().isCloudPath(path);
	}
	
	protected void modifyMenuItem(TreeView<Resource> treeView, String menuName, boolean flag) {
		treeView.getContextMenu().getItems().forEach(menu->{
			if(menu.getText().equals(menuName)) {
				menu.setDisable(!flag);
			}
		});
	}
	
	
	protected abstract FilterableTreeItem<Resource> loadServerData(DbxClient client, String loadingPath,
			FilterableTreeItem<Resource> node) throws DbxException;
	protected abstract void getSynchronizeMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getPasteMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getCopyMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getCutMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getOpenMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getLocalReloadMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getUploadLocalMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getDownloadLocalMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getDeleteMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getSelectInViewMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getServerReconnectMenuItemAction();
	protected abstract void loadDirectoryView(String path);
}
