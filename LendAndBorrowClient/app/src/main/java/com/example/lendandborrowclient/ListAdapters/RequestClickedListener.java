package com.example.lendandborrowclient.ListAdapters;

import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Item;

public interface RequestClickedListener {
    void RequestConfirmClicked(Borrow request, Item relatedItem, Availability relatedAvailability);
    void RequestDeclineClicked(Borrow request, Item relatedItem, Availability relatedAvailability);
}
