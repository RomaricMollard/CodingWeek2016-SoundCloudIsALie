package core;


import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelContentDetails;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;


public class ChannelData {
	/*
	 * ChannelData serves to store all the informations concerning a single channel (similarly to VideoData for videos)
	 * It consists of a constructor and geters/seters.
	 */
	private String id;
	private ChannelSnippet snippet;
	private String thumbnail;
	private ChannelContentDetails contentDetails;
	private ChannelStatistics stats;


	public ChannelData(Channel channel) {
		id = channel.getId();
		snippet = channel.getSnippet();
		contentDetails = channel.getContentDetails();
		stats = channel.getStatistics();
		if (snippet.getThumbnails() != null && snippet.getThumbnails().getMedium() != null) {
			thumbnail = snippet.getThumbnails().getMedium().getUrl();
		}
		else {
			thumbnail = "";
		}
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public ChannelSnippet getSnippet() {
		return snippet;
	}


	public void setSnippet(ChannelSnippet snippet) {
		this.snippet = snippet;
	}


	public ChannelContentDetails getContentDetails() {
		return contentDetails;
	}


	public void setContentDetails(ChannelContentDetails contentDetails) {
		this.contentDetails = contentDetails;
	}


	public ChannelStatistics getStats() {
		return stats;
	}


	public void setStats(ChannelStatistics stats) {
		this.stats = stats;
	}


	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


}
