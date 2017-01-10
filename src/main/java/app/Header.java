package app;


import core.StoredData;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import model.DownloadModel;


public class Header extends WebViewImproved {

	double xOffset = 0;
	double yOffset = 0;


	public Header(String id, Button drawWindow) {
		super(id);

		load("header.html");

		createBridge(new HeaderBridge(), "HeaderBridge");

		drawWindow.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		drawWindow.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				App.mainWindow.primaryStage.setX(event.getScreenX() - xOffset);
				App.mainWindow.primaryStage.setY(event.getScreenY() - yOffset);
			}
		});



	}


	/* Bridge JS/JAVA */

	public class HeaderBridge extends Bridge {

		double initialHeight;
		double initialWidth;
		boolean maximized = false;


		public void quitApp() {
			DownloadModel.stopAll();
			StoredData.save();
			Platform.exit();
		}

		public void reduceApp() {
			App.mainWindow.primaryStage.setIconified(true);
		}

		public void maximizeApp() {

			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();

			maximized = !maximized;

			if (maximized) {

				initialWidth = App.mainWindow.primaryStage.getWidth();
				initialHeight = App.mainWindow.primaryStage.getHeight();

				App.mainWindow.primaryStage.setX(bounds.getMinX());
				App.mainWindow.primaryStage.setY(bounds.getMinY());
				App.mainWindow.primaryStage.setWidth(bounds.getWidth());
				App.mainWindow.primaryStage.setHeight(bounds.getHeight());
			}
			else {
				App.mainWindow.primaryStage.setX((bounds.getWidth() - initialWidth) / 2);
				App.mainWindow.primaryStage.setY((bounds.getHeight() - initialHeight) / 2);
				App.mainWindow.primaryStage.setWidth(initialWidth);
				App.mainWindow.primaryStage.setHeight(initialHeight);
			}

			App.mainWindow.resize();

		}

	}

}
