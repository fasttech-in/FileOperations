package com.file.action;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.file.pojo.FolderDetailVO;

public class DatabaseDAOAction {

	public DatabaseDAOAction() {

	}

	public static void updateRecent(ObservableList<FolderDetailVO> detailsList) {
		loadRecent(detailsList);
	}

	public static void updatePending(ObservableList<FolderDetailVO> detailsList) {
		loadPending(detailsList);
	}

	public static ObservableList<FolderDetailVO> loadRecent(ObservableList<FolderDetailVO>... detailsList) {
		if(detailsList.length>0)
		 return detailsList[0];
		else{
			return FXCollections.observableArrayList();
		}
	}

	public static ObservableList<FolderDetailVO> loadPending(ObservableList<FolderDetailVO>... detailsList) {
		if(detailsList.length>0)
		 return detailsList[0];
		else {
			return FXCollections.observableArrayList();
		}
	}
	
}
