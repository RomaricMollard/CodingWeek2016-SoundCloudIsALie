package app;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import core.DownloadableElement;
import core.VideoData;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import model.DownloadModel;
import utils.Utils;


public class CurrentList extends View {

	String elementTemplate;
	String buttonElementTemplate;
	String downloadElementTemplate;
	String uploadElementTemplate;
	HashMap<String, View> list;


	public CurrentList(WebEngine jfxWebEngine) {

		this.jfxWebEngine = jfxWebEngine;

		elementTemplate = View.getHTMLFileContent("listElement.html");
		buttonElementTemplate = View.getHTMLFileContent("listElementButton.html");
		downloadElementTemplate = View.getHTMLFileContent("listDownloadElementTemplate.html");
		uploadElementTemplate = View.getHTMLFileContent("listUploadElementTemplate.html");

		App.uploadManager.setView(this);

	}

	@Override
	public String getHTML() {
		return View.getHTMLFileContent("currentList.html");
	}

	public void justAdded() {
		DownloadModel.viewToUpdate = this;
	}

	public void updateList(LinkedList<VideoData> historyList, LinkedList<VideoData> upnextList, LinkedList<VideoData> autonextList) {

		doJS("#historyContener", "html('')");
		int index = 0;
		for (; index < 100; index++) {
			if (historyList.size() - 1 - index >= 0) {
				VideoData video = historyList.get(historyList.size() - 1 - index);
				this.append(new SimpleView(elementTemplate, Utils.getMap(video)), "historyContener");
			}
			else {
				break;
			}
		}

		doJS("#upNext", "html('')");
		for (VideoData video : upnextList) {
			this.append(new SimpleView(buttonElementTemplate, Utils.getMap(video)), "upNext");
		}

	}

	@Override
	public void update() {
		updateDownloadList();
		updateUploadList();
	}



	private HashMap<String, DownloadableElement> currentVisible = new HashMap<String, DownloadableElement>();


	public void updateDownloadList() {

		if (DownloadModel.downloadList.size() == 0) {
			return;
		}

		Platform.runLater(new Runnable() {

			public void run() {

				synchronized (currentVisible) {

					for (Entry<String, DownloadableElement> video : DownloadModel.downloadList.entrySet()) {
						if (!video.getValue().stop.get()) {
							if (currentVisible.containsKey(video.getKey())) {
								doJS(".videoDownloadId" + video.getKey(), "find('.list_download_status').css({'width':'" + Math.round(video.getValue().currentState * 100) + "%'})");
								doJS(".videoDownloadId" + video.getKey(), "find('.list_download_status_text').html('" + Math.round(video.getValue().currentState * 100) + "')");
							}
							else {
								currentVisible.put(video.getKey(), video.getValue());
								append(new SimpleView(downloadElementTemplate, Utils.getMap(video.getValue().video)), "downloadList");
							}
						}
					}

					for (Entry<String, DownloadableElement> video : currentVisible.entrySet()) {
						if (!DownloadModel.downloadList.containsKey(video.getKey()) || DownloadModel.downloadList.get(video.getKey()).stop.get()) {
							currentVisible.remove(video.getKey());
							doJS(".videoDownloadId" + video.getKey(), "remove()");
						}

					}

				}


			}
		});


	}

	public void updateUploadList() {

		Platform.runLater(new Runnable() {
			public void run() {

				synchronized (App.uploadManager) {
					if (App.uploadManager.getUploads().size() == 0) {
						empty("uploadList");
						return;
					}

					for (Integer i = 0; i < App.uploadManager.getUploads().size(); ++i) {
						String title = App.uploadManager.getUploads().get(i).videoToUpload.getTitle();
						Integer progress = App.uploadManager.getProgress().get(i);

						HashMap<String, String> info = new HashMap<String, String>();
						info.put("video_title", title);
						info.put("upload_state", progress.toString());
						info.put("video_id", i.toString());


						if (i == 0) {
							add(new SimpleView(uploadElementTemplate, info), "uploadList");
						}
						else {
							append(new SimpleView(uploadElementTemplate, info), "uploadList");
						}
					}
				}
			}
		});
	}

}
