package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Borrow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class BorrowsController {
    private DataStore db;

    public BorrowsController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @PostMapping("/borrows")
    public ResponseEntity postBorrowItem(@RequestBody Borrow borrow){
        return null;
    }
}
