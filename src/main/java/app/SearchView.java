package app;


import java.util.ArrayList;

import core.Search;
import core.VideoData;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;


public class SearchView extends View {

	ArrayList<VideoResultView> resultsViews;
	Search search;
	String term;
	SearchView me = this;


	public SearchView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		this.resultsViews = new ArrayList<VideoResultView>();
		createBridge(new SearchViewBridge(), "SearchViewBridge");
	}

	@Override
	public void justAdded() {
		doResearch("musique music sound album");
	}

	@Override
	public String getHTML() {
		return View.getHTMLFileContent("search_view.html");
	}

	public void doResearch(final String text) {

		empty("search_contener");
		doJS(".search.loading","show()");

		search = new Search();
		term = text;

		(new Thread() {
			public void run() {

				final ArrayList<VideoData> results = search.research(text);
				resultsViews.clear();


				Platform.runLater(new Runnable() {

					public void run() {

						doJS(".search.loading","hide()");
						show(results);
						
					}


				});

			}

		}).start();

	}

	public void show(ArrayList<VideoData> results) {
		if (results.size() == 0) {
			doJS(".hideNoResult", "hide()");
		}
		else {
			doJS(".hideNoResult", "show()");
		}

		for (VideoData data : results) {
			View view = new VideoResultView(data);
			resultsViews.add(new VideoResultView(data));
			append(view, "search_contener");
		}
	}


	public class SearchViewBridge extends Bridge {


		public void doResearch(final String text) {

			me.doResearch(text);

		}

		public void nextPage() {

			if (search == null) {
				return;
			}

			(new Thread() {
				public void run() {


					final ArrayList<VideoData> results = search.research(term);

					Platform.runLater(new Runnable() {

						public void run() {
							show(results);
							
						}

					});

				}

			}).start();

		}

	}
}
