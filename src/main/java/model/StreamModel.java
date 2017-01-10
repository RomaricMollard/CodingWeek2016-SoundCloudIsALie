package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import app.App;
import app.StreamView;
import core.Search;
import core.StoreData;
import core.StoredData;
import core.VideoData;
import javafx.application.Platform;


public class StreamModel {

	public boolean hasVideo = false;
	public boolean hasPlayer = false;
	public boolean isPlaying = false;

	public String currentVideoId;
	public VideoData currentVideo;


	/* Player lists */

	LinkedList<VideoData> upnextList = new LinkedList<VideoData>();
	LinkedList<VideoData> historyList = new LinkedList<VideoData>();
	LinkedList<VideoData> autonextList = new LinkedList<VideoData>();

	private StreamView player;
	public boolean channelMode;
	private String channelTerms;
	public String currentChannelName = "";


	public StreamModel() {
	}

	public void setPlayer(StreamView view) {
		player = view;
		hasPlayer = true;
	}

	/**
	 * Change current video
	 * 
	 * @param videoId
	 */
	public void setVideo(VideoData video) {
		synchronized (video) {
			currentVideoId = video.getId();
			currentVideo = video;
			hasVideo = true;

			player.myComments.init(this.currentVideo);
			StoreData.saveLastVideo(video.getId());


			player.myComments.init(this.currentVideo);
			StoreData.saveLastVideo(video.getId());

			HashMap<String, String> values = new HashMap<String, String>();

			values.put("channel_name", video.getChannelTitle());
			values.put("video_name", video.getTitle());
			if (currentChannelName.equals("")) {
				values.put("current_channel", "");
			}
			else {
				values.put("current_channel", "[En lecture : " + currentChannelName + "]");
			}

			int likeState = 0;
			if (video.getRating().equals("like")) {
				likeState = 1;
			}
			if (video.getRating().equals("dislike")) {
				likeState = -1;
			}


			player.doJS("setVideo('" + video.getId() + "','" + video.getThumbnail().getMedium().getUrl() + "')");
			player.doJS("likeState(" + likeState + ")");

			if (StoredData.getFav(video)) {
				player.doJS(".favorite_button", "hide()");
				player.doJS(".favoriteadded_button", "show()");
			}
			else {
				player.doJS(".favorite_button", "show()");
				player.doJS(".favoriteadded_button", "hide()");
			}

			play();

			player.doTemplate(values);

			historyList.remove(video);
			historyList.add(video);
		}
		fillAutoNext();

		updateList();

	}

	public void setVideo(String videoId) {
		setVideo(new Search().get(videoId));
	}

	/**
	 * Remove video
	 */
	public void unsetVideo() {

		currentVideoId = null;
		currentVideo = null;
		hasVideo = false;

		HashMap<String, String> values = new HashMap<String, String>();

		values.put("channel_name", "-");
		values.put("video_name", "-");
		player.doJS("setVideo('','')");
		player.doTemplate(values);

	}

	/**
	 * Play and Pause video
	 */
	public void play() {

		if (hasVideo == false) {
			fillAutoNext();
			videoEnd();
		}

		isPlaying = true;
		player.doJS("wantplay = true;");
	}

	public void pause() {
		isPlaying = false;
		player.doJS("wantplay = false;");
	}





	/**
	 * Handle End of video and autoplay next things in list
	 */
	public void videoEnd() {
		isPlaying = false;
		unsetVideo();

		if (!upnextList.isEmpty()) {

			setVideo(upnextList.get(0));
			upnextList.remove(0);

		}
		else if (!autonextList.isEmpty()) {

			setVideo(autonextList.get(0));
			autonextList.remove(0);

			fillAutoNext();

		}

		updateList();

	}

	/**
	 * Fill autonext for the current video
	 */
	public void fillAutoNext() {

		if (this.channelMode) {
			autonextList.clear();
			fillPlaylist(this.channelTerms);
			return;
		}

		(new Thread() {

			public void run() {


				String videoId = "music"; //#TODO change default value
				if (hasVideo) {
					videoId = currentVideoId;
				}
				else if (!historyList.isEmpty()) {
					videoId = historyList.getLast().getId();
				}

				final ArrayList<VideoData> searchResults = (new Search()).getRelated(videoId/*,50-autonextList.size()*/);

				Platform.runLater(new Runnable() {

					public void run() {
						synchronized (autonextList) {
							autonextList.clear();
							autonextList.addAll(searchResults);
							@SuppressWarnings("unchecked")
							LinkedList<VideoData> auto = (LinkedList<VideoData>) autonextList.clone();
							for (VideoData v : auto) {
								synchronized (v) {
									if (v.getId() == App.player.currentVideoId) {
										autonextList.remove(v);
									}
									int index2 = 0;
									for (; index2 < 10; index2++) {
										if (historyList.size() > 0)
											if (historyList.get(Math.max(0, historyList.size() - index2 - 1)).getId().equals(v.getId())) {
												autonextList.remove(v);
											}
									}
								}

							}

							updateList();
						}
					}

				});


			}

		}).start();

	}


	public void fillPlaylist(final String playlist) {
		(new Thread() {

			public void run() {

				final ArrayList<VideoData> searchResults = (new Search()).research(playlist);

				Platform.runLater(new Runnable() {

					public void run() {
						synchronized (autonextList) {
							if (!(autonextList.size() < 10)) {
								return;
							}
							autonextList.addAll(searchResults);
							@SuppressWarnings("unchecked")
							LinkedList<VideoData> auto = (LinkedList<VideoData>) autonextList.clone();
							for (VideoData v : auto) {
								synchronized (v) {
									int index2 = 0;
									if (v.getId() == App.player.currentVideoId) {
										autonextList.remove(v);
									}
									for (; index2 < 10; index2++) {
										if (historyList.size() > 0)
											if (historyList.get(Math.max(0, historyList.size() - index2 - 1)).getId().equals(v.getId())) {
												autonextList.remove(v);
											}
									}
								}

							}
						}
						updateList();

					}

				});


			}

		}).start();
	}

	public void resetAutoNext() {

		autonextList.clear();
		fillAutoNext();

	}


	private void updateList() {
		player.currentList.updateList(historyList, upnextList, autonextList);
	}

	/**
	 * Play next / previous
	 */
	public void next() {
		videoEnd(); //Force end and play next
	}

	public void previous() {
		synchronized (autonextList) {
			@SuppressWarnings("unchecked")
			LinkedList<VideoData> auto = (LinkedList<VideoData>) autonextList.clone();
			for (VideoData video : auto) {
				if (video.getId() == App.player.currentVideoId) {
					autonextList.remove(video);
				}
			}
		}

		if (historyList.size() == 1) {
			return;
		}

		if (hasVideo) { //Mettre la video courante dans le up next
			VideoData v = historyList.getLast();
			historyList.removeLast();
			upnextList.addFirst(v);
		}

		VideoData v = historyList.getLast();
		historyList.removeLast();
		setVideo(v);
	}

	/**
	 * Manage upnext list
	 * 
	 * @param vId
	 */
	public void addUpNext(String vId) {
		upnextList.add(new Search().get(vId));
		updateList();
	}

	public void addUpNextNow(String vId) {
		upnextList.addFirst(new Search().get(vId));
		updateList();
	}

	public void removeUpNext(String vId) {
		for (VideoData v : upnextList) {
			if (v.getId().equals(vId)) {
				upnextList.remove(v);
			}
			break;
		}
		updateList();
	}

	public void channel(String searchterms, String channelName) {

		this.channelMode = true;
		this.channelTerms = searchterms;
		this.currentChannelName = channelName;
		this.upnextList.clear();
		this.autonextList.clear();
		this.fillAutoNext();
		this.next();
	}


}
