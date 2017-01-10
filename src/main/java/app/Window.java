package app;


import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Window {

	public Stage primaryStage;
	private GridPane mainGrid;
	private Pane root;
	public MainContent mainViewer;


	public Window(Stage pStage) {

		this.primaryStage = pStage;

		primaryStage.initStyle(StageStyle.UNDECORATED);

		primaryStage.setTitle("SoundCloudIsALie");


		// Add invisible button to handle window move
		Button drawWindow = new Button();
		drawWindow.setLayoutX(85);
		drawWindow.setLayoutY(0);
		drawWindow.setMinWidth(1000000);
		drawWindow.setMinHeight(30);
		drawWindow.setOpacity(0.0);

		// Create the main grid with header on the top (close, minus, maximize)
		// and the main content (two webviews)
		WebViewImproved header = new Header("header", drawWindow);
		mainViewer = new MainContent("mainContent");

		mainGrid = new GridPane();

		RowConstraints row = new RowConstraints(0);
		RowConstraints row2 = new RowConstraints(30);
		RowConstraints row3 = new RowConstraints();
		row3.setVgrow(Priority.ALWAYS);

		GridPane.setConstraints(header.get(), 1, 1, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
		GridPane.setConstraints(mainViewer.get(), 1, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

		mainGrid.getRowConstraints().addAll(row, row2, row3);
		mainGrid.getChildren().addAll(header.get(), mainViewer.get());

		root = new Pane();

		root.getChildren().add(mainGrid);
		root.getChildren().add(drawWindow);

		Scene scene = new Scene(root, 900, 650, Color.BLACK);

		primaryStage.setScene(scene);
		primaryStage.show();

		resize();

	}

	/**
	 * Force inner content to fill windows size
	 */
	public void resize() {

		mainGrid.setPrefWidth(primaryStage.getWidth());
		mainGrid.setPrefHeight(primaryStage.getHeight());

		primaryStage.show();
	}

}
