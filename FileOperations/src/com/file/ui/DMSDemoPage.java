package com.file.ui;

import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.user.info.UserInfo;

public class DMSDemoPage extends Application {

	private static RootDirItem rootDirItem;
	Operations ops;

	public DMSDemoPage() {

		UserInfo userInfo;

		userInfo = new UserInfo("Testuser", "C:\\TEMP\\test\\dest\\Testuser\\",
				null);
		ops = new FileOperations(userInfo);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootDirItem = ResourceItem.createObservedPath(Paths
				.get(ops.getUserRootDirectory()));
		ObservableList<ResourceItem>items = rootDirItem.getChildren();

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
				uploadButtonAction(srcTxt, destTxt);
			}
		});
		zipBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				zipButtonAction(srcTxt, destTxt); 
			}
		});
		downloadBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				downloadButtonAction(srcTxt, destTxt);
			}
		});

		openBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openButtonAction(v);
			}
		});
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				deleteButtonAction(v);
			}
		});
		
		VBox vbox = new VBox();
		vbox.setSpacing(10);
		HBox hbox = new HBox();
//		vbox.getChildren().add(v);
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
		
		TitledPane tp1 = new TitledPane("Directory Preview",v);
		tp1.setExpanded(true);
		tp1.setCollapsible(true);
		tp1.setMaxHeight(1500);
		v.setMaxHeight(1500);
		
		TitledPane tp2 = new TitledPane("Controls",vbox);
//		tp2.setExpanded(false);
		Accordion accordion = new Accordion(tp2);
		VBox mvbox = new VBox(tp1, accordion);
		SplitPane p = new SplitPane(tv, mvbox, prev);
		p.setDividerPositions(0.2, 0.653);
		MenuBar menuBar = new MenuBar();
		
		VBox mbox = new VBox(menuBar,p);
		double hgt = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double wdt = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		Scene s = new Scene(mbox, wdt-20, hgt-70);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	private void uploadButtonAction(TextField srcTxt, TextField destTxt) {
		String srcPath = srcTxt.getText();
		String destPath = destTxt.getText();
		try {
			ops.upload(srcPath, destPath);
		} catch (IOException e) {
		} catch (DbxException e) {
		}
	}

	private void zipButtonAction(TextField srcTxt, TextField destTxt) {
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

	private void downloadButtonAction(TextField srcTxt, TextField destTxt) {
		String srcPath = srcTxt.getText();
		String destPath = destTxt.getText();
		try {
			ops.download(srcPath, destPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//					e.printStackTrace();
		}
	}

	private void openButtonAction(DirectoryView v) {
		try {
			String path = v.getSelectedItems().get(0).getUri().replace("file:/", "");
			ops.openFile(path);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
//					e.printStackTrace();
		}
	}

	private void deleteButtonAction(DirectoryView v) {
		String path = v.getSelectedItems().get(0).getUri()
				.replace("file:/", "");
		ops.deleteFile(path);
	}

	public static void main(String[] args) {
//		DMSDemoPage sample = new DMSDemoPage();
		Application.launch(args);
		rootDirItem.dispose();
	}
}
