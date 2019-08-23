package LoanStuff.Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import LoanStuff.ViewModels.UserAction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import LoanStuff.ViewModels.User;
import LoanStuff.DB.DataStore;

@RestController
public class UsersController {
    private DataStore db;

    public UsersController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/users")
    public ArrayList<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM account");
            while(rs.next()) {
                String uid = rs.getString(1).trim();
                String userName = rs.getString(2).trim();
                String status = rs.getString(3).trim();
                String profileImagePath = rs.getString(4).trim();
                users.add(new User(){{Uid = uid; Username = userName; Status = status; ImagePath = profileImagePath;}});
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
            rs = db.execQuery("SELECT * FROM account WHERE uid = '" + id + "'");
            if (rs.next()){
                String uid = rs.getString(1).trim();
                String userName = rs.getString(2).trim();
                String status = rs.getString(3).trim();
                String profileImagePath = rs.getString(4).trim();
                return (new User(){{Uid = uid; Username = userName; Status = status; ImagePath = profileImagePath;}});
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
        vals.put("username", Objects.toString( newUser.Username, ""));
        vals.put("status", Objects.toString( newUser.Status, ""));
        vals.put("image_path", Objects.toString( newUser.ImagePath, ""));
        try {
            if (db.insert("account", vals) == 1) {
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
        if (Objects.toString(newUser.Username, "") != "")
            vals.put("username", newUser.Username);
        if (Objects.toString(newUser.Status, "") != "")
            vals.put("status", newUser.Status);
        if (Objects.toString(newUser.ImagePath, "") != "")
            vals.put("image_path", newUser.ImagePath);
        try {
            db.update("account", String.format( "uid = '%s'", uid), vals);
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return newUser;
    }

    @DeleteMapping("users/{uid}")
    void deleteUser(@PathVariable String uid){
        try {
            db.delete("account",  String.format("uid = '%s'", uid));
        } catch (SQLException e) {
            // TODO: log or something
        }
    }

    @GetMapping("users/{uid}/borrows")
    ArrayList<UserAction> getUserBorrows(@PathVariable String uid){

        ArrayList<UserAction> actions = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT item.image_path, borrowed_item.borrow_date, item.item_name, a1.username, a2.username\n" +
                    "FROM borrowed_item, item, account as a1, account as a2\n" +
                    "WHERE borrowed_item.iid = item.iid AND\n" +
                    "\t  a1.uid = item.holder AND\n" +
                    "\t  a2.uid = borrowed_item.borrower AND \n" +
                    "\t  borrowed_item.borrower = '" + uid + "'");
            while(rs.next()) {
                UserAction cur = new UserAction();
                cur.ImagePath = rs.getString(1).trim();
                cur.BorrowDate = rs.getDate(2);
                cur.ItemName = rs.getString(3).trim();
                cur.OwnerName = rs.getString(4).trim();
                cur.BorrowerName = rs.getString(5).trim();
                actions.add(cur);
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return actions;
    }

    @GetMapping("users/{uid}/lents")
    ArrayList<UserAction> getUserLents(@PathVariable String uid){

        ArrayList<UserAction> actions = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT item.image_path, borrowed_item.borrow_date, item.item_name, a1.username, a2.username\n" +
                    "FROM borrowed_item, item, account as a1, account as a2\n" +
                    "WHERE borrowed_item.iid = item.iid AND\n" +
                    "\t  a1.uid = item.holder AND\n" +
                    "\t  a2.uid = borrowed_item.borrower AND \n" +
                    "\t  item.holder = '" + uid + "'");
            while(rs.next()) {
                UserAction cur = new UserAction();
                cur.ImagePath = rs.getString(1).trim();
                cur.BorrowDate = rs.getDate(2);
                cur.ItemName = rs.getString(3).trim();
                cur.OwnerName = rs.getString(4).trim();
                cur.BorrowerName = rs.getString(5).trim();
                actions.add(cur);
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return actions;
    }
}