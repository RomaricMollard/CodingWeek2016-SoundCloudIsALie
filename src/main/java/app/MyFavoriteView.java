package app;


import java.util.ArrayList;

import core.Search;
import core.ShortVideoData;
import core.StoredData;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;


public class MyFavoriteView extends View {

	ArrayList<ShortVideoResultView> resultsViews;
	Search search;


	public MyFavoriteView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		this.resultsViews = new ArrayList<ShortVideoResultView>();
		createBridge(new MyFavoriteViewBridge(), "MyFavoriteViewBridge");
	}

	@Override
	public String getHTML() {
		return View.getHTMLFileContent("my_favorites_view.html");
	}

	@Override
	public void justAdded() {

		search = new Search();

		(new Thread() {
			public void run() {

				final ArrayList<ShortVideoData> results = StoredData.getAll();
				resultsViews.clear();


				Platform.runLater(new Runnable() {

					public void run() {
						empty("my_favorites_contener");

						show(results);
					}


				});

			}

		}).start();
	}

	public void show(ArrayList<ShortVideoData> results) {
		if (results.size() == 0) {
			doJS(".hideNoResult", "hide()");
		}
		else {
			doJS(".hideNoResult", "show()");
		}

		for (ShortVideoData data : results) {
			View view = new ShortVideoResultView(data);
			resultsViews.add(new ShortVideoResultView(data));
			append(view, "my_favorites_contener");
		}
	}


	public class MyFavoriteViewBridge extends Bridge {
	}
}
