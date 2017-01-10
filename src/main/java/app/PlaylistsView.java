package app;


import javafx.scene.web.WebEngine;


public class PlaylistsView extends View {

	String filePath;


	public PlaylistsView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		filePath = "";
		createBridge(new PlaylistsViewBridge(), "PlaylistsViewBridge");
	}


	@Override
	public String getHTML() {
		return View.getHTMLFileContent("playlists_view.html");
	}

	@Override
	public void justAdded() {


		String[] musicCategoriesNames = { "Classique", "Jazz", "Pop", "Rock", "Rap", "Punk", "Reggae", "Disco", "Metal", "Blues" };
		String[] musicPicturesNames = { "classic", "jazz", "pop", "rock", "rap", "punk", "reggae", "disco", "metal", "blues" };
		String[] musicSearchTerms = { "orchestra classic music", "jazz", "pop music", "rock music", "rap francais", "punk",
				"reggae", "disco", "metal", "blues" };

		for (int i = 0; i < musicCategoriesNames.length; ++i) {
			append(new PlaylistItemView(jfxWebEngine, musicCategoriesNames[i], i, musicPicturesNames[i] + ".jpg", musicSearchTerms[i]), "playlists_music_contener");
		}

		String[] videosCategoriesNames = { "Jeux vidéo", "Films", "Cuisine", "Beauté", "Politique" };
		String[] videosPicturesNames = { "videogames", "films", "food", "beauty", "politique" };
		String[] videosSearchTerms = { "playthrough", "films", "cuisine", "beaute", "politique" };

		for (int i = 0; i < videosCategoriesNames.length; ++i) {
			append(new PlaylistItemView(jfxWebEngine, videosCategoriesNames[i], i, videosPicturesNames[i] + ".jpg", videosSearchTerms[i]), "playlists_tv_contener");
		}
	}


	public class PlaylistsViewBridge extends Bridge {
		public void setPlaylist(int playlistId) {

		}
	}


}
