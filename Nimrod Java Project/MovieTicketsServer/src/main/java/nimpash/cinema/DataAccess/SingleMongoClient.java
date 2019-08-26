package nimpash.cinema.DataAccess;

import com.mongodb.MongoClient;

/**
 * enables to create only one instance of this mongo client
 * to use: call the getInstance() method
 *
 */
public class SingleMongoClient
{
    private static volatile SingleMongoClient instance;
    private MongoClient client;

    private SingleMongoClient()
	{
		client = new MongoClient();
	}

    public static SingleMongoClient getInstance()
	{
        if (instance == null)
			synchronized (SingleMongoClient.class)
			{
				if (instance == null)
					instance = new SingleMongoClient();
			}

        return instance;
    }

    public MongoClient getClient()
	{
        return client;
    }
}
