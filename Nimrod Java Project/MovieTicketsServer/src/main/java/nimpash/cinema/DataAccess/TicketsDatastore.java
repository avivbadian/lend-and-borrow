package nimpash.cinema.DataAccess;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;

public class TicketsDatastore
{
//	private static Datastore _datastore;

	private static final String PACKAGE_PATH = "nimpash.cinema.objects";

	public static Datastore GetDataStore(String dbName)
	{
		final Morphia morphia = new Morphia(new Mapper());

		// tell Morphia where to find your classes
		// can be called multiple times with different packages or classes
		morphia.mapPackage(PACKAGE_PATH);

		// create the Datastore connecting to the default port on the local host
		final Datastore datastore =
				morphia.createDatastore(SingleMongoClient.getInstance().getClient(),
										dbName);
		datastore.ensureIndexes();

		return datastore;
	}
}
