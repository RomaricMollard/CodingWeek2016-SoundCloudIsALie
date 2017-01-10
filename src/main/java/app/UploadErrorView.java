package app;


import javafx.scene.web.WebEngine;


public class UploadErrorView extends View {

	String message;
	boolean error;


	public UploadErrorView(WebEngine jfxWebEngine, String message, boolean error) {
		this.jfxWebEngine = jfxWebEngine;
		this.message = message;
		this.error = error;
	}


	@Override
	public String getHTML() {
		return "<span style='color: " + (error ? "red" : "green") + "'>" + message + "</span>";
	}
}
