package utils;


import java.util.ArrayList;
import java.util.HashMap;

import core.ChannelData;
import core.ShortVideoData;
import core.VideoData;


public class Utils {

	//Suppress all html tokens from a String 
	public static String html2text(String html) {
		return html.replaceAll("\\<[^>]*>", "");
	}


	//Transform a VideoData to a HashMap (used in Graphic part)
	public static HashMap<String, String> getMap(VideoData vdata) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("video_id", vdata.getId());
		map.put("video_title", vdata.getTitle());
		map.put("video_descr", vdata.getDescription());
		map.put("video_thumbnail", vdata.getThumbnail().getMedium().getUrl());
		map.put("video_channelTitle", vdata.getChannelTitle());
		if(vdata.getStats()!=null && vdata.getStats().getLikeCount()!=null){
			int like = vdata.getStats().getLikeCount().intValue();
			int dislike = vdata.getStats().getDislikeCount().intValue();
			map.put("video_viewCount", vdata.getStats().getViewCount().toString());
			map.put("video_likeCount", like + "");
			map.put("video_dislikeCount", dislike + "");
			if (like + dislike == 0) {
				map.put("video_likePercentage", "100%");
			}
			else {
				map.put("video_likePercentage", ((like * 100) / (like + dislike)) + "%");
			}
		}
		map.put("video_fav", Boolean.toString(vdata.isFavorite()));
		map.put("video_rate", vdata.getRating() + "");
		return map;
	}


	public static HashMap<String, String> getMap(ShortVideoData svd) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("video_id", svd.getId());
		map.put("video_title", svd.getTitle());
		map.put("video_descr", svd.getDescription());
		map.put("video_thumbnail", svd.getThumbnailURL());
		map.put("video_fav", Boolean.toString(svd.isFav()));
		map.put("video_rate", svd.getNote() + "");
		return map;
	}

	// Transform a ChannelData to a HashMap (used in Graphic part)
	public static HashMap<String, String> getMap(ChannelData cdata) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("channel_id", cdata.getId());
		map.put("channel_title", cdata.getSnippet().getTitle());
		map.put("channel_thumbnailurl", cdata.getSnippet().getThumbnails().getMedium().getUrl());
		return map;
	}

	// transform an ArrayList of String to a single CSV formatted String (used with list of ids in Rate class)
	public static String getCSV(ArrayList<String> string) {
		String s = "";
		boolean first = true;
		for (String k : string) {
			if (!first) {
				s += ",";
			}
			else {
				first = !first;
			}
			s += k;
		}
		return s;
	}

}
