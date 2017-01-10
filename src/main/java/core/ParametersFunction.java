package core;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public final class ParametersFunction {



	public static void savedlPath(String dlPath) {
		try {
			Path path = Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.app.parameter");
			Files.write(path, (dlPath).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			byte[] encoded = Files.readAllBytes(path);

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String loaddlPath() {
		byte[] encoded;
		Path path = Paths.get(System.getProperty("user.home") + "/.oauth-credentials/.app.parameter");
		try {
			if (!Files.notExists(path)) {
				encoded = Files.readAllBytes(path);
				return new String(encoded, Charset.defaultCharset());
			}
			return "";
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
