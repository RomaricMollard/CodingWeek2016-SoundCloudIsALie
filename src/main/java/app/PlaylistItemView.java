package app;


import java.util.HashMap;

import javafx.scene.web.WebEngine;


public class PlaylistItemView extends View {

	Integer playlistId;
	String playlistName;
	String coverFile;
	String search_terms;


	public PlaylistItemView(WebEngine jfxWebEngine, String playlistName, int playlistId, String coverFile, String search_terms) {
		this.jfxWebEngine = jfxWebEngine;
		this.playlistId = playlistId;
		this.playlistName = playlistName;
		this.coverFile = coverFile;
		this.search_terms = search_terms;
	}

	@Override
	public String getHTML() {
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("playlist_id", playlistId.toString());
		info.put("playlist_name", playlistName);
		info.put("cover_file", coverFile);
		info.put("search_terms", search_terms);

		return doTemplate(View.getHTMLFileContent("playlist_item_view.html"), info);
	}

}
