package com.example.lendandborrowclient;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import com.example.lendandborrowclient.Validation.TextInputLayoutDataAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nimrod on 06/08/2017.
 */

public class BorrowConfirmFragment extends Fragment implements Validator.ValidationListener
{
    private Validator _validator;

    @BindView(R.id.tv_borrow_summary)
    TextView _borrowSummaryView;
    @BindView(R.id.til_first_name) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _firstName;
    @BindView(R.id.til_last_name) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _lastName;
    @BindView(R.id.til_email) @Email(messageResId = R.string.email_error)
    TextInputLayout _emailAddress;
    @BindView(R.id.til_phone) @Pattern(regex = "^05[0-9]{8}$", messageResId = R.string.phone_number_error)
    TextInputLayout _phoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_borrow_confirm, container, false);
        // Binding Fields
        ButterKnife.bind(this, v);

        // Setting fields validator
        _validator = new Validator(this);
        _validator.setValidationListener(this);
        _validator.registerAdapter(TextInputLayout.class, new TextInputLayoutDataAdapter());

        // Setting view details
        _borrowSummaryView.setText(ConstructBorrowSummary());

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private String ConstructBorrowSummary()
    {
        StringBuilder builder = new StringBuilder("Borrow details:\n");
        Item item = ((MainActivity)getActivity()).GetSelectedItem();
        Branch branch = ((MainActivity)getActivity()).GetSelectedBranch();
        Availability availability = ((MainActivity)getActivity()).GetSelectedAvailability();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = df.format(availability.Start_date);
        String endDate = df.format(availability.End_date);

        builder.append(item.Title).append(", Category: ").append(item.Category)
                .append("\n during: ").append(startDate).append(" to: ").append(endDate)
                .append("\n from branch: ").append(branch.Title).append("(").append(branch.Address).append(")");

        return builder.toString();
    }

    @OnClick(R.id.btn_purchase)
    public void Validate()
    {
        _validator.validate();
    }

    @Override
    public void onValidationSucceeded()
    {
        ClearErrorOnAllFields();

        Borrow borrow = new Borrow();
        borrow.Email = _emailAddress.getEditText().getText().toString();
        borrow.First_name = _firstName.getEditText().getText().toString();
        borrow.Last_name = _lastName.getEditText().getText().toString();
        borrow.Phone = _phoneNumber.getEditText().getText().toString();
        borrow.BranchTitle = ((MainActivity)getActivity()).GetSelectedBranch().Title;
        borrow.AvailabilityId = ((MainActivity)getActivity()).GetSelectedAvailability().Id;

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        // Send server request and wait.
        HandyServiceFactory.GetInstance().Borrow(borrow).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe((s, throwable) -> {
                            progressDialog.dismiss();

                            if (s != null)
                                ShowBorrowCompletionDialog(s);
                            else
                                Snackbar.make(getView(), "Failed submitting borrow", Snackbar.LENGTH_LONG).show();
                        }
                );
    }

    private void ShowBorrowCompletionDialog(String selectionId)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        TextView messageText = new TextView(getContext());
        messageText.setText(String.format(getString(R.string.borrow_approval_text), selectionId, _emailAddress.getEditText().getText().toString()));
        messageText.setTextSize(20f);
        messageText.setGravity(Gravity.CENTER);

        builder.setView(messageText);

        builder.setPositiveButton("Ok", (dialog, id) -> {
            ((MainActivity)getActivity()).CloseAllFragments();
            ((MainActivity)getActivity()).ShowItemsListFragment();
        });

        AlertDialog dlg = builder.create();

        dlg.show();

        final Button positiveButton = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        MenuItem nextItem = menu.findItem(R.id.next_action);

        nextItem.setVisible(false);
        searchItem.setVisible(false);

        super.onPrepareOptionsMenu(menu);
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
            else
                Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }

        Snackbar.make(getView(), R.string.illegal_fields, Snackbar.LENGTH_LONG).show();
    }

    private void ClearErrorOnAllFields()
    {
        _firstName.setError(null);
        _lastName.setError(null);
        _emailAddress.setError(null);
        _phoneNumber.setError(null);
        _firstName.setErrorEnabled(false);
        _lastName.setErrorEnabled(false);
        _emailAddress.setErrorEnabled(false);
        _phoneNumber.setErrorEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                getActivity().onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


