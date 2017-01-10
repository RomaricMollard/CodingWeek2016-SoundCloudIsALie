package SoundCloudIsALie.SCiaL;


import java.util.ArrayList;
import java.util.HashMap;

import core.Rate;
import core.VideoData;
import junit.framework.TestCase;


public class RatingTest extends TestCase {
	public static void Test(VideoData data, String rating) {
		Rate.rate(data.getId(), rating);
		try {
			Thread.sleep(15000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(data.getId());
		HashMap<String, String> temp2 = Rate.getRating(temp);
		assertTrue("Failed rating test for " + rating, rating.equals(temp2.get(data.getId())));
	}
}
