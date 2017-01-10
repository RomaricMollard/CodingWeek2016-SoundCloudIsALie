package SoundCloudIsALie.SCiaL;


import core.MyChannel;
import junit.framework.TestCase;


public class MyChannelTest extends TestCase {
	public static void Test() {
		new MyChannel();
		assertFalse("Your channel doesn't exists (shouldn't happen)", MyChannel.getId() == null);
		assertFalse("Your channel doesn't exists (shouldn't happen)", MyChannel.getMyChannel() == null);
	}
}
