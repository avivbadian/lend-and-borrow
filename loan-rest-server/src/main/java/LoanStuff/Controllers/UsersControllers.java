package LoanStuff.Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import LoanStuff.Api.User;
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
                String userName = rs.getString(1).trim();
                String password = rs.getString(2).trim();
                users.add(new User(){{id = userName; pass = password;}});
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return users;
    }


    @PostMapping("/users")
    public User createUser(@RequestBody User newUser){
        Map vals = new HashMap();
        vals.put("id", newUser.id);
        vals.put("pass", newUser.pass);

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

    @PutMapping("users/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable String id){
        Map vals = new HashMap();
        vals.put("id", newUser.id);
        vals.put("pass", newUser.pass);

        try {
            db.update("users", String.format( "id = '%s'", id), vals);
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return newUser;
    }

    @DeleteMapping("users/{id}")
    void deleteUser(@PathVariable String id){
        try {
            db.delete("users",  String.format("id = '%s'", id));
        } catch (SQLException e) {
            // TODO: log or something
        }
    }
}