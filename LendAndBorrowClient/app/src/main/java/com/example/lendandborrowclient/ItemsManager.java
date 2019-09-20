package com.example.lendandborrowclient;

import com.example.lendandborrowclient.Admins.Listeners.ItemsChangedListener;
import com.example.lendandborrowclient.Models.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {

    private static final ItemsManager instance = new ItemsManager();

    public static ItemsManager getInstance() {
        return instance;
    }

    private List<ItemsChangedListener> listeners;

    private ItemsManager() {
        listeners = new ArrayList<>();
    }

    public void registerListener(ItemsChangedListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void notifyItemsChanged(List<Item> items) {
        for (ItemsChangedListener listener : listeners) {
            listener.ItemsChanged(items);
        }
    }

    public void unregisterListener(ItemsChangedListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
