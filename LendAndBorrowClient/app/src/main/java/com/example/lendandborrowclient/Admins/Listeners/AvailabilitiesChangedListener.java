package com.example.lendandborrowclient.Admins.Listeners;

import com.example.lendandborrowclient.Models.Availability;

import java.util.List;

public interface AvailabilitiesChangedListener
{
    void HallsChanged(List<Availability> availabilities);
}
