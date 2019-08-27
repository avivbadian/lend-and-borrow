package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Availability;
import LoanStuff.ViewModels.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        // Availability not found
        return null;
    }
}
