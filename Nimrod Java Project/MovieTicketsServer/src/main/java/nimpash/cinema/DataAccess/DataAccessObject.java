package nimpash.cinema.DataAccess;

import nimpash.cinema.utils.Constants;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides easy DB access for OO experience
 * @param <T> The object type to handle a collection for
 */
public class DataAccessObject<T> implements CRUD<T>
{
	private Datastore _datastore;
	private Class<T> _class;

	/**
	 * constructor
	 */
	public DataAccessObject(Class<T> classHandled)
	{
		_class = classHandled;
		this._datastore = TicketsDatastore.GetDataStore(Constants.DB.TICKET_DATABASE);
	}

	@Override
	public String CreateOne(T obj)
	{
		try
		{
			return (String) _datastore.save(obj).getId();
		}
		catch (Exception e)
		{
			System.out.println("Error saving");

			throw e;
		}
	}

	@Override
	public List<String> CreateMany(List<T> objects)
	{
		List<String> keys = new ArrayList<>();

		_datastore.save(objects).forEach(key -> keys.add((String) key.getId()));

		return keys;
	}

	@Override
	public List<T> ReadAll()
	{
		return _datastore.find(_class).asList();
	}

	@Override
	public T ReadOne(String id)
	{
		return _datastore.get(_class, id);
	}

	@Override
	public T ReadField(String id, String fieldKey)
	{
		return getIdQuery(id).project(fieldKey, true).get();
	}

	@Override
	public List<T> ReadByField(String fieldKey, Object fieldValue)
	{
		return _datastore.createQuery(_class).filter(fieldKey, fieldValue).asList();
	}

	@Override
	public boolean Update(T object)
	{
		return _datastore.update(object,
								 _datastore.createUpdateOperations(_class))
				.getWriteResult()
				.wasAcknowledged();
	}

	@Override
	public boolean UpdateField(String id, String fieldKey, Object object)
	{
		UpdateOperations<T> updateOperation = _datastore.createUpdateOperations(_class).set(fieldKey, object);
		UpdateResults result = _datastore.update(getIdQuery(id), updateOperation);

		return result.getWriteResult().wasAcknowledged();
	}

	@Override
	public T DeleteOne(String id)
	{
		return _datastore.findAndDelete(getIdQuery(id));
	}

	@Override
	public boolean DeleteAll()
	{
		try
		{
			return _datastore.delete(_datastore.createQuery(_class)).wasAcknowledged();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean DeleteByField(String fieldKey, Object fieldValue){
		 return _datastore.delete(_datastore.createQuery(_class).filter(fieldKey,fieldValue)).wasAcknowledged();
	}


	private Query<T> getIdQuery(String id)
	{
		return _datastore.createQuery(_class).filter(Constants.Movie.ID, id);
	}
}


