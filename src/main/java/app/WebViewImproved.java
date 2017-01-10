package app;


import javafx.scene.web.WebView;


public abstract class WebViewImproved extends View {

	WebView jfxWebView;


	WebViewImproved() {
		super("");
		jfxWebView = new WebView();
		jfxWebEngine = jfxWebView.getEngine();
		
		jfxWebView.setContextMenuEnabled(false);
	}

	WebViewImproved(String id) {
		super(id);

		jfxWebView = new WebView();
		jfxWebEngine = jfxWebView.getEngine();
		jfxWebView.setContextMenuEnabled(false);

	}

	public void load(String ressource) {
		jfxWebEngine.load(getClass().getResource("/" + ressource).toExternalForm());
	}

	public WebView get() {
		return jfxWebView;
	}

	public void loadURL(String url) {
		jfxWebEngine.load(url);
	}

}
