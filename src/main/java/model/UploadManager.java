package model;


import java.util.ArrayList;

import app.CurrentList;
import app.UploadStatus;
import core.UploadVideo;


public class UploadManager {
	private ArrayList<UploadVideo> uploads;
	private ArrayList<UploadStatus> status;
	private ArrayList<Integer> progress;

	private CurrentList currentListView;


	public UploadManager() {
		uploads = new ArrayList<UploadVideo>();
		status = new ArrayList<UploadStatus>();
		progress = new ArrayList<Integer>();
	}


	/*
	 * returns the list of UploadVideo which are currently working
	*/
	public ArrayList<UploadVideo> getUploads() {
		return uploads;
	}

	/*
	 * returns the list of UploadStatus corresponding to the UploadVideo objects
	*/
	public ArrayList<UploadStatus> getStatus() {
		return status;
	}

	/*
	 * returns the list of progress (percentage of the operation) corresponding to the UploadVideo objects
	*/
	public ArrayList<Integer> getProgress() {
		return progress;
	}

	/*
	 * set the view which should be notified of the updates
	*/
	public void setView(CurrentList currentListView) {
		this.currentListView = currentListView;
	}

	/*
	 * add a started UploadStatus in the manager
	*/
	public void addUpload(UploadVideo upload) {
		uploads.add(upload);
		status.add(UploadStatus.InitializationStarted);
		progress.add(0);
	}

	/*
	 * remove a started UploadStatus from the manager
	*/
	public void removeUpload(int uploadId) {
		if (progress.get(uploadId) == 100) {
			uploads.remove(uploadId);
			status.remove(uploadId);
			progress.remove(uploadId);
			currentListView.updateUploadList();
		}
	}

	/*
	 * update the status of an upload
	*/
	public void updateStatus(UploadVideo upload, UploadStatus status) {
		this.status.set(uploads.indexOf(upload), status);
		if (currentListView != null) {
			currentListView.updateUploadList();
		}
	}

	/*
	 * update the progress (percentage) of an upload
	*/
	public void updateProgress(UploadVideo upload, int progress) {
		this.progress.set(uploads.indexOf(upload), progress);
		if (currentListView != null) {

			currentListView.updateUploadList();
		}
	}
}
