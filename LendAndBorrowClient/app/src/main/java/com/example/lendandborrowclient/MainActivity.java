package com.example.lendandborrowclient;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayItemsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);

        // Create search bar in the action bar
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView  = (SearchView) searchItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.CENTER));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home: {
                // child fragment on focus handles this
                return false;
            }
            case R.id.admin_action: {
                Intent intent = new Intent(this, AdminLoginActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.about_action: {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Handy App");
                alertDialog.setMessage("Handy App was developed by Aviv Badian and Yaniv Krim.\n" +
                        "It is a platform that supplies borrowing services to multiple clients and managing tools for administrators.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void CloseAllFragments()
    {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void ShowSelectAvailabilityFragment(Item item)
    {
        _selectedItem = item;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new BorrowRequestFragment())
                .addToBackStack(null).commit();
    }

    public void ShowBorrowConfirmDialog(Branch selectedBranch, Availability selectedAvailability) {
        _selectedBranch = selectedBranch;
        _selectedAvailability = selectedAvailability;

        BorrowConfirmFragment frag = new BorrowConfirmFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null).commit();
    }

    public void DisplayItemsFragment()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ItemsDisplayFragment(), "ItemsDisplayFragment")
                .commit();
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
