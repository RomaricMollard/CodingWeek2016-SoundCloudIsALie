package app;


import core.MyChannel;
import core.StoredData;
import core.YT;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DownloadModel;
import model.StreamModel;
import model.UploadManager;


public class App extends Application {

	public static Window mainWindow;
	public static YT youtube;
	public static StreamModel player;
	public static MyChannel myChannel;
	public static UploadManager uploadManager = new UploadManager();


	@Override
	public void start(Stage primaryStage) throws Exception {

		StoredData.load();

		mainWindow = new Window(primaryStage);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				DownloadModel.stopAll();
				StoredData.save();
				System.out.println("Properly exited.");
			}
		});

	}

}
