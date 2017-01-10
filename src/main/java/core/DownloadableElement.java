package core;


import java.util.concurrent.atomic.AtomicBoolean;

import com.github.axet.vget.info.VideoInfo;


public class DownloadableElement {


	public VideoData video;
	public double currentState;

	public AtomicBoolean stop;
	private VideoInfo videoInfo;


	public DownloadableElement(VideoData video, AtomicBoolean stop, VideoInfo videoinfo) {
		this.video = video;
		this.stop = stop;
		this.videoInfo = videoinfo;
	}


	public void showState() {
		System.out.println(video.getId() + " = " + currentState);
	}

	public void stopDownload() {
		System.out.println("stop " + video.getId());
		synchronized (stop) {
			this.stop.set(true);
			this.stop.notifyAll();
		}
		for (int i = 0; i < videoInfo.getInfo().size(); i++) {
			videoInfo.getInfo().get(i).targetFile.delete();
		}
	}

}
