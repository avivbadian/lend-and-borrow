package nimpash.cinema.Managers;

import nimpash.cinema.objects.User;
import nimpash.cinema.DataAccess.DataAccessObject;

public class UserManager {

    private DataAccessObject<User> _userDao;

    public UserManager()
    {
        _userDao = new DataAccessObject<>(User.class);
    }

    public User HandleValidateUser(User user)
    {
        User userFromDb = _userDao.ReadOne(user.getUser().toLowerCase());

        if (userFromDb == null || !userFromDb.getPassword().equals(user.getPassword()))
            return null;

        return user;
    }
}
