package core;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoStatistics;


public class Search {
	private final long DEFAULT_NB_RESULTS = 50;
	private final long DEFAULT_NB_RELATED = 10;
	private final long DEFAULT_NB_CHANNEL = 50;
	public String token;
	//This ArrayList prevent the search to returns already researched videos
	public ArrayList<String> alreadySearched = new ArrayList<String>();


	public Search() {

	}

	public VideoData get(String videoId) {
		if (CacheVideoData.isVideoDataInCache(videoId))
			return CacheVideoData.getVideoData(videoId);
		// #TODO Gérer le cas ou il n'y a pas de premier résultat
		return research(videoId, 1, 0).get(0);
	}

	public ArrayList<VideoData> getRelated(String videoId) {
		return research(videoId, DEFAULT_NB_RELATED, 1);
	}

	public ArrayList<VideoData> getRelated(String videoId, long nbresults) {
		return research(videoId, nbresults, 1);
	}

	public ArrayList<VideoData> research(String entry) {
		return research(entry, DEFAULT_NB_RESULTS, 0);
	}

	public ArrayList<VideoData> research(String entry, long nbresults) {
		return research(entry, nbresults, 0);
	}

	public ArrayList<VideoData> researchFromChannel(String idChannel) {
		return research(idChannel, DEFAULT_NB_CHANNEL, 2);
	}

	/*
	 * Type define the type of research :
	 * 	0 - Video search
	 * 	1 - Related Video search (video id must be referenced in "entry")
	 *  2 - Videos from Channel (channel id must be referenced in "entry")
	 */
	private ArrayList<VideoData> research(String entry, long nbresults, int type) {
		ArrayList<VideoData> videosdata = new ArrayList<VideoData>();
		try {

			// Set list of data returned
			YouTube.Search.List search = YT.api.search().list("id,snippet");

			// Set your API key (compulsory)
			String apiKey = YT.apikey;
			search.setKey(apiKey);

			// Set max results
			search.setMaxResults(nbresults);

			// Research video
			if (type == 0) {
				search.setType("video");

				// Only accept 2D videos (we can't read 3D video anyway)
				search.setVideoDimension("2d");

				// Only accept videos that can be loaded on other website (compulsory)
				search.setVideoEmbeddable("true");

				// Only accept videos that can be loaded outside of Youtube (compulsory)
				search.setVideoSyndicated("true");
				
				

				// Set research entry
				if (token != null) {
					search.setPageToken(token);
				}
				search.setQ(entry);
			}

			if (type == 1) {
				search.setType("video");
				search.setRelatedToVideoId(entry);
			}

			if (type == 2) {
				search.setType("video");
				search.setVideoDimension("2d");
				search.setVideoEmbeddable("true");
				search.setVideoSyndicated("true");
				search.setChannelId(entry);
			}




			// Get search item (necessary to get video items)
			SearchListResponse searchResponse = search.execute();
			token = searchResponse.getNextPageToken();
			List<SearchResult> searchResultList = searchResponse.getItems();
			List<String> videoIds = new ArrayList<String>();

			if (searchResultList != null) {

				Iterator<SearchResult> searchResultListiter = searchResultList.iterator();

				if (!searchResultListiter.hasNext()) {
					System.out.println(" There aren't any results for your query.");
				}

				// Do a second research to get more details

				for (SearchResult searchResult : searchResultList) {

					videoIds.add(searchResult.getId().getVideoId());
				}
				Joiner stringJoiner = Joiner.on(',');
				String videoId = stringJoiner.join(videoIds);

				// Second resquest
				YouTube.Videos.List listVideosRequest = YT.api.videos().list("id, snippet, contentDetails, statistics").setId(videoId);
				listVideosRequest.setKey(YT.apikey);
				VideoListResponse listResponse = listVideosRequest.execute();

				// Get informations from Video elements and create the list of VideoData
				List<Video> videoList = listResponse.getItems();
				if (videoList != null) {
					//get the ratings of the videos
					ArrayList<String> ids = new ArrayList<String>();
					for (Video video : videoList) {
						ids.add(video.getId());
					}
					HashMap<String, String> ratings = Rate.getRating(ids);

					//create VideoData's
					Iterator<Video> videoIterator = videoList.iterator();
					while (videoIterator.hasNext()) {
						// get all elements
						Video singleVideo = videoIterator.next();
						String rId = singleVideo.getId();
						String duration = singleVideo.getContentDetails().getDuration();
						VideoStatistics stats = singleVideo.getStatistics();

						if (!alreadySearched.contains(rId)) {
							// Create and add VideoData to the list
							alreadySearched.add(rId);
							VideoData vdata = new VideoData(rId, singleVideo.getSnippet(), duration, stats);
							vdata.setRating(ratings.get(rId));
							// Add videoData to cache
							if (!CacheVideoData.isVideoDataInCache(rId))
								CacheVideoData.add(rId, vdata);
							videosdata.add(vdata);
						}

					}
				}
			}

		}
		catch (


		GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage() + " more details : " + e.getDetails().toString());
		}
		catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		return videosdata;
	}

	public static String getMyChannelId() {
		String id = null;
		try {
			YouTube.Channels.List search = YT.api.channels().list("id");
			String apiKey = YT.apikey;
			search.setKey(apiKey);
			search.setMine(true);
			Channel mychannel = search.execute().getItems().get(0);
			id = mychannel.getId();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static ChannelData getChannelData(String id) {
		try {
			YouTube.Channels.List search = YT.api.channels().list("id, snippet, contentDetails, statistics");
			String apikey = YT.apikey;
			search.setKey(apikey);
			search.setId(id);
			Channel channel = search.execute().getItems().get(0);
			return new ChannelData(channel);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
