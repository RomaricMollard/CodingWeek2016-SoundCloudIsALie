package core;


import java.util.ArrayList;

import com.google.api.services.youtube.model.VideoSnippet;


public class VideoToUpload {
	private VideoSnippet snippet = new VideoSnippet();
	private String link;
	private String status;
	private String description;
	private String title;
	private ArrayList<String> tags = new ArrayList<String>();


	public VideoToUpload(String link, String status, String title, String description, ArrayList<String> tags) {
		this.link = link;
		this.description = description;
		this.status = status;
		this.tags = tags;
		this.title = title;
		updateSnippet();
	}

	public VideoToUpload(String link, String status, VideoSnippet snippet) {
		this.link = link;
		this.snippet = snippet;
	}

	public VideoToUpload(String link, String status, String title, String description) {
		this.link = link;
		this.status = status;
		this.title = title;
		this.description = description;
		updateSnippet();
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public void updateSnippet() {
		snippet.setTitle(title);
		snippet.setDescription(description);
		snippet.setTags(tags);
	}

	public VideoSnippet getSnippet() {
		return snippet;
	}

	public void setSnippet(VideoSnippet snippet) {
		this.snippet = snippet;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
}
