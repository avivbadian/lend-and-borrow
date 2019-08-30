package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class AdminController {
    private DataStore db;

    public AdminController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @PostMapping("/admin")
    public ResponseEntity validateAdmin(@RequestBody Admin admin) {
        if (admin == null)
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        ArrayList<Admin> users = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery(String.format("select * \nfrom admins\nwhere username='%s' AND password='%s'",
                    admin.Username,
                    admin.Password));
            while (rs.next()) {
                String userName = rs.getString(1).trim();
                String password = rs.getString(2).trim();
                users.add(new Admin() {{
                    Username = userName;
                    Password = password;
                }});
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        if (users.size() > 0)
            return new ResponseEntity(HttpStatus.ACCEPTED);

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}