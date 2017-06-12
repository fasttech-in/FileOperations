package com.file.operations;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.file.abstracts.Preview;
import com.file.util.OperationUtil;

public class ImagePreviewOperations extends Preview {
	
	private ImageView imageView = null;
	private StackPane root;
	private Tab previewTab;
	private Label l1,l2,l3;
	
	public ImagePreviewOperations(Tab previewTab) {
		super(previewTab);
		this.previewTab = previewTab;
	}

	@Override
	public Parent loadContents(String path) {
		path = getFilePath(path);
		if (imageView == null) {
			root = new StackPane();
			
			imageView = new ImageView(path);
			imageView.fitWidthProperty().bind(previewTab.getTabPane().widthProperty());
			imageView.setPreserveRatio(true);

			VBox infoBox = setFileDetails();
			
			StackPane.setAlignment(imageView, Pos.CENTER);
			root.getChildren().add(imageView);
			root.getChildren().add(infoBox);
			infoBox.toBack();
			
		} else {
			imageView.setImage(new Image(path));
			setFileDetails();
		}
		return root;
	}

	protected String getFilePath(String path) {
		if(!isFilePresent(path)) {
			path = downloadCloudFile(path);
		}
		return "file:"+path;
	}

	protected VBox setFileDetails() {
		VBox infoBox = null;
		if(l1 ==null){
			infoBox = new VBox(5);
			infoBox.getChildren().add(new Label());
			l1 = new Label();
			infoBox.getChildren().add(l1);
			l2 = new Label();
			infoBox.getChildren().add(l2);
			l3 = new Label();
			infoBox.getChildren().add(l3);
		}
		l1.setText("File Name : " + previewFile.getName());
		l2.setText("Last Modified : " + OperationUtil.getFileOperations().getLastModifiedDate(previewFile));
		l3.setText("Size : " + OperationUtil.getFileOperations().getSize(previewFile,null,"2"));
		return infoBox;
	}
}
