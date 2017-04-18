package com.file.test;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.file.abstracts.Operations;
import com.file.operations.FileOperations;
import com.user.info.UserInfo;

public class FileOperationTest {

	public static void main(String args[]) {
		
		try {
			String fromPath ="C:\\TEMP\\test\\src";
			String toPath ="C:\\TEMP\\test\\dest\\Testuser\\doc2";
			
			UserInfo userInfo = new UserInfo("Testuser", "C:\\TEMP\\test\\dest\\Testuser\\", null);
			
			Operations ops = new FileOperations(userInfo);
			ops.upload(fromPath, toPath);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
