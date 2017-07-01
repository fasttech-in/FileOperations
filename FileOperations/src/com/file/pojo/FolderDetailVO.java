package com.file.pojo;

import java.io.File;

import com.file.util.OperationUtil;

import javafx.beans.property.SimpleStringProperty;

public class FolderDetailVO {

	private final SimpleStringProperty name;
	private final SimpleStringProperty action;
    private final SimpleStringProperty lastModified;
    private final SimpleStringProperty size;
    
	public FolderDetailVO(String n, String l, String s, String a) {
		this.name = new SimpleStringProperty(n);
        this.lastModified = new SimpleStringProperty(l);
        this.action = new SimpleStringProperty(a);
        this.size = new SimpleStringProperty(s);
	}
	
	public FolderDetailVO(File f, String a) {
		this.name = new SimpleStringProperty(f.getName());
        this.lastModified = new SimpleStringProperty(OperationUtil.getFileOperations().getLastModifiedDate(f));
        this.size = new SimpleStringProperty(OperationUtil.getFileOperations().getSize(f, null, "2"));
        this.action = new SimpleStringProperty(a);
	}

	public String getName() {
		return name.get();
	}

	public String getLastModified() {
		return lastModified.get();
	}

	public String getSize() {
		return size.get();
	}

	public String getAction() {
		return action.get();
	}

}
