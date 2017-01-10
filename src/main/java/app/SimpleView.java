package app;


import java.util.HashMap;


public class SimpleView extends View {

	String HTML;


	SimpleView(String content, HashMap<String, String> values) {
		HTML = doTemplate(content, values);
	}

	SimpleView(String content) {
		HTML = content;
	}

	@Override
	public String getHTML() {
		return HTML;
	}

}
