package com.example.lendandborrowclient;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.LoansHttpService;
import com.example.lendandborrowclient.utils.DividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {


    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_items_view)
    TextView noItemsView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<Item> itemsList = new ArrayList<>();
    private ItemsAdapter mAdapter;
    private String userName;


    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_items, container, false);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userName = settings.getString("username", "");

        LoansHttpService.GetInstance().GetUserItems(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<Item>, Throwable>() {
                    @Override
                    public void accept(List<Item> items, Throwable throwable) {
                        itemsList = items;
                        mAdapter = new ItemsAdapter(getContext(), itemsList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
                        recyclerView.setAdapter(mAdapter);
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog(false, null, -1);
            }
        });

        return view;
    }


    /**
     * Inserting new item in db
     * and refreshing the list
     */
    private void createItem(String itemName, String itemDesc, String imgPath) {
        // inserting item in db and getting
        // newly inserted item id

        final Item i = new Item();
        i.Description = itemDesc;
        i.ImagePath= imgPath;
        i.Name = itemName;
        i.Owner = userName;


        LoansHttpService.GetInstance().AddItem(i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Integer, Throwable>() {
                    @Override
                    public void accept(Integer insertedId, Throwable throwable) {
                        i.Id = insertedId;
                        itemsList.add(0, i);
                        mAdapter.notifyDataSetChanged();
                        toggleEmptyItems();
                    }
                });
    }

    /**
     * Updating item in db and updating
     * item in the list by its position
     */
    private void updateItem(String description, final String name, final String imgPath, final int position) {


        // deleting the item from db
        LoansHttpService.GetInstance().UpdateItem(itemsList.get(position).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean success, Throwable throwable) {

                        Item i = itemsList.get(position);
                        Item updated = new Item();
                        updated.Id = i.Id;
                        updated.ImagePath = imgPath;
                        updated.Name = name;
                        itemsList.set(position, updated);
                        mAdapter.notifyItemChanged(position);
                        toggleEmptyItems();
                    }
                });
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showItemDialog(true, itemsList.get(position), position);
                } else {
                    deleteItem(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Deleting item from Db and removing the
     * item from the list by its position
     */
    private void deleteItem(final int position) {

        // deleting the item from db
        LoansHttpService.GetInstance().DeleteItem(itemsList.get(position).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean success, Throwable throwable) {

                        itemsList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        toggleEmptyItems();
                    }
                });
    }

    /**
     * Shows alert dialog with EditText options to enter / edit item.
     * when shouldUpdate=true, it automatically displays old item and changes the
     * button text to UPDATE
     */
    private void showItemDialog(final boolean shouldUpdate, final Item itemPreview, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity().getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.item_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputItemName = view.findViewById(R.id.dialog_item_name);
        final EditText inputItemDes = view.findViewById(R.id.item_description);
        final EditText inputItemPicPath = view.findViewById(R.id.tv_imageName);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_item_title) : getString(R.string.lbl_edit_item_title));

        if (shouldUpdate && itemPreview != null) {
            inputItemName.setText(itemPreview.Name);
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputItemName.getText().toString()) ||
                        TextUtils.isEmpty(inputItemDes.getText().toString())) {
                    Toast.makeText(getContext(), "Fill fields please!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && itemPreview != null) {
                    // update note by it's id
                    updateItem(inputItemName.getText().toString(), inputItemDes.getText().toString(), inputItemPicPath.getText().toString(), position);
                } else {
                    // create new note
                    createItem(inputItemName.getText().toString(), inputItemDes.getText().toString(), inputItemPicPath.getText().toString());
                }
            }
        });
    }


    /**
     * Toggling list and empty items view
     */
    private void toggleEmptyItems() {
        if (itemsList.size() > 0) {
            noItemsView.setVisibility(View.GONE);
        } else {
            noItemsView.setVisibility(View.VISIBLE);
        }
    }

}
