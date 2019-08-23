package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserAction {
    @JsonProperty("ownerName")
    public String OwnerName;

    @JsonProperty("borrowerName")
    public String BorrowerName;

    @JsonProperty("itemName")
    public String ItemName;

    @JsonProperty("borrowDate")
    public Date BorrowDate;

    @JsonProperty("imagePath")
    public String ImagePath;
}
