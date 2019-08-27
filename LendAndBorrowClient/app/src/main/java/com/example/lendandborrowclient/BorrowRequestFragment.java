package com.example.lendandborrowclient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nimrod on 24/06/2017.
 */

public class AvailabilitySelectionFragment extends Fragment
{
    Item m_displayedItem;

    List<Availability> _allAvailabilities;
    List<Availability> _selectedDayPossibleAvailabilities;
    List<Branch> _allBranches;
    Branch _selectedBranch;

    Availability _selectedAvailability;
    MonthAdapter.CalendarDay _selectedDay;
    DatePickerDialog initialDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    ArrayAdapter<Branch> _branchesSpinnerAdapter;

    @BindView(R.id.iv_item_image) ImageView _itemImage;
    @BindView(R.id.tv_item_title) TextView _itemTitle;
    @BindView(R.id.tv_item_category) TextView _itemCategory;
    @BindView(R.id.tv_item_description) TextView _itemDescription;
    @BindView(R.id.tv_selected_availability) TextView _selectedAvailabilityView;
    @BindView(R.id.sp_branch_to_borrow) Spinner _branchesSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_availability_selection, container, false);
        ButterKnife.bind(this, v);

        _itemDescription.setMovementMethod(ScrollingMovementMethod.getInstance());

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        m_displayedItem = ((MainActivity)getActivity()).getSelectedItem();
//        m_savedImageBitmap = ((MainActivity)getActivity()).getSelectedMovieImage();

        PopulateFields();
        LoadBranches();
        LoadAvailabilities();
    }

    private void LoadBranches() {
        HandyServiceFactory.GetInstance().GetBranches().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((branches, throwable) ->
                {
                    if (branches != null) {
                        _allBranches = branches;
                        _branchesSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, branches);
                        _branchesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        _branchesSpinner.setAdapter(_branchesSpinnerAdapter);
                        _branchesSpinner.setOnItemClickListener((adapterView, view, i, l) -> {
                            _selectedBranch = (Branch) _branchesSpinner.getSelectedItem();
                            _selectedAvailability.Branch = _selectedBranch.Id;
                        });
                    }
                    else
                        Toast.makeText(getContext(), "Failed Loading branches", Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick(R.id.btn_date)
    void OnDatePickerButtonClicked()
    {
         _allAvailabilities.sort((a1, a2) -> a1.Start_date.compareTo(a2.Start_date));
         Calendar minCal = toCalendar( _allAvailabilities.get(0).Start_date);

         if (minCal.before(Calendar.getInstance())) {
             minCal = Calendar.getInstance();
         }

        initialDatePickerDialog = DatePickerDialog.newInstance(new initialDatePicker(),
                minCal.get(Calendar.YEAR),
                minCal.get(Calendar.MONTH),
                minCal.get(Calendar.DAY_OF_MONTH));

        _allAvailabilities.sort((a1, a2) -> a1.End_date.compareTo(a2.End_date));
        initialDatePickerDialog.setMaxDate(toCalendar( _allAvailabilities.get(_allAvailabilities.size()-1).End_date));

        if (_allAvailabilities != null && _allAvailabilities.size() != 0)
        {
            initialDatePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }
        else
        {
            Log.d("Availabilities", "availabilities were not found while trying to init date picker");
            Snackbar.make(getView(), "No available dates", Snackbar.LENGTH_LONG).show();
        }
    }

    private void LoadAvailabilities()
    {
        HandyServiceFactory.GetInstance().GetItemAvailabilities(m_displayedItem.Id, true)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((availabilities, throwable) ->
                {
                    if (availabilities != null)
                        _allAvailabilities = availabilities;
                    else
                        Toast.makeText(getContext(), "Failed Loading availabilities", Toast.LENGTH_SHORT).show();
                });
    }

    private void PopulateFields()
    {
        _itemTitle.setText(m_displayedItem.Title);
        _itemCategory.setText(String.format(getString(R.string.genres), m_displayedItem.Category));
        Picasso.get().load(FirebaseStorage.getInstance().getReference().child("Items images").child(m_displayedItem.Id + ".jpg").getPath()).into(_itemImage);
        _itemDescription.setText(String.format(getString(R.string.description_format), m_displayedItem.Description));
        _selectedAvailability = new Availability();

        //_itemYear.setText(String.format("%s %s", getString(R.string.release_date), m_displayedMovie.ReleaseDate.toString(getString(R.string.release_date_time_pattern))));
        //_itemImage.setImageBitmap(m_savedImageBitmap);
        //_itemDirector.setText(String.format(getString(R.string.director), m_displayedMovie.Director));
        // _itemDuration.setText(String.format(getString(R.string.duration), String.valueOf(m_displayedMovie.Duration)));
    }


    class initialDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            _selectedAvailability.Start_date = new java.sql.Date(cur.getTimeInMillis());
            endDatePickerDialog = DatePickerDialog.newInstance(new endDatePicker(),
                    year,
                    monthOfYear,
                    dayOfMonth);

            _allAvailabilities.sort((a1,a2) -> a1.End_date.compareTo(a2.End_date));
            endDatePickerDialog.setMaxDate(toCalendar(_allAvailabilities.get(_allAvailabilities.size()-1).End_date));
            endDatePickerDialog.show(getFragmentManager(), "Datepickeridialog");
        }
    }

    class endDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            _selectedAvailability.End_date = new java.sql.Date(cur.getTimeInMillis());
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        MenuItem nextItem = menu.findItem(R.id.next_action);

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
                if (_selectedAvailability != null) {

                    if (_selectedAvailability.Branch == 0) {
                        Toast.makeText(getContext(), "Please choose a branch", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    try  {
                        HandyServiceFactory.GetInstance().Borrow(new Borrow())
                        ((MainActivity) getActivity()).ShowPurchaseDetailsFragment(selectedSeats, selectionId);
                    } catch (Exception e)                    {
                        e.printStackTrace();
                    }
                }

                else
                    Toast.makeText(getContext(), R.string.screening_wasnt_selected, Toast.LENGTH_LONG).show();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
