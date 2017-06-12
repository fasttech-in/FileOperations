package com.file.action;

import com.file.constant.FileConstants;
import com.file.operations.RecentAndPendingOperations;
import com.file.ui.controller.DMSMasterController;

public class RecentAndPendingAction {

	private String actionType;

	public RecentAndPendingAction(String actionType) {
		this.actionType = actionType;
	}

	public void loadTableData(DMSMasterController controller) {
		RecentAndPendingOperations ops = null;
		if (actionType.equals(FileConstants.PendingRecentAction.PENDING_ACTION)) {
			 ops = new RecentAndPendingOperations(
					controller.getPendingTable(), actionType);
		} else if (actionType.equals(FileConstants.PendingRecentAction.RECENT_ACTION)) {
			 ops = new RecentAndPendingOperations(
					controller.getRecentTable(), actionType);
		}
		ops.loadPreview();
	}
}
