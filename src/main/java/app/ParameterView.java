package app;


import java.io.File;
import java.util.HashMap;

import core.ParametersFunction;
import javafx.scene.web.WebEngine;
import javafx.stage.DirectoryChooser;


public class ParameterView extends View {

	String filePath;


	public ParameterView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		filePath = ParametersFunction.loaddlPath();
		createBridge(new ParameterViewBridge(), "ParameterViewBridge");
	}


	@Override
	public String getHTML() {
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("path", filePath);
		return doTemplate(View.getHTMLFileContent("parameter_view.html"), info);

	}


	public class ParameterViewBridge extends Bridge {
		public void selectDir() {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Selectionnez l'emplacement ");
			System.out.println("ok");
			File selectedFile = chooser.showDialog(app.App.mainWindow.primaryStage);
			filePath = selectedFile.getAbsolutePath();
			ParametersFunction.savedlPath(filePath);
			System.out.println(filePath);
			doJS("#path", "html('" + filePath + "')");
			System.out.println(filePath);

		}
	}



}
