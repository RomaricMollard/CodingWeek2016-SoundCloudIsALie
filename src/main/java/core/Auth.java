package core;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.collect.Lists;

import app.App;
import app.WebViewImproved;
import javafx.application.Platform;
import model.StreamModel;


public class Auth {

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";


	/**
	 * Authorizes the installed application to access user's protected data.
	 *
	 * @param scopes
	 *            list of scopes needed to run youtube upload.
	 * @param credentialDatastore
	 *            name of the credential datastore to cache OAuth tokens
	 */
	public static Credential authorize(List<String> scopes, String credentialDatastore, final WebViewImproved view) throws IOException {

		// Load client secrets.
		Reader clientSecretReader = new InputStreamReader(new FileInputStream("./assets/client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

		// Checks that the defaults have been replaced (Default = "Enter X here").
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println(
					"Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
							+ "into src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
						.build();

		// Build the local server and bind it to port 8080
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();
		String userId = "user";

		Credential credential = null;

		try {
			credential = flow.loadCredential(userId);
			if (credential != null
					&& (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)) {
				return credential;
			}
			// open in browser
			String redirectUri = localReceiver.getRedirectUri();
			final AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

			Platform.runLater(new Runnable() {
				public void run() {
					view.loadURL(authorizationUrl.build());
				}
			});

			// receive authorization code and exchange it for an access token
			String code = localReceiver.waitForCode();
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
			// store credential and return it
			credential = flow.createAndStoreCredential(response, userId);
		}
		finally {
			localReceiver.stop();
		}

		return credential;
	}

	public static void login(final WebViewImproved view) {

		(new Thread() {

			public void run() {
				List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload", "https://www.googleapis.com/auth/youtube.force-ssl",
						"https://www.googleapis.com/auth/youtube.readonly");
				Credential credential = null;
				try {
					credential = Auth.authorize(scopes, "authentification", view);
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				App.youtube = new YT(credential);
				App.player = new StreamModel();
				App.myChannel = new MyChannel();

				Platform.runLater(new Runnable() {
					public void run() {
						App.mainWindow.mainViewer.loadApp();
					}
				});
			}

		}).start();

	}

	public static Credential authorize(List<String> scopes, String credentialDatastore) throws IOException {

		// Load client secrets.
		Reader clientSecretReader = new InputStreamReader(new FileInputStream("./assets/client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

		// Checks that the defaults have been replaced (Default = "Enter X here").
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println(
					"Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
							+ "into src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
						.build();

		// Build the local server and bind it to port 8080
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

		// Authorize.
		return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}

	public static void logout() {

		try {
			//Delete the authentification file created by Oath2. Will not work if the user deplaced the file (we can't track it)
			Files.deleteIfExists(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/authentification"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		App.mainWindow.mainViewer.loadStartPage();

	}

	public static boolean testLogin(WebViewImproved me) {

		if ((new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY)).exists()) {
			Auth.login(me);
			return true;
		}

		return false;
	}


}
