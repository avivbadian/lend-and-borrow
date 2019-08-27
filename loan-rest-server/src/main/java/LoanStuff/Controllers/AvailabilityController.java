package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class AvailabilityController {
    private DataStore db;

    public AvailabilityController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }
}
