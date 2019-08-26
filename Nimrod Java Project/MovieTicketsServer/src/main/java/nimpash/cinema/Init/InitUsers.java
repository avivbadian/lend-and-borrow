package nimpash.cinema.Init;

import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.objects.User;
import nimpash.cinema.DataAccess.DataAccessObject;

import java.util.ArrayList;

public class InitUsers
{
    public static ArrayList<User> AddUser()
    {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("admin", "admin123" ));
        users.add(new User("pasha", "Pasha123"));
        users.add(new User("nimrod", "nimrod123"));

        CRUD<User> crud = new DataAccessObject<>(User.class);
        crud.DeleteAll();
        crud.CreateMany(users);

        return users;
    }
}
