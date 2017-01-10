package app;


import java.util.HashMap;

import core.ShortVideoData;


public class ShortVideoResultView extends View {
	ShortVideoData videoData;


	public ShortVideoResultView(ShortVideoData videoData) {
		this.videoData = videoData;
	}


	@Override
	public String getHTML() {

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("video_name", videoData.getTitle());
		values.put("channel_name", videoData.getTitle());
		values.put("video_desc", videoData.getDescription());
		values.put("video_cover", "background-image: url('" + videoData.getThumbnailURL() + "');");
		values.put("video_id", videoData.getId());

		return doTemplate(View.getHTMLFileContent("video_result.html"), values);
	}
}
