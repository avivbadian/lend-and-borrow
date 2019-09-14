package com.example.lendandborrowclient.Admins;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import com.example.lendandborrowclient.Validation.TextInputLayoutDataAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManageAvailabilityFragment extends Fragment implements Validator.ValidationListener
{
    // Members
    private Validator _validator;
    private List<Item> _itemsList;
    private List<Availability> _availabilitiesForMovie;
    private ArrayAdapter<Item> _itemsSpinnerAdapter;
    private Date _initialDate;
    private Date _endDate;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    // Expandable Views
    @BindView(R.id.expl_add_availability)
    ExpandableLayout _addAvailabilityLayout;
    @BindView(R.id.expl_delete_availability)
    ExpandableLayout _deleteAvailabilityLayout;

    // Views
    @BindView(R.id.sp_items)
    Spinner _itemsSpinner;
    @BindView(R.id.sp_availabilities)
    Spinner _availabilitiesSpinner;
    @BindView(R.id.til_start_date) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _availabilityStartDateView;
    @BindView(R.id.til_end_date) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _availabilityEndDateView;
    private ArrayAdapter<Availability> _availabilitiesSpinnerAdapter;

    public static Fragment newInstance()
    {
        return new ManageAvailabilityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_manage_availabilites, container, false);
        ButterKnife.bind(this, v);

        // Setting fields validator
        _validator = new Validator(this);
        _validator.setValidationListener(this);
        _validator.registerAdapter(TextInputLayout.class, new TextInputLayoutDataAdapter());

        _itemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                OnDeleteItemAvailability();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        LoadValuesFromServer();

        return v;
    }

    private void LoadValuesFromServer()
    {
        HandyServiceFactory.GetInstance().GetAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((items, throwable) -> {
                    // no errors
                    if (throwable == null)
                    {
                        _itemsList = items;
                        _itemsSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _itemsList);
                        _itemsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        _itemsSpinner.setAdapter(_itemsSpinnerAdapter);
                    }
                });
    }

    @OnFocusChange(R.id.tiet_start_date)
    public void OnSelectAvailabilityStartDateFocused(View v, boolean focusGained)
    {
        if (!focusGained)
            return;

        // Prevent keyboard from appearing since we show a date dialog
        ((EditText)v).setShowSoftInputOnFocus(false);

        Calendar now = Calendar.getInstance();

        DatePickerDialog initialDatePickerDialog =
                DatePickerDialog.newInstance(new initialDatePicker(),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        initialDatePickerDialog.show(getFragmentManager(), "initial date picker");
    }

    public void RefreshItems(List<Item> items) {
        _itemsSpinnerAdapter.clear();
        _itemsSpinnerAdapter.addAll(items);
    }

    class initialDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            _initialDate = new Date(cur.getTimeInMillis());
            _availabilityStartDateView.getEditText().setText(dateFormatter.format(_initialDate));
        }
    }

    @OnFocusChange(R.id.tiet_end_date)
    public void OnSelectAvailabilityEndDateFocused(View v, boolean focusGained)
    {
        if (!focusGained)
            return;

        // Prevent keyboard from appearing since we show a date dialog
        ((EditText)v).setShowSoftInputOnFocus(false);

        Calendar now = Calendar.getInstance();

        DatePickerDialog endDatePickerDialog =
                DatePickerDialog.newInstance(new endDatePicker(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog.show(getFragmentManager(), "end date picker");
    }

    class endDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            _endDate = new Date(cur.getTimeInMillis());
            _availabilityEndDateView.getEditText().setText(dateFormatter.format(_endDate));
        }
    }

    @OnClick(R.id.btn_add_availability)
    public void OnAddAvailabilityClicked()
    {
        _validator.validate();
    }

    @OnClick(R.id.btn_delete_availability)
    public void OnDeleteAvailabilityClicked()
    {
        if (_availabilitiesSpinner.getAdapter().getCount() == 0)
        {
            Snackbar.make(getView(), "No availabilities to delete", Snackbar.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Delete");
        alert.setMessage(R.string.availability_delete_confirmation);
        alert.setPositiveButton("Yes", (dialog, which) ->
        {
            DeleteSelectedAvailability();
            dialog.dismiss();
        });

        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        alert.show();
    }

    private void DeleteSelectedAvailability()
    {
        HandyServiceFactory.GetInstance().DeleteAvailability(((Availability) _availabilitiesSpinner.getSelectedItem()).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseBody, throwable) ->
                {
                    if (throwable != null)
                    {
                        Snackbar snack = Snackbar.make(getView(), R.string.failure_deleting_availability, Snackbar.LENGTH_LONG);
                        snack.setAction(R.string.retry, v -> OnDeleteAvailabilityClicked());
                        snack.show();
                    }
                    else
                    {
                        Snackbar.make(getView(), R.string.operation_success_delete_availability, Snackbar.LENGTH_LONG).show();
                        ClearAll();

                        _availabilitiesSpinnerAdapter.remove((Availability) _availabilitiesSpinner.getSelectedItem());
                        _availabilitiesSpinnerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void OnDeleteItemAvailability()
    {
        HandyServiceFactory.GetInstance().GetItemAvailabilities(((Item) _itemsSpinner.getSelectedItem()).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((availabilities, throwable) -> {
                    // If everything is ok
                    if (throwable == null)
                    {
                        _availabilitiesForMovie = availabilities;
                        _availabilitiesSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _availabilitiesForMovie);
                        _availabilitiesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        _availabilitiesSpinner.setAdapter(_availabilitiesSpinnerAdapter);
                    }
                });
    }

    @Override
    public void onValidationSucceeded()
    {
        AddAvailability();
    }

    private void AddAvailability()
    {
        Availability Availability = ConstructAvailabilityObject();

        HandyServiceFactory.GetInstance().AddAvailability(Availability)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((s, throwable) -> {
                    if (throwable != null)
                    {
                        Snackbar addAvailabilitySnack = Snackbar.make(getView(), R.string.failure_adding_availability, Snackbar.LENGTH_LONG);
                        addAvailabilitySnack.setAction(R.string.retry, v -> OnAddAvailabilityClicked());
                        addAvailabilitySnack.show();
                    }
                    else
                    {
                        Snackbar.make(getView(), R.string.operation_success_add_availability, Snackbar.LENGTH_LONG).show();
                        ClearAll();
                    }
                });
    }

    private void ClearErrorOnAllFields()
    {
        _availabilityStartDateView.setError(null);
        _availabilityEndDateView.setError(null);
        _availabilityStartDateView.setErrorEnabled(false);
        _availabilityEndDateView.setErrorEnabled(false);
    }

    private void ClearAll()
    {
        _availabilityStartDateView.getEditText().setText("");
        _availabilityEndDateView.getEditText().setText("");

        // Lose focus from everything
        getActivity().getCurrentFocus().clearFocus();
    }

    private Availability ConstructAvailabilityObject()
    {
        Availability newAvailability = new Availability();
        newAvailability.Start_date = _initialDate;
        newAvailability.End_date = _endDate;
        newAvailability.Item_id = ((Item)_itemsSpinner.getSelectedItem()).Id;
        return newAvailability;
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

    @OnClick(R.id.btn_toggle)
    public void OnToggleButtonClicked()
    {
        _addAvailabilityLayout.toggle();
        _deleteAvailabilityLayout.toggle();
    }
}

