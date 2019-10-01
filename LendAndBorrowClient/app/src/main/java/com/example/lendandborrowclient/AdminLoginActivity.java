package com.example.lendandborrowclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lendandborrowclient.Admins.AdminManagementActivity;
import com.example.lendandborrowclient.Models.Admin;
import com.example.lendandborrowclient.RestAPI.HandyRestApiBuilder;
import com.example.lendandborrowclient.Validation.TextInputLayoutDataAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdminLoginActivity extends AppCompatActivity implements Validator.ValidationListener
{
    private static final String TAG = "AdminLoginActivity";

    @BindView(R.id.til_input_username) @NotEmpty(messageResId = R.string.empty_field_error)
    TextInputLayout _userText;
    @BindView(R.id.til_input_password) @Password(scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.password_invalid_error)
    TextInputLayout _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(android.R.id.content)
    View _rootView;

    private Validator _validator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle("Admin");

        // Setting fields validator
        _validator = new Validator(this);
        _validator.setValidationListener(this);
        _validator.registerAdapter(TextInputLayout.class, new TextInputLayoutDataAdapter());
        _loginButton.setOnClickListener(v -> _validator.validate());
    }

    public void Login()
    {
        // Loading while validating user
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(AdminLoginActivity.this,
                android.R.style.Theme_Material_Light);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        // Getting user parameters
        String userName = _userText.getEditText().getText().toString();
        String password = _passwordText.getEditText().getText().toString();

        // POST request to the server to validate user (parameters sent through the body)
        HandyRestApiBuilder.GetInstance().ValidateUser(new Admin(userName, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseBody, throwable) ->
                {
                    progressDialog.dismiss();

                    if (throwable == null)
                        OnLoginSuccess();
                    else
                    {
                        OnLoginFailed();

                        Snackbar.make(_rootView, "Login failed, user or password incorrect", Snackbar.LENGTH_INDEFINITE).show();
                    }
                });
    }

    public void OnLoginSuccess()
    {
        // Start the administration activity
        _loginButton.setEnabled(true);
        startActivity(new Intent(this, AdminManagementActivity.class));
        finish();
    }

    public void OnLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    @Override
    public void onValidationSucceeded()
    {
        Login();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        ClearErrorOnAllFields();

        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof TextInputLayout)
                ((TextInputLayout) view).setError(message);
            else
                Snackbar.make(_rootView, message, Snackbar.LENGTH_LONG).show();
        }

        Snackbar.make(_rootView, R.string.illegal_fields, Snackbar.LENGTH_LONG).show();
    }

    private void ClearErrorOnAllFields()
    {
        _userText.setError(null);
        _passwordText.setError(null);
        _userText.setErrorEnabled(false);
        _passwordText.setErrorEnabled(false);
    }

}