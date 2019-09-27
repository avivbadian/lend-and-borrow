package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Availability;
import LoanStuff.ViewModels.Borrow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class AvailabilityController {
    private DataStore db;

    public AvailabilityController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/availabilities/{id}")
    public Availability getAvailabilityById(@PathVariable int id) {
        try {
            ResultSet rs = db.execQuery(String.format("SELECT * FROM availabilities WHERE id='%d'", id));
            if (rs.next()) {

                Availability availability = new Availability();
                availability.Id = rs.getInt(1);
                availability.Item_id = rs.getInt(2);
                availability.Start_date = rs.getDate(3);
                availability.End_date = rs.getDate(4);

                return availability;
            }
        } catch (SQLException ignored) {
        }

        // Availability not found
        return null;
    }

    @DeleteMapping("/availabilities/{id}")
    public ArrayList<Borrow> DeleteAvailability(@PathVariable int id) {
        try {
            ArrayList<Borrow> borrows = new ArrayList<>();
            ResultSet rs = db.execQuery(String.format("SELECT * FROM borrows " +
                    "where availability = '%d' and status = 'pending'", id));
            BorrowsController.BuildBorrow(borrows, rs);
            db.execUpdate(String.format("DELETE FROM availabilities WHERE id='%s'", id));

            return borrows;
        } catch (SQLException e) {
            return null;
        }
    }

    @PostMapping("/availabilities")
    public ResponseEntity postAddAvailability(@RequestBody Availability newAvailability) {
        try {
            db.execUpdate(String.format("INSERT INTO availabilities (id, item_id, start_date, end_date) VALUES\n(DEFAULT,'%s','%s', '%s')",
                    newAvailability.Item_id, newAvailability.Start_date, newAvailability.End_date));

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}