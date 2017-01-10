package SoundCloudIsALie.SCiaL;


import java.util.ArrayList;

import core.Search;
import core.VideoData;
import junit.framework.TestCase;


public class SearchTest extends TestCase {
	public static void TestById() {
		Search search = new Search();
		VideoData data = search.get("S5ge8dFBoz0");
		assertTrue("Id of the video is false", data.getId().equals("S5ge8dFBoz0"));
		assertTrue("Title of the video isn't what is expected", data.getTitle().equals("Trailer Game Of The Year 2016"));
		assertTrue("Description of the video isn't what is expected", data.getDescription().equals("This is the best game ever made !!"));
		assertTrue("Thumbnail url is false", data.getThumbnail().getMedium().getUrl().equals("https://i.ytimg.com/vi/S5ge8dFBoz0/mqdefault.jpg"));

	}

	public static void TestQuery() {
		Search search2 = new Search();
		ArrayList<VideoData> vdatas = search2.research("musique");
		assertTrue("No video found", vdatas.size() > 0);
		assertTrue("Too much videos found", vdatas.size() <= 50);

	}

	public static void TestRelated() {
		Search search = new Search();
		ArrayList<VideoData> vdatas = search.getRelated("S5ge8dFBoz0");
		assertTrue("No related video found", vdatas.size() > 0);
		assertTrue("Too much related video returned", vdatas.size() <= 10);
		assertFalse("Same video returned", vdatas.get(0).getId().equals("S5ge8dFBoz0"));
	}
}
