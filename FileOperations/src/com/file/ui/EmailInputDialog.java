package com.file.ui;

import java.util.Optional;

import com.file.util.CommanUtil;
import com.user.info.UserVO;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class EmailInputDialog {
	
	private String toString;
	private String subjectString;
	private String messageString;
	
	public EmailInputDialog() {
		init();
	}

	private void init() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Email");
		dialog.setHeaderText("Please enter To, Subject & Message of email.");

		dialog.setGraphic(new ImageView(CommanUtil.emailNodeImg));

		ButtonType loginButtonType = new ButtonType("Send", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));

		TextField toNames = new TextField();
		toNames.setPromptText("To");
		toNames.setPrefWidth(500);
		TextField subject = new TextField();
		subject.setText("Documents from "+UserVO.getInstance().getClientName());
		TextField message = new TextField();
		message.setPromptText("Message");

		grid.add(new Label("To (Each email id separated by ';')"), 0, 0);
		grid.add(toNames, 0, 1);
		grid.add(new Label("Subject:"), 0, 2);
		grid.add(subject, 0, 3);
		grid.add(new Label("Message:"), 0, 4);
		grid.add(message, 0, 5);

		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		toNames.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> toNames.requestFocus());

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(toNames.getText(), subject.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(res -> {
		    toString = toNames.getText();
		    subjectString = subject.getText();
		    messageString = message.getText();
		});
	}

	public String getToString() {
		return toString;
	}

	public String getSubjectString() {
		return subjectString;
	}

	public String getMessageString() {
		return messageString;
	}
	
}
