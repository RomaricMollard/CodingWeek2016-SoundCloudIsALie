package core;


import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;


/*
 * Contents of SearchResultSnippet :
 * 	String   			- ChannelId				explicit
 * 	String   			- ChannelTitle 			Channel that uploaded the content searched
 *  String   			- Title					explicit
 *  String   			- Description
 *  ThumbnailsDetails 	- Thumbnails			A map that associate thumbnail name (key) to other informations
 *  String   			- LiveBroadcastContent 	Indicates if there is an upcoming/active live broadcast content
 *  DateTime 			- PublishedAt			Get time of publication for the content searched
 *  String				- Rating				Rating of the video (by the User)
 */
public class VideoData {
	private String channelID;
	private String channelTitle;
	private String Description;
	private String LiveBroadcastContent;
	private DateTime PublishedAt;
	private String title;
	private ThumbnailDetails thumbnail;
	private VideoSnippet basics;
	private String id;
	private VideoStatistics stats;
	private String duration;
	private String rating;
	private int note;
	private boolean favorite = false;


	public VideoData() {

	}

	public VideoData(String id, VideoSnippet basics, String duration, VideoStatistics stats) {
		this.basics = basics;
		this.id = id;
		this.duration = duration;
		this.stats = stats;
		this.extractSnippet();
	}


	public void extractSnippet() {
		if (this.basics != null) {
			this.channelID = basics.getChannelId();
			this.channelTitle = basics.getChannelTitle();
			this.Description = basics.getDescription();
			this.LiveBroadcastContent = basics.getLiveBroadcastContent();
			this.PublishedAt = basics.getPublishedAt();
			this.title = basics.getTitle();
			this.thumbnail = basics.getThumbnails();
		}
		else {
			System.out.println("Warning : No snippet associated with this search result");
		}
	}

	public String getChannelID() {
		return channelID;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public String getDescription() {
		return Description;
	}

	public String getLiveBroadcastContent() {
		return LiveBroadcastContent;
	}

	public DateTime getPublishedAt() {
		return PublishedAt;
	}

	public String getTitle() {
		return title;
	}

	public ThumbnailDetails getThumbnail() {
		return thumbnail;
	}

	public VideoSnippet getBasics() {
		return basics;
	}

	public String getId() {
		return id;
	}

	public VideoStatistics getStats() {
		return stats;
	}

	public String getDuration() {
		return duration;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	@Override
	public boolean equals(Object data) {
		if (data != null)
			return this.getId().equals(((VideoData) data).getId());
		else
			return false;
	}
}
