package com.file.operations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import com.file.abstracts.RecentAndPendingPreview;
import com.file.action.DatabaseDAOAction;
import com.file.constant.FileConstants;
import com.file.pojo.FolderDetailVO;

public class RecentAndPendingOperations extends RecentAndPendingPreview {

	private TableView<FolderDetailVO> tableView;
	private String actionType;
	
	public RecentAndPendingOperations(TableView<FolderDetailVO> tableView, String actionType) {
		super(tableView);
		this.tableView = tableView;
		this.actionType = actionType;
	}

	@Override
	protected ObservableList<FolderDetailVO> loadContents() {
		if(actionType.equals(FileConstants.PendingRecentAction.PENDING_ACTION)){
			return loadPendingData();
		} else if(actionType.equals(FileConstants.PendingRecentAction.RECENT_ACTION)) {
			return loadRecentData();
		}
		return FXCollections.observableArrayList() ;
	}

	private ObservableList<FolderDetailVO> loadRecentData() {
		return DatabaseDAOAction.loadRecent();
	}

	private ObservableList<FolderDetailVO> loadPendingData() {
		return DatabaseDAOAction.loadPending();
	}

}
