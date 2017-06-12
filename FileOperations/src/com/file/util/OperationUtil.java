package com.file.util;

import com.file.operations.FileOperations;

public class OperationUtil {

	private static FileOperations ops;
	
	public OperationUtil() {
		// TODO Auto-generated constructor stub
	}

	public static FileOperations getFileOperations() {
		return ops;
	}

	public static void setFileOperations(FileOperations oprs) {
		ops = oprs;
	}

	
}
