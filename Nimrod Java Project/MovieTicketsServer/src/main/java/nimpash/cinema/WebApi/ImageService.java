package nimpash.cinema.WebApi;

import nimpash.cinema.utils.ImagesPathUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Paths;

@Path("/images")
public class ImageService {

	/*
	 * The maximum allowed file size in megabytes.
	 * Files larger than this size will not be uploadable or downloadable.
	 */
	private static final int MAX_SIZE_IN_MB = 5;

	/*
	 * The directory where the images will be stored.
	 * Make sure this directory exists before you run the service.
	 */
	private static final String RELATIVE_IMAGE_DIR = "/Images/";
	private static final java.nio.file.Path BASE_DIR = Paths.get("C:\\Users\\ppoltar\\Android_Project\\MovieTicketsServer\\src\\main\\resources\\Images");


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response UploadImage2(@FormDataParam("upload") InputStream stream,
								 @FormDataParam("upload") FormDataContentDisposition data) throws IOException
	{
		try {
			int read;
			byte[] bytes = new byte[1024];

			OutputStream out = new FileOutputStream(new File(ImagesPathUtil.GetFilePath(data.getFileName())));
			while ((read = stream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return Response.ok().build();//Response.status(Status.CREATED).entity(Parameters.IMAGE_FOLDER+fileName).build();
	}


	/*
     * Download a JPEG file.
     */
	@GET
	@Path("{name}.jpg")
	@Produces("image/jpeg")
	public InputStream getJpegImage(@PathParam("name") String fileName) throws IOException {

		fileName += ".jpg";

		return GetImageResourceStream(fileName);
	}

	/*
	 * Download a PNG file.
	 */
	@GET
	@Path("{name}.png")
	@Produces("image/png")
	public InputStream getPngImage(@PathParam("name") String fileName) throws IOException {

		fileName += ".png";

		return GetImageResourceStream(fileName);
	}

	// Help function to get stream for image
	private InputStream GetImageResourceStream(String imageName) throws FileNotFoundException
	{
		File f = new File(ImagesPathUtil.GetFilePath(imageName));

		if (f.exists())
			return new FileInputStream(ImagesPathUtil.GetFilePath(imageName));
		else
			throw new WebApplicationException("File: '" + imageName + "' not found");

	}

}