package com.example.lendandborrowclient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ItemsFragment();
//            case 1:
//                return new MyActivityFragment();
//            case 2:
//                return new RequestsFragment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Items";
            case 1:
                return "Requests";
            case 2:
                return "Search";

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
