package app;


import core.Auth;


public class MainContent extends WebViewImproved {

	View currentContent;
	WebViewImproved me;


	public MainContent(String id) {
		super(id);

		this.me = this;

		if (!Auth.testLogin(me)) {
			loadStartPage();
		}

	}

	public void loadStartPage() {
		load("startPage.html");
		createBridge(new MainBridge(), "MainContentBridge");
	}

	public void loadApp() {
		load("mainContent.html");
		createBridge(new MainBridge(), "MainContentBridge");

		SearchView content = new SearchView(jfxWebEngine);
		MenuView menu = new MenuView(jfxWebEngine, this);

		CommentsView comments = new CommentsView(jfxWebEngine);
		StreamView stream = new StreamView(jfxWebEngine, comments);

		add(menu, "left_menu");
		add(content, "content");
		add(stream, "stream");
		add(comments, "right_menu");

		currentContent = content;
	}

	public void changeView(ContentViewType viewType) {
		remove(currentContent);

		if (viewType == ContentViewType.Search) {
			currentContent = new SearchView(jfxWebEngine);
		}
		else if (viewType == ContentViewType.Playlists) {
			currentContent = new PlaylistsView(jfxWebEngine);
		}
		else if (viewType == ContentViewType.MyVideos) {
			currentContent = new MyVideosView(jfxWebEngine);
		}
		else if (viewType == ContentViewType.Upload) {
			currentContent = new UploadView(jfxWebEngine);
		}
		else if (viewType == ContentViewType.Parameters) {
			currentContent = new ParameterView(jfxWebEngine);
		}
		else if (viewType == ContentViewType.Favorites) {
			currentContent = new MyFavoriteView(jfxWebEngine);
		}

		append(currentContent, "content");
	}


	/* Bridge JS/JAVA */

	public class MainBridge extends Bridge {


		public void login() {

			Auth.login(me);

		}


	}

}
