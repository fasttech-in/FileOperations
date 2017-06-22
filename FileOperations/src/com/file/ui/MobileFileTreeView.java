package com.file.ui;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.file.ui.MobileFileTreeView.Resource;
import com.file.util.CommanUtil;
import com.user.info.UserInfo;


public abstract class MobileFileTreeView {

	protected TextField filterField;
	protected UserInfo userInfo;
	protected FilterableTreeItem<Resource> root;
	protected TreeView<Resource> treeView;
	protected String loadPath = "";
	private TitledPane treeViewTitlePane;
	protected String CLOUD_UNAVAILABLE = "No Internet Connection.";
	
	public MobileFileTreeView(UserInfo userInfo, String loadPath) {
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
		treeViewTitlePane = new TitledPane("Precise Data Management System", treeView);
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
				MenuItem serverReconnect = getServerReconnectMenuItem(treeView,"Reload");
				rootContextMenu.getItems().add(serverReconnect);
				MenuItem selectInView = getSelectInViewMenuItem(treeView);
				rootContextMenu.getItems().add(selectInView);
			} else {
				MenuItem localDelete = getDeleteLocalMenuItem(treeView);
				rootContextMenu.getItems().add(localDelete);
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

	private MenuItem getSelectInViewMenuItem(TreeView<Resource> treeView) {
		MenuItem selectInView = new MenuItem("Select in tree");
		selectInView.setOnAction(t -> getSelectInViewMenuItemAction(treeView));
		return selectInView;
	}
	
	private MenuItem getServerReconnectMenuItem(TreeView<Resource> treeView, String menuName) {
		MenuItem serverReconnect = new MenuItem(menuName);
		serverReconnect.setOnAction(t -> getServerReconnectMenuItemAction());
		return serverReconnect;
	}
	
	private MenuItem getDeleteLocalMenuItem(TreeView<Resource> treeView) {
		MenuItem localDelete = new MenuItem("Delete");
		localDelete.setOnAction(t -> getDeleteLocalMenuItemAction(treeView));
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
			File rootFolder = new File(loadPath);
			if(rootFolder.exists()) {
				loadLocalData(loadPath,root);
			} else {
				loadServerdata(root);
			}
		}
		return treeView; 
	}

	public void loadServerdata(FilterableTreeItem<Resource> root) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					root.getInternalChildren().clear();
					loadServerData(userInfo.getClient(), loadPath, root);
				} catch (DbxException e) {
					PopupNotification.showError("Failure-Unable to connect", "Please check internet connection.");
				}
			}
		});
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
	
	public void loadLocalData(String loadingPath, FilterableTreeItem<Resource> root) {
		root.getInternalChildren().clear();
		loadData(loadingPath, root);
	}

	private FilterableTreeItem<Resource> loadData(String loadingPath, FilterableTreeItem<Resource> root) {
		File rootFolder = new File(loadingPath);	
		for(File f : rootFolder.listFiles()) {
			if(f.isDirectory()) {
				FilterableTreeItem<Resource> folder = new FilterableTreeItem<>(new Resource(f.getName()), new ImageView(CommanUtil.folderNodeImg));
				root.getInternalChildren().add(folder);
				loadData(f.getAbsolutePath(), folder);
			} else {
				FilterableTreeItem<Resource> file = new FilterableTreeItem<>(new Resource(f.getName()), new ImageView(CommanUtil.getNodeImage(f.getName())));
				root.getInternalChildren().add(file);
			}
		}
		return root;
	}
	
	protected abstract FilterableTreeItem<Resource> loadServerData(DbxClient client, String loadingPath,
				FilterableTreeItem<Resource> node) throws DbxException;

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
		return path.startsWith("/");
	}
	
	protected abstract void getDeleteLocalMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getSelectInViewMenuItemAction(TreeView<Resource> treeView);
	protected abstract void getServerReconnectMenuItemAction();
	protected abstract void loadDirectoryView(String path);
}
