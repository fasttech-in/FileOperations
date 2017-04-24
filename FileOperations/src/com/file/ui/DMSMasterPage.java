package com.file.ui;

import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;

import org.eclipse.fx.ui.controls.filesystem.DirectoryTreeView;
import org.eclipse.fx.ui.controls.filesystem.DirectoryView;
import org.eclipse.fx.ui.controls.filesystem.IconSize;
import org.eclipse.fx.ui.controls.filesystem.ResourceItem;
import org.eclipse.fx.ui.controls.filesystem.ResourcePreview;
import org.eclipse.fx.ui.controls.filesystem.RootDirItem;

import com.dropbox.core.DbxException;
import com.file.abstracts.Operations;
import com.file.operations.FileOperations;
import com.user.info.UserInfo;

public class DMSMasterPage extends Application {

	private static RootDirItem rootDirItem;
	Operations ops;

	public DMSMasterPage() {

		UserInfo userInfo = new UserInfo("Testuser",
				"C:\\TEMP\\test\\dest\\Testuser\\", null);

		ops = new FileOperations(userInfo);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootDirItem = ResourceItem.createObservedPath(Paths
				.get(ops.getUserRootDirectory()));

		TextField srcTxt = new TextField();
		TextField destTxt = new TextField();

		DirectoryTreeView tv = new DirectoryTreeView();
		tv.setIconSize(IconSize.MEDIUM);
		tv.setRootDirectories(FXCollections.observableArrayList(rootDirItem));

		DirectoryView v = new DirectoryView();
		v.setIconSize(IconSize.MEDIUM);

		tv.getSelectedItems().addListener(
				(Observable o) -> {
					if (!tv.getSelectedItems().isEmpty()) {
						v.setDir(tv.getSelectedItems().get(0));
						srcTxt.setText(tv.getSelectedItems().get(0).getUri()
								.replace("file:/", ""));
					} else {
						v.setDir(null);
					}
		});

		ResourcePreview prev = new ResourcePreview();
		v.getSelectedItems().addListener((Observable o) -> {
			if (v.getSelectedItems().size() == 1) {
				prev.setItem(v.getSelectedItems().get(0));
			} else {
				prev.setItem(null);
			}
		});
		v.setPrefHeight(500);

		VBox vbox = new VBox();
		vbox.setSpacing(10);

		Label l1 = new Label("Source Directory");
		Label l2 = new Label("Destination Directory");

		Button uploadBtn = new Button("Upload");
		uploadBtn.setPrefWidth(120);
		Button downloadBtn = new Button("Download");
		downloadBtn.setPrefWidth(120);
		Button openBtn = new Button("Open");
		openBtn.setPrefWidth(120);
		Button deleteBtn = new Button("Delete");
		deleteBtn.setPrefWidth(120);
		Button zipBtn = new Button("Zip");
		zipBtn.setPrefWidth(120);
		uploadBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String srcPath = srcTxt.getText();
				String destPath = destTxt.getText();
				try {
					ops.upload(srcPath, destPath);
				} catch (IOException e) {
				} catch (DbxException e) {
				}
			}
		});
		zipBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String srcPath = srcTxt.getText();
				String destPath = destTxt.getText();
				try {
					ops.zip(srcPath, destPath);
				} catch (IOException e) {
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		downloadBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String srcPath = srcTxt.getText();
				String destPath = destTxt.getText();
				try {
					ops.download(srcPath, destPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		});

		openBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					String path = v.getSelectedItems().get(0).getUri().replace("file:/", "");
					ops.openFile(path);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		});
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String path = v.getSelectedItems().get(0).getUri()
						.replace("file:/", "");
				ops.deleteFile(path);
			}
		});
		HBox hbox = new HBox();
		vbox.getChildren().add(v);
		vbox.getChildren().add(l1);
		vbox.getChildren().add(srcTxt);
		vbox.getChildren().add(l2);
		vbox.getChildren().add(destTxt);
		hbox.getChildren().add(uploadBtn);
		hbox.getChildren().add(downloadBtn);
		hbox.getChildren().add(openBtn);
		hbox.getChildren().add(deleteBtn);
		hbox.getChildren().add(zipBtn);
		vbox.getChildren().add(hbox);

		SplitPane p = new SplitPane(tv, vbox, prev);
		p.setDividerPositions(0.2, 0.653);
		double hgt = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double wdt = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		Scene s = new Scene(p, wdt-20, hgt-70);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		DMSMasterPage sample = new DMSMasterPage();
		Application.launch(args);
		rootDirItem.dispose();
	}
}
