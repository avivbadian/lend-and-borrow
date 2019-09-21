package com.example.lendandborrowclient.Admins;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.example.lendandborrowclient.NotificationListeners.ItemsChangedListener;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagementActivity extends AppCompatActivity implements ItemsChangedListener
{
    private static String tabTitles[] = new String[] { "Items", "Availabilities", "Requests" };

    private static final int ITEM_FRAGMENT = 0;
    private static final int AVAILABILITY_FRAGMENT = 1;
    private static final int REQUESTS_FRAGMENT = 2;

    @BindView(R.id.vp_management)
    ViewPager _viewPager;
    @BindView(R.id.management_tabs)
    TabLayout _tabLayout;
    private ManagementFragmentsAdapter _viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        setTitle("Management");
        _viewPagerAdapter = new ManagementFragmentsAdapter(getFragmentManager(), this);
        _viewPager.setAdapter(_viewPagerAdapter);
        _tabLayout.setupWithViewPager(_viewPager);
    }

    public class ManagementFragmentsAdapter extends FragmentPagerAdapter
    {
        private final static int NUM_ITEMS = 3;
        private final ItemsChangedListener itemsListener;
        SparseArray<Fragment> registeredFragments = new SparseArray<>(NUM_ITEMS);

        public ManagementFragmentsAdapter(FragmentManager fragmentManager, ItemsChangedListener itemsListener) {
            super(fragmentManager);
            this.itemsListener = itemsListener;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            /**
             * Fragments need to have empty public ctor,
             * to allow the FragmentManger to kill and recreate Fragments with states.
             *
             * Convention is that every fragment has static newInstance() method for creation.
             */

            switch (position) {
                case ITEM_FRAGMENT:
                    return ManageItemFragment.newInstance();
                case AVAILABILITY_FRAGMENT:
                    return ManageAvailabilityFragment.newInstance();
                case REQUESTS_FRAGMENT:
                    return ManageRequestsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    @Override
    public void ItemsChanged(List<Item> items)
    {
        try
        {
            ((ManageAvailabilityFragment)_viewPagerAdapter.getRegisteredFragment(AVAILABILITY_FRAGMENT)).RefreshItems(items);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
