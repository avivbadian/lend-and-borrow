package com.example.lendandborrowclient;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.lendandborrowclient.NotificationListeners.AvailabilitiesChangedListener;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.HandyRestApiBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.joda.time.DateTimeComparator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class BorrowRequestFragment extends Fragment implements AvailabilitiesChangedListener
{
    @BindView(R.id.iv_item_image) ImageView _itemImage;
    @BindView(R.id.tv_item_title) TextView _itemTitle;
    @BindView(R.id.tv_item_category) TextView _itemCategory;
    @BindView(R.id.tv_item_description) TextView _itemDescription;
    @BindView(R.id.tv_selected_availability) TextView _selectedAvailabilityView;
    @BindView(R.id.sp_branch_to_borrow) Spinner _branchesSpinner;

    private ArrayAdapter<Branch> _branchesSpinnerAdapter;
    private Unbinder _unbinder;
    private Item _displayedItem;
    private List<Availability> _allAvailabilities;
    private List<Branch> _allBranches;
    private Availability _selectedAvailability;
    private Branch _selectedBranch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Enable back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_borrow_request, container, false);
        _unbinder = ButterKnife.bind(this, v);
        // Set scrollable text view in case of overflows
        _itemDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        _displayedItem = ((MainActivity)getActivity()).GetSelectedItem();
        PopulateFields();
        LoadBranches();
        LoadAvailabilities();
    }

    private void LoadBranches() {
        HandyRestApiBuilder.GetInstance().GetBranches().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((branches, throwable) ->
                {
                    // Getting the branches from the server
                    if (branches != null) {
                        // No need for special adapter as we only display the toString() of every branch in the spinner.
                        // (Adapter = bridge between the UI components and the data source that fill data into the UI Component)
                        _allBranches = branches;
                        _branchesSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                                _allBranches);
                        _branchesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        _branchesSpinner.setAdapter(_branchesSpinnerAdapter);
                    } else
                        Toast.makeText(getContext(), "Failed Loading branches", Toast.LENGTH_SHORT).show();
                });
    }

    private void LoadAvailabilities()
    {
        HandyRestApiBuilder.GetInstance().GetItemAvailabilities(_displayedItem.Id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((availabilities, throwable) ->
                {
                    if (availabilities != null)
                        _allAvailabilities = availabilities;
                    else
                        Toast.makeText(getContext(), "Failed Loading availabilities", Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick(R.id.btn_date)
    void OnDatePickerButtonClicked() {
        Calendar now = Calendar.getInstance();

        // Using a date picker dialog which enables setting selectable dates.
        DatePickerDialog initialDatePickerDialog = DatePickerDialog.newInstance(new initialDatePicker(),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        // Initializing the datepicker dialog only in case there are availabilities for that item
        if (_allAvailabilities != null && _allAvailabilities.size() != 0)
        {
            Calendar[] selectableDays = _allAvailabilities.stream().map(
                    availability -> toCalendar(availability.Start_date)).toArray(Calendar[]::new);
            initialDatePickerDialog.setSelectableDays(selectableDays);
            initialDatePickerDialog.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
        }

        else {
            Log.d("Availabilities", "availabilities were not found");
            Snackbar.make(getView(), "No available dates", Snackbar.LENGTH_LONG).show();
        }
    }

    private void PopulateFields()
    {
        // display fields in the UI
        _selectedAvailability = new Availability();
        _itemTitle.setText(_displayedItem.Title);
        _itemCategory.setText(String.format(getString(R.string.categories), _displayedItem.Category));
        _itemDescription.setText(String.format(getString(R.string.description_format), _displayedItem.Description));
        // Load image with Glide
        Glide.with(this).load(_displayedItem.Path).into(_itemImage);
    }

    @Override
    public void AvailabilitiesChanged(int itemId) {
        if (itemId == _displayedItem.Id)
            LoadAvailabilities();
    }


    class initialDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            // Converting the selected date into the java.util.Date object
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            Date date = new Date(cur.getTimeInMillis());
            DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

            // Getting the fitting availability with the selected date as the start date
            _selectedAvailability = _allAvailabilities.stream().filter((availability) ->
                comparator.compare(availability.Start_date, date) == 0
            ).findFirst().orElse(null);

            if (_selectedAvailability != null)
                _selectedAvailabilityView.setText(_selectedAvailability.toString());
            else {
                Toast.makeText(getContext(), "An error occurred during dates selection. Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        MenuItem nextItem = menu.findItem(R.id.next_action);

        // Next is visible, Search is invisible in this fragment
        nextItem.setVisible(true);
        searchItem.setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                // Tell the activity back was pressed
                getActivity().onBackPressed();
                return true;
            }
            case R.id.next_action:
            {
                // If the user selected availability and branch, route him to the next fragment
                if (_selectedAvailability.Id != 0) {
                    _selectedBranch = (Branch)_branchesSpinner.getSelectedItem();
                    if (_selectedBranch == null || _selectedBranch.Title.equals("")) {
                        Toast.makeText(getContext(), "Please choose a branch", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    try  {
                        ((MainActivity) getActivity()).ShowBorrowConfirmDialog(_selectedBranch, _selectedAvailability);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else
                    Toast.makeText(getContext(), R.string.request_fill_all_fields, Toast.LENGTH_LONG).show();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onDestroyView() {
        _unbinder.unbind();
        super.onDestroyView();
    }
}
