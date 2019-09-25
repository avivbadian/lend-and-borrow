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

import com.bumptech.glide.Glide;
import com.example.lendandborrowclient.NotificationListeners.AvailabilitiesChangedListener;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
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
    private Item _displayedItem;
    private List<Availability> _allAvailabilities;
    private List<Branch> _allBranches;
    private Availability _selectedAvailability;
    private DatePickerDialog _initialDatePickerDialog;
    private ArrayAdapter<Branch> _branchesSpinnerAdapter;
    private Unbinder _unbinder;

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
        _unbinder = ButterKnife.bind(this, v);
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
        HandyServiceFactory.GetInstance().GetBranches().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((branches, throwable) ->
                {
                    if (branches != null) {
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
        HandyServiceFactory.GetInstance().GetItemAvailabilities(_displayedItem.Id)
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

        _initialDatePickerDialog = DatePickerDialog.newInstance(new initialDatePicker(),
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));

        if (_allAvailabilities != null && _allAvailabilities.size() != 0)
        {
            Calendar[] selectableDays = _allAvailabilities.stream().map(
                    availability -> toCalendar(availability.Start_date)).toArray(Calendar[]::new);
            _initialDatePickerDialog.setSelectableDays(selectableDays);
            _initialDatePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        }

        else {
            Log.d("Availabilities", "availabilities were not found");
            Snackbar.make(getView(), "No available dates", Snackbar.LENGTH_LONG).show();
        }
    }

    private void PopulateFields()
    {
        _selectedAvailability = new Availability();
        _itemTitle.setText(_displayedItem.Title);
        _itemCategory.setText(String.format(getString(R.string.categories), _displayedItem.Category));
        Glide.with(this).load(_displayedItem.Path).into(_itemImage);
        _itemDescription.setText(String.format(getString(R.string.description_format), _displayedItem.Description));
    }

    @Override
    public void AvailabilitiesChanged(int itemId) {
        if (itemId == _displayedItem.Id)
            LoadAvailabilities();
    }


    class initialDatePicker implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cur = Calendar.getInstance();
            cur.set(year, monthOfYear, dayOfMonth);
            Date date = new Date(cur.getTimeInMillis());
            DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

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
                    Branch selectedBranch = (Branch)_branchesSpinner.getSelectedItem();
                    if (selectedBranch == null || selectedBranch.Title.equals("")) {
                        Toast.makeText(getContext(), "Please choose a branch", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    try  {
                        ((MainActivity) getActivity()).ShowBorrowConfirmDialog(selectedBranch, _selectedAvailability);
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

    @Override
    public void onDestroyView() {
        _unbinder.unbind();
        super.onDestroyView();
    }
}
