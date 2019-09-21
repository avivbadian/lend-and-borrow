package com.example.lendandborrowclient.Admins;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.ActivityCompat;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
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

        // Request for send SMS permission if not yet given
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    public class ManagementFragmentsAdapter extends FragmentPagerAdapter
    {
        private final static int NUM_ITEMS = 3;
        private final ItemsChangedListener listener;
        SparseArray<Fragment> registeredFragments = new SparseArray<>(NUM_ITEMS);

        public ManagementFragmentsAdapter(FragmentManager fragmentManager, ItemsChangedListener listener) {
            super(fragmentManager);
            this.listener = listener;
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
                    return ManageItemFragment.newInstance(listener);
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


    public void sendSMS(Borrow borrow, Item relatedItem, Availability relatedAvailability, boolean approved) {
        StringBuilder msg =
                new StringBuilder("HandyApp: dear ").append(borrow.First_name).append(" ")
                        .append(borrow.Last_name).append(", ").append(" your request to borrow the item: '")
                        .append(relatedItem.Title).append("' during: ")
                        .append(relatedAvailability.toString()).append(" has been ");
        if (approved) {
            msg = msg.append("approved.").append(" Please arrive ").append(borrow.Branch).append(" at the specified date to get your item.");
        } else {
            msg = msg.append("declined.");
        }
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(borrow.Phone,null,msg.toString(),null,null);
    }
}
