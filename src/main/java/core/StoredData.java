package core;


import java.util.ArrayList;
import java.util.HashMap;


public final class StoredData {
	private static HashMap<String, ShortVideoData> storedData = new HashMap<String, ShortVideoData>();
	public static boolean loaded = false;


	public static ArrayList<ShortVideoData> getAll() {
		ArrayList<ShortVideoData> array = new ArrayList<ShortVideoData>(storedData.values());
		return array;
	}

	public static void rate(VideoData vdata, int note) {
		ShortVideoData svd = new ShortVideoData(vdata);
		storedData.remove(svd.getId());
		vdata.setNote(note);
		svd.setNote(note);
		storedData.put(svd.getId(), svd);
		return;

	}

	public static void fav(VideoData vdata, boolean fav) {
		ShortVideoData svd = new ShortVideoData(vdata);
		storedData.remove(svd.getId());
		vdata.setFavorite(fav);
		svd.setFav(fav);
		if (fav) {
			storedData.put(svd.getId(), svd);
		}
	}

	@SuppressWarnings("null")
	public static void unrate(VideoData vdata) {
		ShortVideoData svd = new ShortVideoData(vdata);
		storedData.remove(svd.getId());
		vdata.setNote((Integer) null);
		svd.setNote((Integer) null);
	}

	public static void load() {
		ArrayList<ShortVideoData> temp = StoreData.loadSaved();
		for (ShortVideoData svd : temp) {
			storedData.put(svd.getId(), svd);
		}
		loaded = true;
	}

	public static int getRate(VideoData vdata) {
		if (storedData.containsKey(vdata.getId()))
			return storedData.get(vdata.getId()).getNote();
		else
			return -1;
	}

	public static boolean getFav(VideoData vdata) {
		if (storedData.containsKey(vdata.getId()))
			return storedData.get(vdata.getId()).isFav();
		else
			return false;
	}

	public static void save() {
		ArrayList<ShortVideoData> temp = new ArrayList<ShortVideoData>(storedData.values());
		StoreData.saveAll(temp);
	}
}
