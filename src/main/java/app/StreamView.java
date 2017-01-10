package app;


import core.Rate;
import core.StoreData;
import core.StoredData;
import javafx.scene.web.WebEngine;
import model.DownloadModel;


public class StreamView extends View {

	String currentTemplate;
	public CurrentList currentList;
	public CommentsView myComments;


	public StreamView(WebEngine jfxWebEngine, CommentsView comments) {

		this.jfxWebEngine = jfxWebEngine;
		this.myComments = comments;

		createBridge(new StreamViewBridge(), "StreamViewBridge");

		currentList = new CurrentList(jfxWebEngine);

		App.player.setPlayer(this);

	}

	@Override
	public void justAdded() {
		add(currentList, "stream_history");
		String lastv = StoreData.getLastVideo();
		if (lastv != "") {
			App.player.setVideo(lastv); // Autoplay last played music
		}
	}

	@Override
	public String getHTML() {
		currentTemplate = View.getHTMLFileContent("stream.html");
		return currentTemplate;
	}


	public class StreamViewBridge extends Bridge {

		public void setVideo(String videoId) {
			App.player.channelMode = false;
			App.player.currentChannelName = "";
			App.player.setVideo(videoId);
		}

		public void play() {
			App.player.play();
		}

		public void pause() {
			App.player.pause();
		}

		public void videoEnd() {
			App.player.videoEnd();
		}

		public void previous() {
			App.player.previous();
		}

		public void next() {
			App.player.next();
		}

		public void addUpNext(String vId) {
			App.player.addUpNext(vId);
		}

		public void addUpNextNow(String vId) {
			App.player.addUpNextNow(vId);
		}

		/*
				public void addUpNextCurrent() {
					if (App.player.hasVideo) {
						App.player.addUpNext(App.player.currentVideoId);
					}
				}
		
				public void addUpNextNowCurrent() {
					if (App.player.hasVideo) {
						App.player.addUpNextNow(App.player.currentVideoId);
					}
				}
		*/
		public void removeUpNext(String vId) {
			App.player.removeUpNext(vId);
		}

		public void downloadCurrent() {
			DownloadModel.download(App.player.currentVideo);
		}

		public void stopDownload(String vId) {
			DownloadModel.stopDownload(vId);
		}

		public void removeUpload(int vId) {
			App.uploadManager.removeUpload(vId);
		}

		public void like() {
			if (App.player.currentVideo.getRating().equals("like")) {
				Rate.rate(App.player.currentVideoId, "none");
				App.player.currentVideo.setRating("none");
				doJS("likeState(0);");
			}
			else {
				Rate.rate(App.player.currentVideoId, "like");
				App.player.currentVideo.setRating("like");
				doJS("likeState(1);");
			}
		}

		public void dislike() {
			if (App.player.currentVideo.getRating().equals("dislike")) {
				Rate.rate(App.player.currentVideoId, "none");
				App.player.currentVideo.setRating("none");
				doJS("likeState(0);");
			}
			else {
				Rate.rate(App.player.currentVideoId, "dislike");
				App.player.currentVideo.setRating("dislike");
				doJS("likeState(-1);");
			}
		}

		public void addFavorite() {
			StoredData.fav(App.player.currentVideo, true);
			doJS(".favorite_button", "hide()");
			doJS(".favoriteadded_button", "show()");
		}

		public void delFavorite() {
			StoredData.fav(App.player.currentVideo, false);
			doJS(".favorite_button", "show()");
			doJS(".favoriteadded_button", "hide()");
		}

		public void setChannel(String searchterms, String channelName) {
			App.player.channel(searchterms, channelName);
		}

	}
}
