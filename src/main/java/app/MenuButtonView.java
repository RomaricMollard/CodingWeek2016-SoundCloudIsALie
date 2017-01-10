package app;


import java.util.HashMap;

import javafx.scene.web.WebEngine;


public class MenuButtonView extends View {

	private String label;
	private Integer viewId;


	public MenuButtonView(WebEngine jfxWebEngine, String label, int viewId) {
		this.jfxWebEngine = jfxWebEngine;
		this.label = label;
		this.viewId = viewId;
	}

	@Override
	public String getHTML() {

		HashMap<String, String> info = new HashMap<String, String>();
		info.put("view_id", viewId.toString());
		info.put("label", label);

		return doTemplate(View.getHTMLFileContent("menu_button.html"), info);
	}
}
