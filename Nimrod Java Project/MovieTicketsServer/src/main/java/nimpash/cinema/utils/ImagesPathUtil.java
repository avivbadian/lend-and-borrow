package nimpash.cinema.utils;

public class ImagesPathUtil
{
	public static final String IMAGE_FOLDER_PATH = System.getenv("APPDATA") + "/Cinegans/Images/";

	public static String GetFilePath(String fileName)
	{
		return IMAGE_FOLDER_PATH + fileName;
	}
}
