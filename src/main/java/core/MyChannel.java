package core;


public class MyChannel {

	private static String id;
	private static ChannelData myChannel;


	//This class is used like a final class, but for clarity (and because we were not sure how it would be used when it was created), it's not final.
	public MyChannel() {
		id = Search.getMyChannelId();
		updateChannelData();
	}

	//Call a search function to update all the data of the user channel from YouTube
	private static void updateChannelData() {

		myChannel = Search.getChannelData(id);

	}

	public static String getId() {
		return id;
	}

	public static ChannelData getMyChannel() {
		return myChannel;
	}
}
