package com.example.lendandborrowclient;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.example.lendandborrowclient.RestAPI.HandyRestApiBuilder;
import com.example.lendandborrowclient.Validation.TextInputLayoutDataAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BorrowConfirmFragment extends Fragment implements Validator.ValidationListener
{
    private Validator _validator;
    private Unbinder _unbinder;

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
        _unbinder = ButterKnife.bind(this, v);

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
        StringBuilder builder = new StringBuilder();
        Item item = ((MainActivity)getActivity()).GetSelectedItem();
        Branch branch = ((MainActivity)getActivity()).GetSelectedBranch();
        Availability availability = ((MainActivity)getActivity()).GetSelectedAvailability();

        builder.append(item.Title).append("\nCategory: ").append(item.Category)
                .append("\n" + availability)
                .append("\nbranch: ").append(branch.Title).append("(").append(branch.Address).append(")");

        return builder.toString();
    }

    @OnClick(R.id.btn_submit)
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
        borrow.Branch = ((MainActivity)getActivity()).GetSelectedBranch().Title;
        borrow.Availability = ((MainActivity)getActivity()).GetSelectedAvailability().Id;

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                android.R.style.Theme_Material_Light);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        // Send server request and wait.
        HandyRestApiBuilder.GetInstance().Borrow(borrow).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe((ResponseBody, throwable) -> {
                            progressDialog.dismiss();

                            if (throwable == null){
                                ShowBorrowCompletionDialog();
                            }
                            else
                                Snackbar.make(getView(), "Failed submitting borrow", Snackbar.LENGTH_LONG).show();
                        }
                );
    }

    private void ShowBorrowCompletionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), android.R.style.Widget_Holo_Light));

        TextView messageText = new TextView(getContext());
        messageText.setText(String.format(getString(R.string.borrow_approval_text), _phoneNumber.getEditText().getText().toString()));
        messageText.setTextSize(20f);
        messageText.setGravity(Gravity.CENTER);

        builder.setView(messageText);

        builder.setPositiveButton("Ok", (dialog, id) -> {
            ((MainActivity)getActivity()).CloseAllFragments();
            ((MainActivity)getActivity()).DisplayItemsFragment();
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
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        _unbinder.unbind();
        super.onDestroyView();
    }
}


