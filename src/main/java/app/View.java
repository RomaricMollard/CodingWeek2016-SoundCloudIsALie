package app;


import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import utils.Handle;


public abstract class View {

	WebEngine jfxWebEngine;
	Bridge myBridge;
	String myBridgeName;
	ArrayList<View> views;
	String id;

	int nextChildId;


	public View() {
		views = new ArrayList<View>();
		this.id = "";
		nextChildId = 0;
	}

	public View(String id) {
		views = new ArrayList<View>();
		this.id = id;
		nextChildId = 0;
	}

	/*
	 * get the id of the view
	*/
	public String getId() {
		return id;
	}

	/*
	 * set the id of the view
	*/
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * execute a code with safe conditions (because of WebEngine)
	*/
	public void safeDo(final Handle handle) {

		if (jfxWebEngine.getLoadWorker().stateProperty().get() == Worker.State.SUCCEEDED) {
			handle.execute();
		}
		else {
			jfxWebEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
				public void changed(ObservableValue ov, State oldState, State newState) {

					if (newState == Worker.State.SUCCEEDED) {

						handle.execute();

					}

				}
			});
		}

	}

	/*
	 * initialize a bridge for the view
	 * a bridge is the way that use the javascript to communicate with the Java code
	*/
	public void createBridge(Bridge bridge, String varName) {

		myBridge = bridge;
		myBridgeName = varName;


		safeDo(new Handle() {

			public void execute() {
				JSObject jsobj = (JSObject) jfxWebEngine.executeScript("window");
				jsobj.setMember(myBridgeName, myBridge);
			}

		});

	}

	/*
	 * add a view into the object
	 * child : html id of the container
	 * append : true of the view must be clean
	*/
	public void add(final View contener, final String childId, final boolean append) {
		safeDo(new Handle() {

			public void execute() {
				realAdd(contener, childId, append);
			}

		});
	}

	/*
	 * add a view into the object (with reset of this object)
	*/
	public void add(final View contener) {
		add(contener, "", false);
	}

	/*
	 * add a view into the object (with reset of this object and in a child id)
	*/
	public void add(final View contener, final String childId) {
		add(contener, childId, false);
	}

	/*
	 * add a view into the object (without reset of this object and in a child id)
	*/
	public void append(final View contener, final String childId) {
		add(contener, childId, true);
	}

	/*
	 * add a view into the object (without reset of this object)
	*/
	public void append(final View contener) {
		add(contener, "", true);
	}

	/*
	 * remove a view from the object
	*/
	public void remove(final View contener) {
		safeDo(new Handle() {

			public void execute() {
				realRemove(contener);
			}

		});
	}

	/*
	 * reset the view in a specific HTML child id
	*/
	public void empty(final String childId) {
		safeDo(new Handle() {

			public void execute() {
				realEmpty(childId);
			}

		});
	}

	/*
	 * reset the view
	*/
	public void empty() {
		empty("");
	}

	/*
	 * update the view with the current value of getHTML()
	*/
	public void update() {
		safeDo(new Handle() {

			public void execute() {
				realUpdate();
			}

		});
	}

	/**
	 * Remplace les valeurs de forme {{cle}} dans un html par la valeur correspondante
	 * 
	 * @param template
	 *            code html
	 * @param values
	 *            valeurs à remplacer dans le code HTML
	 * @return the new HTML code
	 */
	public String doTemplate(String template, HashMap<String, String> values) {

		for (Entry<String, String> value : values.entrySet()) {
			template = template.replaceAll("(<sv.*?class=\"" + value.getKey() + "\".*?>).*?< */ *sv>", "$1" + value.getValue().replace("\n", "").replace("\r", "") + "</sv>");
			template = template.replaceAll("\\{\\{" + value.getKey() + "\\}\\}", value.getValue().replace("\n", "<br>").replace("\r", "<br>"));
		}

		return template;

	}

	public void doTemplate(HashMap<String, String> values) {

		for (Entry<String, String> value : values.entrySet()) {
			jfxWebEngine.executeScript("$('#" + id + "').find('sv." + value.getKey() + "').html('" + value.getValue().replace("'", "\\'").replace("\n", "").replace("\r", "") + "')");
		}

		return;

	}

	/*
	 * return the HTML code which must be use for this view
	*/
	public String getHTML() {
		return "";
	}

	/*
	 * actions for the addition of a view
	*/
	private void realAdd(View contener, String childId, boolean append) {

		contener.jfxWebEngine = this.jfxWebEngine;

		views.add(contener);
		contener.setId(id + "_" + nextChildId);

		String ifChild = "";
		if (childId != null && childId != "") {
			ifChild = "').find('#" + childId + "";
		}

		if (!append) {
			jfxWebEngine.executeScript("$('#" + id + ifChild + "').html('<div id=\"" + contener.getId() + "\"></div>')");
		}
		else {
			jfxWebEngine.executeScript("$('#" + id + ifChild + "').append('<div id=\"" + contener.getId() + "\"></div>')");
		}
		jfxWebEngine.executeScript("$('#" + contener.getId() + "').html('" + contener.getHTML().replace("'", "\\'") + "')");

		++nextChildId;

		contener.justAdded();

	}


	/**
	 * Fonction to override, called once added
	 */
	public void justAdded() {
		return;
	}

	/*
	 * actions for the removal of a view
	*/
	private void realRemove(View contener) {
		views.remove(contener);
		jfxWebEngine.executeScript("$('#" + contener.getId() + "').remove()");
	}

	/*
	 * actions for the reset of a view
	*/
	private void realEmpty(String childId) {

		String ifChild = "";
		if (childId != null && childId != "") {
			ifChild = "').find('#" + childId + "";
		}

		jfxWebEngine.executeScript("$('#" + this.getId() + ifChild + "').html('')");

		views.clear();

	}

	/*
	 * actions for the update of a view
	*/
	public void realUpdate() {

		for (View view : views) {
			jfxWebEngine.executeScript("$('#" + view.getId() + "').html('" + view.getHTML().replace("'", "\'") + "')");
		}
	}

	/*
	 * call of a Javascript query for a specific selector
	*/
	public void doJS(String selector, String query) {
		doJS("$('#" + this.id + "').find('" + selector + "')." + query);
	}

	/*
	 * call of a Javascript query
	*/
	public void doJS(String query) {
		jfxWebEngine.executeScript(query);
	}

	/*
	 * return the content of an HTML file (in the "templates" directory only) 
	*/
	static public String getHTMLFileContent(String filePath) {

		String fileContent = "";
		InputStream stream;
		BufferedReader streamFilter;

		try {

			stream = View.class.getResourceAsStream("/" + filePath);

			String res = new Scanner(stream, Charset.defaultCharset().name()).useDelimiter("\\A").next();


			res = res.replace("\n", "");
			res = res.replace("\r", "");
			res = res.replace(System.getProperty("line.separator"), "");

			//System.out.println(res);

			return res;

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "aaa";
	}
}
