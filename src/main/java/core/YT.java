package core;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;


public final class YT {


	static public String apikey;
	private final String PROPERTIES_FILENAME = "youtube.apikey";
	static public YouTube api;


	public YT(Credential credential) {
		api = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName("youtube-scial").build();
		getApiKey();
	}

	public void getApiKey() {

		//Load Youtube API key
		Properties properties = new Properties();
		try {
			System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
			InputStream in = new FileInputStream("./assets/" + PROPERTIES_FILENAME);
			properties.load(in);

		}
		catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
					+ " : " + e.getMessage());
			System.exit(1);
		}
		YT.apikey = properties.getProperty("apikey");
	}

}
