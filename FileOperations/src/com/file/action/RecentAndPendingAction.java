package com.file.action;

import javafx.collections.ObservableList;

import com.file.constant.FileConstants;
import com.file.operations.RecentAndPendingOperations;
import com.file.pojo.FolderDetailVO;
import com.file.ui.controller.DMSMasterController;
import com.file.util.CommanUtil;

public class RecentAndPendingAction {

	private String actionType;
	private ObservableList<FolderDetailVO> contentList;

	public RecentAndPendingAction(String actionType) {
		this.actionType = actionType;
	}

	public void loadTableData(DMSMasterController controller) {
		RecentAndPendingOperations ops = null;
		if (actionType.equals(FileConstants.PendingRecentAction.PENDING_ACTION)) {
			 ops = new RecentAndPendingOperations(
					controller.getPendingTable(), actionType);
			 contentList = ops.loadPreview();
			 CommanUtil.setPendingTableContentList(contentList);
		} else if (actionType.equals(FileConstants.PendingRecentAction.RECENT_ACTION)) {
			 ops = new RecentAndPendingOperations(
					controller.getRecentTable(), actionType);
			 contentList = ops.loadPreview();
			 CommanUtil.setRecentTableContentList(contentList);
		}
	}
	
	public void addContent(ObservableList<FolderDetailVO> contents) {
		contents.forEach(folderDetailVO->{
			contentList.add(folderDetailVO);
		});
	}
}
