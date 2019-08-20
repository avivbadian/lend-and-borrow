package LoanStuff.Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import LoanStuff.ViewModels.User;
import LoanStuff.DB.DataStore;

@RestController
public class UsersControllers {
    private DataStore db;

    public UsersControllers() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/users")
    public ArrayList<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM users");
            while(rs.next()) {
                String uid = rs.getString(1).trim();
                String userName = rs.getString(2).trim();
                String status = rs.getString(3).trim();
                users.add(new User(){{Uid = uid; Username = userName; Status = status;}});
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return users;
    }

    @GetMapping("users/{id}")
    public User getUserByUid(@PathVariable String id) {
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM users WHERE uid = '" + id + "'");
            if (rs.next()){
                String uid = rs.getString(1).trim();
                String userName = rs.getString(2).trim();
                String status = rs.getString(3).trim();
                return (new User(){{Uid = uid; Username = userName; Status = status;}});
            }
            // TODO: log not found
            return null;
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }
    }


    @PostMapping("/users")
    public User createUser(@RequestBody User newUser){
        Map vals = new HashMap();
        vals.put("uid", newUser.Uid);
        vals.put("username", newUser.Username);
        vals.put("status", newUser.Status);

        try {
            if (db.insert("users", vals) == 1) {
                System.out.println("Record added");
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return newUser;
    }

    @PutMapping("users/{uid}")
    User updateUser(@RequestBody User newUser, @PathVariable String uid){
        Map vals = new HashMap();
        vals.put("uid", newUser.Uid);
        vals.put("username", newUser.Username);
        vals.put("status", newUser.Status);

        try {
            db.update("users", String.format( "uid = '%s'", uid), vals);
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return newUser;
    }

    @DeleteMapping("users/{uid}")
    void deleteUser(@PathVariable String uid){
        try {
            db.delete("users",  String.format("uid = '%s'", uid));
        } catch (SQLException e) {
            // TODO: log or something
        }
    }
}