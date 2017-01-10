package core;


import java.util.HashMap;
import java.util.Map;


public final class CacheVideoData {
	/*
	 * This class serves as a storage for a HashMap containing a VideoData object linked to the video id.
	 * The cache itself is called when we search for a specific video, in order to reduce the number of calls to YouTube Server.
	*/
	private static Map<String, VideoData> cache = new HashMap<String, VideoData>();


	public static void add(String id, VideoData data) {
		cache.put(id, data);
	}

	public static void remove(String id) {
		cache.remove(id);
	}

	public static VideoData getVideoData(String id) {
		return cache.get(id);
	}

	public static boolean isVideoDataInCache(String id) {
		return cache.containsKey(id);
	}

	public static boolean isVoid() {
		return cache.isEmpty();
	}
}
