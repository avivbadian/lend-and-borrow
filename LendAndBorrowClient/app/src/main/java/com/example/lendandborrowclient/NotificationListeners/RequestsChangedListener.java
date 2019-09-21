package com.example.lendandborrowclient.NotificationListeners;

import com.example.lendandborrowclient.Models.Borrow;

public interface RequestsChangedListener {
    void RequestAdded(Borrow borrowRequest);
}
