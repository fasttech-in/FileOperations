package com.file.ui;

import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import com.file.util.CommanUtil;
import com.user.info.UserVO;

public class LoginDialog {

	private boolean isSuccess = false;
	private UserVO vo;

	public LoginDialog(List<UserVO> users) {
		init(users);
		
	}

	private void init(List<UserVO> users) {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login");
		dialog.setHeaderText("Select client to connect.");
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(CommanUtil.fileNodeImg);

		// Set the icon (must be included in the project).
		dialog.setGraphic(new ImageView(CommanUtil.loginNodeImg));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		ComboBox<String> clientNamesCombo = new ComboBox<String>();
		users.forEach(user->{
			clientNamesCombo.getItems().add(user.getClientName());
		});
		clientNamesCombo.getSelectionModel().select(0);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Client Name:"), 0, 0);
		grid.add(clientNamesCombo, 1, 0);
		grid.add(new Label("Username:"), 0, 1);
		grid.add(username, 1, 1);
		grid.add(new Label("Password:"), 0, 2);
		grid.add(password, 1, 2);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {

			for(UserVO user : users) {
				String clientName = clientNamesCombo.getSelectionModel().getSelectedItem();
				if(user.getClientName().equals(clientName)) {
					if(usernamePassword.getKey().equals(user.getUserName()) &&
				    		usernamePassword.getValue().equals(user.getPassword())) {
						vo = user;
				    	isSuccess = true;
				    } else {
				    	isSuccess = false;
				    }
					break;
				}
			}
		});
		
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	
	public UserVO getUser() {
		return vo;
	}
}
