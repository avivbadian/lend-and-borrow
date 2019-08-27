package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Branch;
import LoanStuff.ViewModels.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class BranchesController {
    private DataStore db;

    public BranchesController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/branches")
    public ArrayList<Branch> getAllBranches() {
        ArrayList<Branch> branches = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM branches");
            while (rs.next()) {
                //-- id, title, address
                Branch branch = new Branch();
                branch.Title = rs.getString(1).trim();
                branch.Address = rs.getString(2).trim();

                branches.add(branch);
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return branches;
    }

    @PostMapping("/branches")
    public ResponseEntity postAddBranch(@RequestBody Branch newBranch) {
        try {
            db.execUpdate(String.format("INSERT INTO branches VALUES ('%s', '%s')", newBranch.Title, newBranch.Address));

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/branches/{title}")
    public ResponseEntity deleteBranch(@PathVariable String title) {
        try {
            db.execUpdate(String.format("DELETE FROM branches WHERE title='%s'", title));

            return new ResponseEntity(HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}