package core;


public class ShortVideoData {
	public String id;
	public String title;
	public String description;
	public String thumbnailURL;
	public int note;
	public boolean fav;


	public ShortVideoData(String id, String title, String description, String thumbnailURL, int note, boolean fav) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.thumbnailURL = thumbnailURL;
		this.note = note;
		this.fav = fav;
	}

	public ShortVideoData() {
	};

	public ShortVideoData(String id, String title, String description, String thumbnailURL, String note, String fav) {
		System.out.println("je suis jackson et je suis débile");
	}

	public ShortVideoData(VideoData vdata) {
		id = vdata.getId();
		title = vdata.getTitle();
		description = vdata.getDescription();
		thumbnailURL = vdata.getThumbnail().getMedium().getUrl();
	}


	public String getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public String getThumbnailURL() {
		return thumbnailURL;
	}


	public int getNote() {
		return note;
	}


	public void setNote(int note) {
		this.note = note;
	}


	public boolean isFav() {
		return fav;
	}


	public void setFav(boolean fav) {
		this.fav = fav;
	}
}
