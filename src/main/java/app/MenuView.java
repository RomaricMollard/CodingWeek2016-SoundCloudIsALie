package app;


import java.util.HashMap;

import core.Auth;
import javafx.scene.web.WebEngine;


public class MenuView extends View {

	MainContent mainContentView;


	public MenuView(WebEngine jfxWebEngine, MainContent mainContentView) {
		this.mainContentView = mainContentView;
		this.jfxWebEngine = jfxWebEngine;

		createBridge(new MenuViewBridge(), "MenuViewBridge");
	}

	@Override
	public String getHTML() {

		String thumbnails = App.myChannel.getMyChannel().getThumbnail();

		HashMap<String, String> info = new HashMap<String, String>();
		info.put("username", App.myChannel.getMyChannel().getSnippet().getTitle());
		info.put("thumbnails", thumbnails.equals("") ? "http://www.snut.fr/wp-content/uploads/2015/06/image-de-profil-2.jpg" : thumbnails);

		return doTemplate(View.getHTMLFileContent("left_menu.html"), info);
	}

	@Override
	public void justAdded() {

		append(new MenuButtonView(jfxWebEngine, "Mes vidéos", 1), "menu_buttons_list");
		append(new MenuButtonView(jfxWebEngine, "Mes favoris", 3), "menu_buttons_list");
		append(new MenuButtonView(jfxWebEngine, "Playlists", 6), "menu_buttons_list");
		append(new MenuButtonView(jfxWebEngine, "Recherche", 0), "menu_buttons_list");
		append(new MenuButtonView(jfxWebEngine, "Mettre en ligne une vidéo", 5), "menu_buttons_list");
		append(new MenuButtonView(jfxWebEngine, "Paramètres", 2), "menu_buttons_list");

	}


	public class MenuViewBridge extends Bridge {
		public void changeView(int viewId) {

			if (viewId == 0) {
				mainContentView.changeView(ContentViewType.Search);
			}
			else if (viewId == 1) {
				mainContentView.changeView(ContentViewType.MyVideos);
			}
			else if (viewId == 2) {
				mainContentView.changeView(ContentViewType.Parameters);
			}
			else if (viewId == 3) {
				mainContentView.changeView(ContentViewType.Favorites);
			}
			else if (viewId == 5) {
				mainContentView.changeView(ContentViewType.Upload);
			}
			else if (viewId == 6) {
				mainContentView.changeView(ContentViewType.Playlists);
			}
		}

		public void logout() {
			Auth.logout();
		}
	}
}
