package core;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoGetRatingResponse;
import com.google.api.services.youtube.model.VideoRating;


public final class Rate {
	public static void rate(String id, String rating) {
		if (!rating.equals("like") && !rating.equals("dislike") && !rating.equals("none")) {
			System.err.println("Warning : incorrect argument, rating must be like, dislike or none");
			return;
		}
		try {
			YouTube.Videos.Rate rate = YT.api.videos().rate(id, rating);
			String apiKey = YT.apikey;
			rate.setKey(apiKey);
			rate.execute();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> getRating(ArrayList<String> id) {

		try {
			YouTube.Videos.GetRating ratingsSearch = YT.api.videos().getRating(utils.Utils.getCSV(id));
			String apiKey = YT.apikey;
			ratingsSearch.setKey(apiKey);
			VideoGetRatingResponse ratingResponse = ratingsSearch.execute();
			HashMap<String, String> map = new HashMap<String, String>();

			for (VideoRating rating : ratingResponse.getItems()) {

				map.put(rating.getVideoId(), rating.getRating());
			}
			return map;

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
