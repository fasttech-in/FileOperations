package com.file.test;

import javafx.application.Application;
import javafx.stage.Stage;

import com.file.operations.DataSynchonizationService;
import com.file.util.CommanUtil;

public class Tester extends Application {

	public Tester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		Application.launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DataSynchonizationService service = new DataSynchonizationService(CommanUtil.getPropertyValue("AUTO.SYNC.FILE"));
		try {
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
