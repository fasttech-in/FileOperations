/*
 * Copyright 2006-2016 ICEsoft Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.file.operations.pdf;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

/**
 * The <code>PageImageExtraction</code> class is an example of how to extract
 * images from a PDF document.  A file specified at the command line is opened
 * and any images that are embedded in a documents page is written to file.
 *
 * @since 5.0
 */
public class PageAsImageExtraction {
	int MAX_IMG_CNT = 20;
	Tab pageAsImgTab;
	FlowPane flow;
	
    public void pageImageExtraction(Tab tab, String filePath, int IMG_CNT) {
    	MAX_IMG_CNT = IMG_CNT;
    	pageAsImgTab = tab;
    	flow = new FlowPane();
    	flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(5);
        flow.setHgap(5);
        flow.setAlignment(Pos.TOP_LEFT);
    	
    	final ScrollPane scroll = new ScrollPane();

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Horizontal scroll bar
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
        scroll.setContent(flow);
        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                flow.setPrefWidth(bounds.getWidth());
                flow.setPrefHeight(bounds.getHeight());
            }
        });
        pageAsImgTab.setContent(scroll);
        Document document = new Document();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            document.setFile(filePath);
            int pages = document.getNumberOfPages();
            List<Callable<Void>> callables = new ArrayList<Callable<Void>>(pages);
            for (int i = 0; i <= pages; i++) {
            	if(i==MAX_IMG_CNT){
            		break;
            	}
                callables.add(new CapturePageImages(document, i));
            }
            executorService.invokeAll(callables);

            executorService.submit(new DocumentCloser(document)).get();
        } catch (Exception e){
        	
        }
        executorService.shutdown();
    }

    public class CapturePageImages implements Callable<Void> {
        private Document document;
        private int pageNumber;
        float scale = 1.0f;
        float rotation = 0f;
        
        private CapturePageImages(Document document, int pageNumber) {
            this.document = document;
            this.pageNumber = pageNumber;
        }

        public Void call() {
			try {
				BufferedImage image = (BufferedImage) document.getPageImage(
						pageNumber, GraphicsRenderingHints.SCREEN,
						Page.BOUNDARY_CROPBOX, rotation, scale);
				java.awt.Image img = image.getScaledInstance(140, 160,
						java.awt.Image.SCALE_SMOOTH);
				
				SwingNode swingNode = new SwingNode();
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	JLabel l = new JLabel();
						l.setIcon(new ImageIcon(img));
		                swingNode.setContent(l);
		            }
		        });
				
				TitledPane titlePane = new TitledPane();
				titlePane.setText("Page "+(pageNumber+1));
				titlePane.setContent(swingNode);
				
				flow.getChildren().add(titlePane);
				image.flush();
			} catch (Exception e) {
                
            }
            return null;
        }
    }

    /**
     * Disposes the document.
     */
    public class DocumentCloser implements Callable<Void> {
        private Document document;

        private DocumentCloser(Document document) {
            this.document = document;
        }

        public Void call() {
            if (document != null) {
                document.dispose();
//                System.out.println("Document disposed");
            }
            return null;
        }
    }
}
