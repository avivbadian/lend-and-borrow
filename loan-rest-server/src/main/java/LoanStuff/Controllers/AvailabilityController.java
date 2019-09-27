package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Availability;
import LoanStuff.ViewModels.Borrow;
import LoanStuff.ViewModels.Item;
import LoanStuff.ViewModels.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
}