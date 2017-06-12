package com.file.abstracts;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import org.apache.commons.io.FileUtils;

import com.dropbox.core.DbxEntry;
import com.file.Interfaces.IDirectoryPreview;
import com.file.pojo.FolderDetailVO;
import com.file.ui.FilterableTreeItem;
import com.file.ui.FileTreeView.Resource;
import com.file.util.CommanUtil;
import com.file.util.OperationUtil;

public abstract class RecentAndPendingPreview implements IDirectoryPreview {
	
	TableView<FolderDetailVO> tableView;
	
	public RecentAndPendingPreview(TableView<FolderDetailVO> t) {
		tableView = t;
	}

	@Override
	public void loadPreview() {
		tableView.getItems().clear();
		ObservableList<FolderDetailVO> content = loadContents();
		tableView.setItems(content);
		tableView.refresh();
	}

	@Override
	public boolean isFilePresent(String path) {
		return FileUtils.getFile(path).exists();
	}

	@Override
	public ObservableList<?> downloadCloudFile(String loadingPath) {
		return null;
	}

	@Override
	public void setPreviewFile(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getPreviewFile() {
		// TODO Auto-generated method stub
		return null;
	}

	protected abstract ObservableList<FolderDetailVO> loadContents();
}
