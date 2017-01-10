package SoundCloudIsALie.SCiaL;


import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.common.collect.Lists;

import core.Auth;
import core.YT;
import junit.framework.TestCase;


public final class AuthentificationTest extends TestCase {
	public static YT authentification() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload", "https://www.googleapis.com/auth/youtube.force-ssl",
				"https://www.googleapis.com/auth/youtube.readonly");
		try {
			Credential credential = Auth.authorize(scopes, "authentification");
			YT youtube = new YT(credential);
			assertTrue(true);
			return youtube;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(false);
		return null;

	}

	public void testApp() {
		YT youtube = authentification();
		assertTrue("Authentification failure", youtube != null);
	}

}
