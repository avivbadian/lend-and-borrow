package com.example.lendandborrowclient.Admins;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lendandborrowclient.Admins.Listeners.ItemsChangedListener;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import com.example.lendandborrowclient.Validation.TextInputLayoutDataAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class ManageItemFragment extends Fragment implements Validator.ValidationListener
{
    private static final int IMAGE_PICK = 1;
    private List<Item> _itemsList;
    private static ItemsChangedListener _itemsChangedListener;

    // Expandable Views
    @BindView(R.id.expl_add_item)
    ExpandableLayout _addItemLayout;
    @BindView(R.id.expl_delete_item)
    ExpandableLayout _deleteItemLayout;

    // Layout fields
    @BindView(R.id.til_item_name) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _itemName;
    @BindView(R.id.til_category) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _category;
    @BindView(R.id.til_description) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _description;
    @BindView(R.id.sp_items)
    Spinner _itemsSpinner;
    @BindView(R.id.iv_choose_item_picture)
    ImageView _itemImage;

    private Validator _validator;
    private ArrayAdapter<Item> _itemsSpinnerAdapter;
    private StorageReference _imagesRef = FirebaseStorage.getInstance().getReference().child("Items");
    private Uri _currentUri;

    public static Fragment newInstance(ItemsChangedListener itemsChangedListener)
    {
        _itemsChangedListener = itemsChangedListener;
        return new ManageItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_manage_items, container, false);
        ButterKnife.bind(this, v);

        // Setting fields validator
        _validator = new Validator(this);
        _validator.setValidationListener(this);
        _validator.registerAdapter(TextInputLayout.class, new TextInputLayoutDataAdapter());

        LoadItems();

        return v;
    }

    public void LoadItems()
    {
        HandyServiceFactory.GetInstance().GetAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((items, throwable) -> {
                    // If everything is ok
                    if (throwable == null)
                    {
                        _itemsList = items;
                        _itemsSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
                        _itemsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        _itemsSpinner.setAdapter(_itemsSpinnerAdapter);
                    }
                });
    }

    @OnClick(R.id.iv_choose_item_picture)
    public void OnChooseItemImage()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            StartGallery();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        }
    }

    private void StartGallery()
    {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, IMAGE_PICK);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super method removed
        if (resultCode == RESULT_OK)
        {
            if (requestCode == IMAGE_PICK)
            {
                _currentUri = data.getData();
                Picasso.get().load(_currentUri).into(_itemImage);
                Toast.makeText(getContext(), "Image selected successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DeleteSelectedItem()
    {
        HandyServiceFactory.GetInstance().DeleteItem(((Item) _itemsSpinner.getSelectedItem()).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseBody, throwable) ->
                {
                    if (throwable != null)
                    {
                        Snackbar snack = Snackbar.make(getView(), R.string.failure_deleting_item, Snackbar.LENGTH_LONG);
                        snack.setAction(R.string.retry, v -> OnDeleteItemClicked());
                        snack.show();
                    }
                    else
                    {
                        Item selectedItem = ((Item) _itemsSpinner.getSelectedItem());

                        Snackbar.make(getView(),
                                String.format(getString(R.string.success_msg_delete_item), selectedItem.Title),
                                Snackbar.LENGTH_LONG).show();
                        ClearAll();

                        _itemsSpinnerAdapter.remove(selectedItem);
                        _itemsSpinnerAdapter.notifyDataSetChanged();
                        _itemsList.remove(selectedItem);
                        _itemsChangedListener.ItemsChanged(_itemsList);
                    }
                });
    }

    @OnClick(R.id.btn_delete_item)
    public void OnDeleteItemClicked()
    {
        if (_itemsSpinnerAdapter.getCount() == 0)
        {
            Snackbar.make(getView(), "No items to delete", Snackbar.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Delete");
        alert.setMessage(R.string.item_delete_confirm);
        alert.setPositiveButton("Yes", (dialog, which) ->
        {
            DeleteSelectedItem();
            dialog.dismiss();
        });

        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        alert.show();
    }

    @OnClick(R.id.btn_add_item)
    public void OnAddItemClicked()
    {
         //if (!_currentUri.equals(""))
            _validator.validate();
        // Snackbar.make(getView(), "Image did not upload yet", Snackbar.LENGTH_LONG).show();
    }

    /**
     * When validation on adding item fields passed
     */
    @Override
    public void onValidationSucceeded()
    {
        ClearErrorOnAllFields();

        AddItem();
    }

    private void AddItem()
    {
        Item newItem = CreateNewItem();

        HandyServiceFactory.GetInstance().AddItem(newItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((itemId, throwable) -> {

                    if (throwable != null)
                    {
                        Snackbar addItemSnack = Snackbar.make(getView(), R.string.failure_adding_item, Snackbar.LENGTH_LONG);
                        addItemSnack.setAction(R.string.retry, v -> OnAddItemClicked());
                        addItemSnack.show();
                    }
                    else
                    {
                        Snackbar.make(getView(), R.string.success_msg_add_item, Snackbar.LENGTH_LONG).show();
                        ClearAll();

                        // Add the new movie to all the lists and notify everyone for changes
                        newItem.Id = itemId;

                        SaveItemImage(itemId);

                        // Adding item to list = SPINNERS DATA so no need to add to adapter too.
                        _itemsList.add(newItem);

                        // Notify other fragments and ourselves
                        _itemsSpinnerAdapter.notifyDataSetChanged();
                        _itemsChangedListener.ItemsChanged(_itemsList);
                    }
                });
    }

    private void SaveItemImage(int itemId) {
        StorageReference imagePath = _imagesRef.child(itemId + ".jpg");
        imagePath.putFile(_currentUri).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String err = Objects.requireNonNull(task.getException()).toString();
                Toast.makeText(getContext(), "Failed saving item image. Error: " + err, Toast.LENGTH_SHORT).show();
            } else {
                // turns image back to default icon
                _itemImage.setImageResource(R.drawable.ic_add_photo);
            }
        });
    }

    private Item CreateNewItem()
    {
        Item newItem = new Item();
        newItem.Title =_itemName.getEditText().getText().toString();
        newItem.Description = _description.getEditText().getText().toString();
        newItem.Category = _category.getEditText().getText().toString();
        return newItem;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        ClearErrorOnAllFields();

        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages
            if (view instanceof TextInputLayout)
                ((TextInputLayout) view).setError(message);
            else if (view instanceof TextView)
                ((TextView) view).setText(message);
        }

        Snackbar.make(getView(), R.string.illegal_fields, Snackbar.LENGTH_LONG).show();
    }

    private void ClearErrorOnAllFields()
    {
        _itemName.setError(null);
        _category.setError(null);
        _description.setError(null);

        _itemName.setErrorEnabled(false);
        _category.setErrorEnabled(false);
        _description.setErrorEnabled(false);
    }

    private void ClearAll()
    {
        // Clear fields
        _itemName.getEditText().setText("");
        _category.getEditText().setText("");
        _description.getEditText().setText("");

        // Lose focus from everything
        getActivity().getCurrentFocus().clearFocus();
    }

    @OnClick(R.id.btn_toggle)
    public void OnToggleButtonClicked()
    {
        _addItemLayout.toggle();
        _deleteItemLayout.toggle();
    }
}


