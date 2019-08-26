package nimpash.cinema;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class AppConfig extends ResourceConfig
{
	public AppConfig() {
		packages("nimpash.cinema");
		register(MultiPartFeature.class);
	}
}