package com.file.tesst;

import javafx.application.Application;
import javafx.stage.Stage;

import com.file.operations.DataSynchonizationService;

public class Tester extends Application {

	public Tester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		Application.launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.show();
		primaryStage.hide();
		DataSynchonizationService service = new DataSynchonizationService("D:\\CodeExamples\\GIT\\FileOperations\\ProductFiles\\testuser.dms");
		try {
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
