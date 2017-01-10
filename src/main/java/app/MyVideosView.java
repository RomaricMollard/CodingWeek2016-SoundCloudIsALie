package app;


import java.util.ArrayList;

import core.Search;
import core.VideoData;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;


public class MyVideosView extends View {

	ArrayList<VideoResultView> resultsViews;
	Search search;


	public MyVideosView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		this.resultsViews = new ArrayList<VideoResultView>();
		createBridge(new MyVideosViewBridge(), "MyVideosViewBridge");
	}

	@Override
	public String getHTML() {
		return View.getHTMLFileContent("my_videos_view.html");
	}

	@Override
	public void justAdded() {

		search = new Search();

		(new Thread() {
			public void run() {

				final ArrayList<VideoData> results = search.researchFromChannel(App.myChannel.getId());
				resultsViews.clear();


				Platform.runLater(new Runnable() {

					public void run() {
						empty("my_videos_contener");

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
			append(view, "my_videos_contener");
		}
	}


	public class MyVideosViewBridge extends Bridge {
		public void nextPage() {
			if (search == null) {
				return;
			}

			(new Thread() {
				public void run() {


					final ArrayList<VideoData> results = search.researchFromChannel(App.myChannel.getId());

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
