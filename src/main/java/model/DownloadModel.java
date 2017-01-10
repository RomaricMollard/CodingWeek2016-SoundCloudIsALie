package model;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;

import app.View;
import core.DownloadableElement;
import core.ParametersFunction;
import core.VideoData;
import javafx.stage.DirectoryChooser;


public final class DownloadModel {
	/*
	 * This classe is used to download a file, it is final as it does not need any instanciation.
	 * The main function is download, which require a VideoData and the path to a directory (where the downloaded file will be stocked
	 */
	public static HashMap<String, DownloadableElement> downloadList = new HashMap<String, DownloadableElement>();
	public static View viewToUpdate;


	public static void download(final VideoData video) {
		String path = ParametersFunction.loaddlPath();
		if (path == "")
			path = "./video/";

		path += "/";

		//Check if the file is already downloading, if this is the case then we don't download it twice
		if (downloadList.containsKey(video.getId())) {
			if (downloadList.get(video.getId()).stop.get()) {
				downloadList.remove(video.getId());
			}
			else {
				return;
			}
		}

		final File file = new File(path);
		file.mkdirs();

		(new Thread() {
			public void run() {
				try {

					final AtomicBoolean stop = new AtomicBoolean(false);

					URL url = new URL("https://www.youtube.com/watch?v=" + video.getId());
					VGetParser user = VGet.parser(url);
					VideoInfo videoinfo = user.info(url);

					VGet v = new VGet(videoinfo, file);

					DownloadableElement current_element = new DownloadableElement(video, stop, videoinfo);
					downloadList.put(video.getId(), current_element);

					VGetStatus notify = new VGetStatus(videoinfo, current_element, stop, this);

					v.download(user, stop, notify);

					downloadList.remove(video.getId());

				}
				catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

			// This methods is used to kill the thread (when asked to stop the download/quit the application)
			@SuppressWarnings("deprecation")
			public void stopit() {
				if (viewToUpdate != null)
					viewToUpdate.update();
				this.stop();
			}


			/* Get percentage */
			class VGetStatus implements Runnable {
				VideoInfo videoinfo;
				DownloadableElement element;
				private AtomicBoolean stop;
				@SuppressWarnings("unused")
				private Thread thread;


				public VGetStatus(VideoInfo i, DownloadableElement element, AtomicBoolean stop, Thread thread) {
					this.videoinfo = i;
					this.element = element;
					this.stop = stop;
					this.thread = thread;
				}

				public void run() {

					if (stop.get()) {
						stopit();
					}

					List<VideoFileInfo> dinfoList = videoinfo.getInfo();

					switch (videoinfo.getState()) {
						case EXTRACTING:
						case EXTRACTING_DONE:
							element.currentState = 0;
							break;
						case DONE:
							element.currentState = 2; // >1 ok
							downloadList.remove(element.video.getId());
							break;
						case DOWNLOADING:
							element.currentState = dinfoList.get(0).getCount() / (float) dinfoList.get(0).getLength();
							break;
						default:
							element.currentState = -1;
							downloadList.remove(element.video.getId());
							break;
					}

					if (viewToUpdate != null)
						viewToUpdate.update();

				}
			}

		}).start();
	}

	//If we do not specify a path, use a DirectoryChooser to ask the user the path (with the user's default folder viewer)
	//Currently not used, as we set a specific file where we download all of our videos.
	public static void download(String id) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Select a download path");
		File selectedDirectory = chooser.showDialog(app.App.mainWindow.primaryStage);
		download(id, selectedDirectory);
	}

	public static void download(String id, File directory) {
		try {
			VGet v = new VGet(new URL("http://www.youtube.com/watch?v=" + id), directory);
			v.download();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}


	//Format a long number to a String (containing donwload speed)
	//Not used currently
	public static String formatSpeed(long s) {
		if (s > 0.1 * 1024 * 1024 * 1024) {
			float f = s / 1024f / 1024f / 1024f;
			return String.format("%.1f GB/s", f);
		}
		else if (s > 0.1 * 1024 * 1024) {
			float f = s / 1024f / 1024f;
			return String.format("%.1f MB/s", f);
		}
		else {
			float f = s / 1024f;
			return String.format("%.1f kb/s", f);
		}
	}

	//Stops the download of a specific video
	public static void stopDownload(String vId) {
		if (downloadList.containsKey(vId)) {
			downloadList.get(vId).stopDownload();
		}
	}

	//Stop the download of all videos (called when we exit the application)
	public static void stopAll() {
		for (Entry<String, DownloadableElement> video : downloadList.entrySet()) {
			stopDownload(video.getKey());
		}
		return;
	}

}
