package SoundCloudIsALie.SCiaL;


import core.MyChannel;
import core.Search;
import core.VideoData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	private final boolean DOWNLOAD = false;
	private final boolean RATING = false;


	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(AppTest.class);
		return suite;
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		//	Authentification test
		assertTrue("Just a test to see if jUnit Works", true);
		AuthentificationTest auth = new AuthentificationTest();
		auth.testApp();

		AuthentificationTest.authentification();

		//	Search test
		//		Search By Id
		SearchTest.TestById();
		//		Search by Querry
		SearchTest.TestQuery();
		//		Search related videos
		SearchTest.TestRelated();


		// For the next tests we will use this particular video (asserting that the previous tests are true) :
		Search search = new Search();
		final VideoData data = search.get("S5ge8dFBoz0");

		// 	Stored Data tests
		// 		load test
		StoredDataTest.loadTest();
		// 		Fav test
		StoredDataTest.favTest(data);
		// 		Unfav test
		StoredDataTest.unfavTest(data);

		//	MyChannel Tests
		MyChannelTest.Test();
		// For the next tests we might need your channel (asserting that the previous tests are true);
		new MyChannel();

		//	Rating Tests
		Thread rating;
		if (RATING) {
			rating = new Thread() {
				public void run() {
					RatingTest.Test(data, "like");
					RatingTest.Test(data, "dislike");
					RatingTest.Test(data, "none");
				}
			};
			rating.start();
		}

		//	Comment Tests
		CommentTest.ShowTest(data);

		if (DOWNLOAD)
			DownloadModelTest.DownloadTest(data);



		// Wait for threads
		if (RATING) {
			try {
				rating.join(50000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
