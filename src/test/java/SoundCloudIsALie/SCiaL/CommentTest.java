package SoundCloudIsALie.SCiaL;


import core.CommentList;
import core.VideoData;
import junit.framework.TestCase;


public class CommentTest extends TestCase {
	public static void ShowTest(VideoData data) {
		CommentList commentlist = new CommentList(data.getId());
		assertFalse("The comment list is empty, this is not normal", commentlist.getCommentList().isEmpty());

	}
}
