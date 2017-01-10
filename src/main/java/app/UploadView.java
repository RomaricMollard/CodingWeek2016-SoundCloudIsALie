package app;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import core.Search;
import core.UploadVideo;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;


public class UploadView extends View {

	ArrayList<VideoResultView> resultsViews;
	Search search;
	String term;
	String filePath;
	int progress;


	public UploadView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		this.resultsViews = new ArrayList<VideoResultView>();
		createBridge(new UploadViewBridge(), "UploadViewBridge");
		filePath = "";
		progress = 0;
	}

	@Override
	public String getHTML() {
		return View.getHTMLFileContent("upload_view.html");
	}

	public void setProgress(final int progress, UploadVideo upload) {

		if (App.uploadManager.getUploads().size() > 0 && App.uploadManager.getUploads().indexOf(upload) == App.uploadManager.getUploads().size() - 1) {
			this.progress = progress;
			System.out.println(this.progress);

			Platform.runLater(new Runnable() {
				public void run() {
					doJS("#progressbar", "css('width', '" + progress + "%');");
					if (progress != 100)
						showMessage("La mise en ligne a commencé (" + progress + "%) (plus de détails dans le gestionnaire de mise en ligne)");
				}
			});
		}
	}

	public void setStatus(UploadStatus status, UploadVideo upload) {

		if (App.uploadManager.getUploads().size() > 0 && App.uploadManager.getUploads().indexOf(upload) == App.uploadManager.getUploads().size() - 1) {

			if (status == UploadStatus.NotStarted) {
				showError("La mise en ligne n'a pas pu démarrer (vérifiez que vous avez bien un compte Youtube, et que vous avez une connexion à internet)");
			}
			else if (status == UploadStatus.InitializationStarted) {
				showMessage("Initialisation de la mise en ligne...");
			}
			else if (status == UploadStatus.InitializationCompleted) {
				clearInputs();
				showMessage("La mise en ligne a commencé (" + progress + "%) (plus de détails dans le gestionnaire de mise en ligne)");
			}
			else if (status == UploadStatus.Completed) {
				showMessage("Mise en ligne terminée");
			}
			else if (status == UploadStatus.FileNotFound) {
				showError("Problème avec le fichier choisi, veuillez rééssayer");
			}
		}
	}

	public void clearInputs() {

		Platform.runLater(new Runnable() {
			public void run() {
				doJS("#title", "val('');");
				doJS("#description", "val('');");
				doJS("#tags", "val('');");
				filePath = "";
			}
		});
	}

	private void showError(final String string) {
		Platform.runLater(new Runnable() {
			public void run() {
				add(new UploadErrorView(jfxWebEngine, string, true), "error_message");

				System.out.println("#1");
			}
		});
	}

	private void showMessage(final String string) {
		Platform.runLater(new Runnable() {
			public void run() {
				add(new UploadErrorView(jfxWebEngine, string, false), "error_message");
				System.out.println("#2");
			}
		});
	}


	public class UploadViewBridge extends Bridge {
		public void selectFile() {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Selectionnez une vidéo à uploader");
			File selectedFile = chooser.showOpenDialog(app.App.mainWindow.primaryStage);
			filePath = selectedFile.getAbsolutePath();
		}

		public void doUpload(final String title, final String description, final String tags, final String status) {

			System.out.println(title + " --- " + description + " --- " + tags);

			(new Thread() {
				public void run() {

					if (title.equals("")) {
						showError("Le titre ne doit pas être vide");
					}
					else if (description.equals("")) {
						showError("La description ne doit pas être vide");
					}
					else if (filePath.equals("")) {
						showError("Un fichier vidéo doit être sélectionné");
					}
					else {

						UploadVideo upload = new UploadVideo(filePath, status, title, description, new ArrayList<String>(Arrays.asList(tags.split(","))));
						App.uploadManager.addUpload(upload);
						upload.setView(UploadView.this);
						upload.upload();
					}
				}
			}).start();
		}
	}
}
