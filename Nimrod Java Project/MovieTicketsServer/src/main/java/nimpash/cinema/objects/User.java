package nimpash.cinema.objects;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("users")
public class User {

    @Id
    private String _user;
    private String _password;

    public User()
    {

    }

    public User(String user, String password)
    {
        this._user = user;
        this._password = password;
    }

    public String getPassword()
    {
        return _password;
    }

    public String getUser()
    {
        return _user;
    }
}
