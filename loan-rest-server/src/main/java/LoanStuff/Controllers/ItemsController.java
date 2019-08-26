package LoanStuff.Controllers;

import LoanStuff.DB.DataStore;
import LoanStuff.ViewModels.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                items.add(new Item() {{
                    Id = id;
                    Title = title;
                    Category = category;
                    Description = description;
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
    public ResponseEntity postAddItem(@RequestBody Item newItem) {
        try {
            db.execUpdate(String.format("INSERT INTO items VALUES\n(DEFAULT,'%s','%s','%s')", newItem.Title, newItem.Category, newItem.Description));

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (SQLException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}