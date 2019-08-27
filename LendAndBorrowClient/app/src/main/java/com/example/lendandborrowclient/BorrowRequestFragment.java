package com.example.lendandborrowclient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import com.example.lendandborrowclient.MainActivity;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class BorrowRequestFragment extends Fragment
{
    Item m_displayedItem;

    List<Availability> _allAvailabilities;
    List<Branch> _allBranches;

    private Branch _selectedBranch;
    private Availability _selectedAvailability;

    DatePickerDialog initialDatePickerDialog;
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
        View v = inflater.inflate(R.layout.fragment_borrow_request, container, false);
        ButterKnife.bind(this, v);

        _itemDescription.setMovementMethod(ScrollingMovementMethod.getInstance());

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        m_displayedItem = ((MainActivity)getActivity()).GetSelectedItem();
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
                        });
                    }
                    else
                        Toast.makeText(getContext(), "Failed Loading branches", Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick(R.id.btn_date)
    void OnDatePickerButtonClicked()
    {
        Calendar now = Calendar.getInstance();

        initialDatePickerDialog = DatePickerDialog.newInstance(new initialDatePicker(),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        initialDatePickerDialog.setSelectableDays(_allAvailabilities.stream().map(
                availability -> toCalendar( availability.Start_date)).toArray(Calendar[]::new));

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
        _selectedAvailability = new Availability();
        _selectedBranch = new Branch();
        _itemTitle.setText(m_displayedItem.Title);
        _itemCategory.setText(String.format(getString(R.string.genres), m_displayedItem.Category));
        Picasso.get().load(FirebaseStorage.getInstance().getReference().child("Items images").child(m_displayedItem.Id + ".jpg").getPath()).into(_itemImage);
        _itemDescription.setText(String.format(getString(R.string.description_format), m_displayedItem.Description));

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
            java.sql.Date sqlDate = new java.sql.Date(cur.getTimeInMillis());

            _selectedAvailability = _allAvailabilities.stream().filter((availability -> availability.Start_date == sqlDate)).
                    findFirst().orElse(new Availability());
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
                if (_selectedAvailability.Id != 0) {

                    if (_selectedBranch.Title.equals("")) {
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
                    Toast.makeText(getContext(), R.string.branch_not_selected, Toast.LENGTH_LONG).show();

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
