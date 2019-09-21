package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Borrow;
import LoanStuff.ViewModels.Item;
import LoanStuff.ViewModels.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class BorrowsController {
    private DataStore db;

    public BorrowsController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/borrows")
    public ArrayList<Borrow> getAllBorrows() {
        ArrayList<Borrow> borrows = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM borrows");
            BuildBorrow(borrows, rs);
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return borrows;
    }

    @PostMapping("/borrows")
    public ResponseEntity postBorrowItem(@RequestBody Borrow borrow) {
        borrow.Status = Status.pending;
        try {
            db.execUpdate(String.format("INSERT INTO borrows (availability, branch, phone, email, first_name, last_name, status) VALUES ('%s','%s','%s','%s','%s','%s','%s')",
                    borrow.Availability,
                    borrow.Branch,
                    borrow.Phone,
                    borrow.Email,
                    borrow.First_name,
                    borrow.Last_name,
                    borrow.Status.toString()));

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/borrows/{id}/{status}")
    public List<Borrow> postChangeBorrowStatus(@PathVariable int id, @PathVariable Status status) {
        try {
            // Update the status
            db.execUpdate(String.format("UPDATE borrows SET status='%s' WHERE id='%s'", status, id));

            // If the request is approved, decline all other borrow requests to the same
            // availability
            if (status == Status.approved) {
                ResultSet rs = db.execQuery(String.format("SELECT availability FROM borrows WHERE id='%d'", id));
                rs.next();
                int availabilityId = rs.getInt(1);
                List<Borrow> toDecline = getAllBorrows().
                        stream().
                        filter(borrow -> borrow.Availability == availabilityId
                                && borrow.Id != id).collect(Collectors.toList());
                db.execUpdate(String.format("UPDATE borrows SET status='declined' WHERE availability='%s' AND id <> '%s'", availabilityId, id));
                return toDecline;
            }
        } catch (SQLException e) {
        }

        return new ArrayList<Borrow>();
    }

    @GetMapping("/borrows/pending")
    public ArrayList<Borrow> getAllPendingBorrows() {
        ArrayList<Borrow> borrows = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM borrows WHERE status = 'pending'");
            BuildBorrow(borrows, rs);
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return borrows;
    }

    private void BuildBorrow(ArrayList<Borrow> borrows, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Borrow borrow = new Borrow();
            borrow.Id = rs.getInt(1);
            borrow.Availability = rs.getInt(2);
            borrow.Branch = rs.getString(3).trim();
            borrow.Phone = rs.getString(4).trim();
            borrow.Email = rs.getString(5).trim();
            borrow.First_name = rs.getString(6).trim();
            borrow.Last_name = rs.getString(7).trim();
            borrow.Status = Status.valueOf(rs.getString(8).trim());
            borrows.add(borrow);
        }
    }
}