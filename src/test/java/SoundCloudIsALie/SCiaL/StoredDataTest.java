package SoundCloudIsALie.SCiaL;


import core.StoredData;
import core.VideoData;
import junit.framework.TestCase;


public class StoredDataTest extends TestCase {
	public static void loadTest() {
		StoredData.load();
		assertTrue("storedData didn't load", StoredData.loaded);
	}

	public static void favTest(VideoData data) {
		StoredData.fav(data, true);
		assertTrue("Favorited didn't work as expected", StoredData.getFav(data));
	}

	public static void unfavTest(VideoData data) {
		StoredData.fav(data, false);
		assertFalse("Unfavorited didn't work as expected", StoredData.getFav(data));
	}

}
