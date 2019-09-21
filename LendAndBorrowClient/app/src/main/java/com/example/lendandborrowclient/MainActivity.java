package com.example.lendandborrowclient;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;

public class MainActivity extends AppCompatActivity {
    private Item _selectedItem;
    private Availability _selectedAvailability;
    private Branch _selectedBranch;

    // TODO : Save Fragments here so we can HIDE/SHOW them on back
    // TODO : Change transaction to hide previous, add new and commit instead of current replace

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ShowItemsListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_page,menu);

        // Create search bar in the action bar
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView  = (SearchView) searchItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.CENTER));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null != searchManager )
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                // Let the child fragment on focus to handle this!!!
                return false;
            }
            case R.id.admin_action:
            {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            // TODO : Ask the current fragment to handle the back press and then pop
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    // TODO: set here another function like backpressed but one that doesnt get called by the back button, call this when we really wanna go back.

    public void CloseAllFragments()
    {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void ShowItemsListFragment()
    {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ItemsListFragment(), "ItemsListFragment")
                /*.addToBackStack(null)*/.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void ShowSelectAvailabilityFragment(Item item)
    {
        _selectedItem = item;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new BorrowRequestFragment())
                .addToBackStack(null).commit();
    }

    public void ShowBorrowConfirmDialog(Branch selectedBranch, Availability selectedAvailability) {
        _selectedBranch = selectedBranch;
        _selectedAvailability = selectedAvailability;

        BorrowConfirmFragment frag = new BorrowConfirmFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null).commit();
    }

    public Item GetSelectedItem(){
        return _selectedItem;
    }

    public Availability GetSelectedAvailability(){
        return _selectedAvailability;
    }

    public Branch GetSelectedBranch() {
        return _selectedBranch;
    }
}
