package nimpash.cinema.DataAccess;

import java.util.List;

/**
 * Inteface for basic DB operations
 */
public interface CRUD<T>
{
    enum status {
        OK, invalid_parameter, invalid_document, duplicate_id
    }
    String CreateOne(T obj);
    List<String> CreateMany(List<T> objects);

    List<T> ReadAll();
    T ReadOne(String id);
    T ReadField(String id, String fieldKey);
    List<T> ReadByField(String fieldKey, Object fieldValue);

    boolean Update(T object);
    boolean UpdateField(String id, String fieldKey, Object object);

    T DeleteOne(String id);
    boolean DeleteAll();
    boolean DeleteByField(String fieldKey, Object fieldValue);
}


