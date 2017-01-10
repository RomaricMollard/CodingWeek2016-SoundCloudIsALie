package SoundCloudIsALie.SCiaL;


import java.io.File;
import java.util.Scanner;

import core.ParametersFunction;
import core.VideoData;
import junit.framework.TestCase;
import model.DownloadModel;


public class DownloadModelTest extends TestCase {

	public static void DownloadTest(VideoData data) {

		try {
			File file = new File(ParametersFunction.loaddlPath() + "/Trailer Game Of The Year 2016.webm");
			if (file.exists())
				file.delete();

			DownloadModel.download(data);
			Thread.sleep(3000);
			String path = ParametersFunction.loaddlPath();
			if (path.equals(""))
				path = "./video";
			System.out.println("Allez vérifier que le fichier \"Trailer Game Of The Year 2016.webm\" est bien présent dans le répertoire \"" + path + "\".\n"
					+ "Si le fichier est bien dans le répertoire (attention cela peut prendre un peu de temps suivant votre connexion) alors entrez 'true', sinon mettez 'false'");

			Scanner sc = new Scanner(System.in);
			assertTrue(sc.nextBoolean());
			sc.close();
		}
		catch (Exception e) {
			assertTrue("There was an exception in download test", false);
		}
	}
}
