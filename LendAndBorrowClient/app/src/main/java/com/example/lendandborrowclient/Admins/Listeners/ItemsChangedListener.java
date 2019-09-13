package com.example.lendandborrowclient.Admins.Listeners;

import com.example.lendandborrowclient.Models.Item;

import java.util.List;

public interface ItemsChangedListener
{
    void ItemsChanged(List<Item> items);
}
