package core;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import app.App;
import app.UploadStatus;
import app.UploadView;


public class UploadVideo {
	public VideoToUpload videoToUpload;
	private static final String VIDEO_FILE_FORMAT = "video/*";
	UploadView view;
	String fileName;



	public UploadVideo(String link, String status, String title, String description, ArrayList<String> tags) {
		videoToUpload = new VideoToUpload(link, status, title, description, tags);
	}

	public UploadVideo(String link, String status, VideoSnippet snippet) {
		videoToUpload = new VideoToUpload(link, status, snippet);
	}

	public UploadVideo(String link, String status, String title, String description) {
		videoToUpload = new VideoToUpload(link, status, title, description);
	}

	public UploadVideo(VideoToUpload videoToUpload) {
		this.videoToUpload = videoToUpload;
	}

	public void setView(UploadView view) {
		this.view = view;
	}

	public void upload() {
		try {


			final File file = new File(videoToUpload.getLink());
			if (!file.exists()) {
				System.out.println("file do not exists");
				view.setStatus(UploadStatus.FileNotFound, UploadVideo.this);
				return;
			}
			System.out.println("Uploading: " + videoToUpload.getLink());

			// Add extra information to the video before uploading.
			Video videoObjectDefiningMetadata = new Video();


			// Visibility : Supporting settings are "public", "unlisted" and "private."
			VideoStatus status = new VideoStatus();
			status.setPrivacyStatus(videoToUpload.getStatus());
			videoObjectDefiningMetadata.setStatus(status);

			// Set the metadata for the video (title, description, tags)
			videoObjectDefiningMetadata.setSnippet(videoToUpload.getSnippet());

			InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, new FileInputStream(file));

			// Insert the video. The command sends three arguments. The first
			// specifies which information the API request is setting and which
			// information the API response should return. The second argument
			// is the video resource that contains metadata about the new video.
			// The third argument is the actual video content.
			YouTube.Videos.Insert videoInsert = YT.api.videos()
					.insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

			// Set the upload type and add an event listener.
			MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

			// Indicate whether direct media upload is enabled. A value of
			// "True" indicates that direct media upload is enabled and that
			// the entire media content will be uploaded in a single request.
			// A value of "False," which is the default, indicates that the
			// request will use the resumable media upload protocol, which
			// supports the ability to resume an upload operation after a
			// network interruption or other transmission failure, saving
			// time and bandwidth in the event of network failures.
			uploader.setDirectUploadEnabled(false);

			MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
				public void progressChanged(MediaHttpUploader uploader) throws IOException {
					switch (uploader.getUploadState()) {
						case INITIATION_STARTED:
							if (view == null) {
								System.out.println("Initiation Started");
							}
							else {
								view.setStatus(UploadStatus.InitializationStarted, UploadVideo.this);
								App.uploadManager.updateStatus(UploadVideo.this, UploadStatus.InitializationStarted);
							}
							break;
						case INITIATION_COMPLETE:
							if (view == null) {
								System.out.println("Initiation Completed");
							}
							else {
								view.setStatus(UploadStatus.InitializationCompleted, UploadVideo.this);
								App.uploadManager.updateStatus(UploadVideo.this, UploadStatus.InitializationCompleted);
							}
							break;
						case MEDIA_IN_PROGRESS:
							System.out.println("Upload in progress");
							if (view == null) {
								System.out.println("Upload percentage: " + (int) ((float) uploader.getNumBytesUploaded() * 100.f / (float) file.length()) + "%");
							}
							else {
								view.setProgress((int) ((float) uploader.getNumBytesUploaded() * 100.f / (float) file.length()), UploadVideo.this);
								view.setStatus(UploadStatus.InProgress, UploadVideo.this);
								App.uploadManager.updateProgress(UploadVideo.this, (int) ((float) uploader.getNumBytesUploaded() * 100.f / (float) file.length()));
								App.uploadManager.updateStatus(UploadVideo.this, UploadStatus.InProgress);
							}
							break;
						case MEDIA_COMPLETE:
							if (view == null) {
								System.out.println("Upload Completed!");
							}
							else {
								view.setStatus(UploadStatus.Completed, UploadVideo.this);
								App.uploadManager.updateStatus(UploadVideo.this, UploadStatus.Completed);
								view.setProgress(100, UploadVideo.this);
								App.uploadManager.updateProgress(UploadVideo.this, 100);
							}
							view.setProgress(100, UploadVideo.this);
							break;
						case NOT_STARTED:
							if (view == null) {
								System.out.println("Upload Not Started!");
							}
							else {
								view.setStatus(UploadStatus.NotStarted, UploadVideo.this);
								App.uploadManager.updateStatus(UploadVideo.this, UploadStatus.NotStarted);
							}
							break;
					}
				}
			};
			uploader.setProgressListener(progressListener);

			// Call the API and upload the video.
			try {
				Video returnedVideo = videoInsert.execute();
				// Print data about the newly inserted video from the API response.
				System.out.println("\n================== Returned Video ==================\n");
				System.out.println("  - Id: " + returnedVideo.getId());
				System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
				System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
				System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
				System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
			}
			catch (Exception e) {
				view.setStatus(UploadStatus.NotStarted, UploadVideo.this);
			}

		}
		catch (GoogleJsonResponseException e) {
			System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		catch (Throwable t) {
			System.err.println("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
	}
}
