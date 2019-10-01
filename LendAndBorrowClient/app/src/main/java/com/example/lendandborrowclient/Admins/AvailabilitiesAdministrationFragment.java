package com.example.lendandborrowclient.Admins;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyRestApiBuilder;
import com.savvi.rangedatepicker.CalendarPickerView;
import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AvailabilitiesAdministrationFragment extends Fragment
{
    // Members
    private List<Item> _itemsList;
    private List<Availability> _availabilitiesOfItem;
    private ArrayAdapter<Item> _itemsSpinnerAdapter;
    private ArrayAdapter<Availability> _availabilitiesSpinnerAdapter;
    private Unbinder _unbinder;

    // Expandable Views
    @BindView(R.id.expl_add_availability)
    ExpandableLayout _addAvailabilityLayout;
    @BindView(R.id.expl_delete_availability)
    ExpandableLayout _deleteAvailabilityLayout;
    // Views
    @BindView(R.id.calendar_view)
    CalendarPickerView calendarPickerView;
    @BindView(R.id.sp_items_for_del)
    Spinner _itemsSpinnerForDel;
    @BindView(R.id.sp_items_for_add)
    Spinner _itemsSpinnerForAdd;
    @BindView(R.id.sp_availabilities_for_del)
    Spinner _availabilitiesSpinner;
    @BindView(R.id.btn_delete_availability)
    Button _deleteAvailabilityButton;

    static AvailabilitiesAdministrationFragment newInstance() {
        return new AvailabilitiesAdministrationFragment();
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
        View v = inflater.inflate(R.layout.fragment_availabilites_administration, container, false);
        _unbinder = ButterKnife.bind(this, v);
        calendarPickerView.setVisibility(View.GONE);
        _availabilitiesOfItem = new ArrayList<>();
        _availabilitiesSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _availabilitiesOfItem);
        _availabilitiesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _availabilitiesSpinner.setAdapter(_availabilitiesSpinnerAdapter);


        _itemsSpinnerForAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                OnItemSelected(((Item)_itemsSpinnerForDel.getSelectedItem()).Id, true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            { }
        });

        _itemsSpinnerForDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                OnItemSelected(((Item)_itemsSpinnerForDel.getSelectedItem()).Id, false);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            { }
        });

        _availabilitiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _deleteAvailabilityButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LoadValuesFromServer();
        return v;
    }

    private void LoadValuesFromServer()
    {
        HandyRestApiBuilder.GetInstance().GetAllItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((items, throwable) -> {
                    // no errors
                    if (throwable == null)
                    {
                        _itemsList = items;
                        _itemsSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, _itemsList);
                        _itemsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        _itemsSpinnerForAdd.setAdapter(_itemsSpinnerAdapter);
                        _itemsSpinnerForDel.setAdapter(_itemsSpinnerAdapter);
                    }
                });
    }

    private void setAvailabilitiesPicker()
    {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTime(new Date());
        maxDate.add(Calendar.YEAR, 1);

        ArrayList<Date> occupiedDates = new ArrayList<>();
        for (Availability a : _availabilitiesOfItem) {
            Date initial = a.Start_date;
            while (initial.before(a.End_date)) {
                Calendar cur = toCalendar(initial);
                occupiedDates.add(cur.getTime());
                cur.add(Calendar.DATE, 1);
                initial = cur.getTime();
            }

            Calendar cur = toCalendar(initial);
            occupiedDates.add(cur.getTime());
        }

        calendarPickerView.init(minDate.getTime(), maxDate.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                // highlight dates in red color, mean they are already used.
                .withHighlightedDates(occupiedDates);


        calendarPickerView.setVisibility(View.VISIBLE);
    }

    void RefreshItems(List<Item> items) {
        _itemsSpinnerAdapter.clear();
        _itemsSpinnerAdapter.addAll(items);
    }


    @OnClick(R.id.btn_add_availability)
    void OnAddAvailabilityClicked()
    {
        Availability availability = ConstructAvailabilityObject();

        if (availability == null) {
            Snackbar.make(getView(), "Please select range of dates", Snackbar.LENGTH_LONG).show();
            return;
        }

        HandyRestApiBuilder.GetInstance().AddAvailability(availability)
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
                        calendarPickerView.highlightDates(calendarPickerView.getSelectedDates());
                        calendarPickerView.clearSelectedDates();
                    }
                });
    }

    @OnClick(R.id.btn_delete_availability)
    void OnDeleteAvailabilityClicked()
    {
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
        HandyRestApiBuilder.GetInstance().DeleteAvailability(((Availability) _availabilitiesSpinner.getSelectedItem()).Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((requestsToDecline, throwable) ->
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
                        Availability deletedAvailability = (Availability) _availabilitiesSpinner.getSelectedItem();

                        // Declining related requests
                        for (Borrow req : requestsToDecline){
                            ((AdminManagementActivity)getActivity()).sendSMS(req,(Item) _itemsSpinnerForAdd.getSelectedItem(),
                                    deletedAvailability, false);
                        }

                        _availabilitiesSpinnerAdapter.remove(deletedAvailability);
                        _availabilitiesSpinnerAdapter.notifyDataSetChanged();
                        ((AdminManagementActivity)getActivity()).AvailabilityDeleted();
                    }
                });
    }

    private void OnItemSelected(int itemId, boolean add)
    {
        HandyRestApiBuilder.GetInstance().GetItemFullAvailabilities(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((availabilities, throwable) -> {
                    // If everything is ok
                    if (throwable == null)
                    {
                        _availabilitiesOfItem = availabilities;
                        if (_availabilitiesOfItem.size() > 0) {
                            _deleteAvailabilityButton.setEnabled(true);
                            _availabilitiesSpinner.setEnabled(true);
                        } else {
                            _availabilitiesSpinner.setEnabled(false);
                            _deleteAvailabilityButton.setEnabled(false);
                        }

                        if (add) {
                            setAvailabilitiesPicker();
                        } else {
                            _availabilitiesSpinnerAdapter.clear();
                            _availabilitiesSpinnerAdapter.addAll(_availabilitiesOfItem);
                            _availabilitiesSpinnerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Snackbar.make(getView(), "Failed loading item availabilities", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private Availability ConstructAvailabilityObject()
    {
        Availability newAvailability = new Availability();
        List<Date> selectedDates = calendarPickerView.getSelectedDates();
        if (selectedDates.size() >= 2) {
            newAvailability.Start_date = selectedDates.get(0);
            newAvailability.End_date = selectedDates.get(selectedDates.size()-1);
            newAvailability.Item_id = ((Item)_itemsSpinnerForAdd.getSelectedItem()).Id;
            return newAvailability;
        }
        return null;
    }

    private static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @OnClick(R.id.btn_toggle)
    void OnToggleButtonClicked()
    {
        _addAvailabilityLayout.toggle();
        _deleteAvailabilityLayout.toggle();
    }

    @Override
    public void onDestroyView() {
        _unbinder.unbind();
        super.onDestroyView();
    }
}

