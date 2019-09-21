package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Availability;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
@RestController
public class ItemsController {
    private DataStore db;

    public ItemsController() throws SQLException, ClassNotFoundException {
        db = new DataStore();
    }

    @GetMapping("/items")
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery("SELECT * FROM items");
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2).trim();
                String category = rs.getString(3).trim();
                String description = rs.getString(4).trim();
                String path = rs.getString(5).trim();
                items.add(new Item() {{
                    Id = id;
                    Title = title;
                    Category = category;
                    Description = description;
                    Path = path;
                }});
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return items;
    }

    @GetMapping("/items/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        try {
            ResultSet rs = db.execQuery(String.format("SELECT * FROM items WHERE id='%d'", itemId));
            if (rs.next()) {
                Item item = new Item();
                item.Id = rs.getInt(1);
                item.Title = rs.getString(2).trim();
                item.Category = rs.getString(3).trim();
                item.Description = rs.getString(4).trim();
                item.Path = rs.getString(5).trim();
                return item;
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        // Item not found
        return null;
    }

    @PostMapping("/items")
    public int postAddItem(@RequestBody Item newItem) {
        try {
            db.execUpdate(String.format("INSERT INTO items (id, title, category, description, path) VALUES\n(DEFAULT,'%s','%s','%s', '%s')",
                    newItem.Title, newItem.Category, newItem.Description, newItem.Path));

            ArrayList<Item> allItems = getAllItems();
            Optional<Item> createdNewItem = allItems.
                    stream().
                    filter(item -> item.Title.equals(newItem.Title) &&
                            item.Category.equals(newItem.Category) &&
                            item.Description.equals(newItem.Description)).
                    findFirst();


            if (createdNewItem.isPresent())
                return createdNewItem.get().Id;
            else
                return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    @GetMapping("/items/{id}/availabilities")
    public ArrayList<Availability> getAllItemAvailabilities(@PathVariable int id) {
        ArrayList<Availability> availability = new ArrayList<>();
        ResultSet rs;
        try {
            rs = db.execQuery(String.format("SELECT * " +
                    "FROM availabilities " +
                    "WHERE item_id='%s' AND (select count(*) FROM borrows WHERE availabilities.id = availability AND status='approved') = 0", id));
            while (rs.next()) {
                Availability newAval = new Availability();
                newAval.Id = rs.getInt(1);
                newAval.Item_id = rs.getInt(2);
                newAval.Start_date = rs.getDate(3);
                newAval.End_date = rs.getDate(4);

                availability.add(newAval);
            }
        } catch (SQLException e) {
            // TODO: log or something
            return null;
        }

        return availability;
    }


    @DeleteMapping("/items/{id}")
    public List<Borrow> deleteItem(@PathVariable int id) {
        try {

            List<Integer> itemsAvailabilitiesIds = getAllItemAvailabilities(id).
                    stream().
                    map(availability -> availability.Id).collect(Collectors.toList());
            ArrayList<Borrow> borrows = new ArrayList<>();
            ResultSet rs = db.execQuery("SELECT * FROM borrows");
            BorrowsController.BuildBorrow(borrows, rs);
            List<Borrow> itemBorrows = borrows.stream().
                    filter(borrow -> itemsAvailabilitiesIds.contains(borrow.Availability) &&
                            borrow.Status == Status.pending).
                    collect(Collectors.toList());
            db.execUpdate(String.format("DELETE FROM items WHERE id='%s'", id));

            return itemBorrows;
        } catch (SQLException e) {
            return null;
        }
    }
}