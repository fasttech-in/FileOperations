package com.file.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class DMSMobileController {

    @FXML
    private TextField searchTxtFld;
    
    @FXML
    private BorderPane mainPane;

//    @FXML
//    private TitledPane mainTitlePane;

	public TextField getSearchTxtFld() {
		return searchTxtFld;
	}

	public BorderPane getMainPane() {
		return mainPane;
	}

//	public TitledPane getMainTitlePane() {
//		return mainTitlePane;
//	}
    
    
}