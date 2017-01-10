package core;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/*
 * Format of favorite videos :
 * id\n
 */


public final class StoreData {

	public static void saveAll(ArrayList<ShortVideoData> temp) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			Files.delete(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data"));

			mapper.writeValue(new File(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data"), temp);

			if (temp.isEmpty()) {
				Files.write(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data"), ("[]").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<ShortVideoData> loadSaved() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (!(new File(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data")).exists()) {
				Files.write(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data"), ("[]").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			}

			List<ShortVideoData> favList = mapper.readValue(new File(System.getProperty("user.home") + "/.oauth-credentials/.favorite.data"),
					mapper.getTypeFactory().constructCollectionType(List.class, ShortVideoData.class));
			ArrayList<ShortVideoData> tmp = new ArrayList<ShortVideoData>(favList);
			return tmp;

		}
		catch (JsonParseException e) {
			e.printStackTrace();
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void saveLastVideo(String id) {
		try {
			Files.write(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.last.data"), (id).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getLastVideo() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.last.data"));
			return new String(encoded, Charset.defaultCharset());
		}
		catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
}
