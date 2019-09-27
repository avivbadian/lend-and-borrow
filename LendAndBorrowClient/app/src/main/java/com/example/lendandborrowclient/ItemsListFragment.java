package com.example.lendandborrowclient;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.lendandborrowclient.ListAdapters.ItemClickedListener;
import com.example.lendandborrowclient.ListAdapters.ItemsListAdapter;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ItemsListFragment extends Fragment implements ItemClickedListener
{
    // Views
    @BindView(R.id.pb_items) ProgressBar _progressBar;
    @BindView(R.id.rv_items) RecyclerView m_itemsListRecyclerView;
    @BindView(R.id.pullToRefresh) SwipeRefreshLayout swipeRefreshLayout;

    // Variables
    private List<Item> _itemDisplays;
    private ItemsListAdapter _itemsAdapter;

    /** because a Fragment may continue to exist after its Views are destroyed,
     *  we manually call .unbind() from fragments to release reference to Views (and allow associated memory to be reclaimed)*/
    private Unbinder _unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View v = inflater.inflate(R.layout.fragment_items_list, container, false);
        _unbinder = ButterKnife.bind(this, v);
        _itemsAdapter = new ItemsListAdapter(this, getContext());
        m_itemsListRecyclerView.setAdapter(_itemsAdapter);
        m_itemsListRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 4));
        m_itemsListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            LoadItemsList(true);
            swipeRefreshLayout.setRefreshing(false);
        });

        LoadItemsList();

        return v;
    }

    public void LoadItemsList()
    {
        LoadItemsList(false);
    }

    public void LoadItemsList(boolean forceLoad)
    {
        if (forceLoad || _itemDisplays == null)
            HandyServiceFactory.GetInstance().GetAllItems()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(
                            disposable ->
                            {
                                _progressBar.setVisibility(View.VISIBLE);
                                m_itemsListRecyclerView.setVisibility(View.GONE);
                            }
                    ).subscribe((items, throwable) ->
            {
                _progressBar.setVisibility(View.GONE);
                m_itemsListRecyclerView.setVisibility(View.VISIBLE);

                if (items != null)
                {
                    _itemDisplays = items;
                    _itemsAdapter.SetData(_itemDisplays);
                }
                else
                {
                    Log.d("Items", "Error retrieving the items");

                    Snackbar.make(getView(),
                            "Failed Loading Items", Toast.LENGTH_SHORT)
                            .setAction(R.string.retry, v -> LoadItemsList(forceLoad))
                            .show();
                }
            });
        else
            _itemsAdapter.SetData(_itemDisplays);
    }

    @Override
    public void OnItemClicked(Item item) {
        ((com.example.lendandborrowclient.MainActivity)getActivity()).ShowSelectAvailabilityFragment(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        //inflater.inflate(R.menu.menu_ticket_order_process,menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                _itemsAdapter.FilterDataByItemName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (TextUtils.isEmpty(newText)){
                    _itemsAdapter.ClearFilteredData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        MenuItem nextItem = menu.findItem(R.id.next_action);

        nextItem.setVisible(false);
        searchItem.setVisible(true);

        super.onPrepareOptionsMenu(menu);
    }
}
