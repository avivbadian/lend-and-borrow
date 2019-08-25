package com.example.lendandborrowclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lendandborrowclient.Models.User;
import com.example.lendandborrowclient.RestAPI.LoansHttpService;
import com.google.android.material.snackbar.Snackbar;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.TextViewStringAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.login_button)
    private Button loginButton;

    @BindView(R.id.login_username) @NotEmpty(messageResId = R.string.username_empty_message)
    private EditText username;

    @BindView(R.id.login_password) @Password(scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.password_invalid_error)
    private EditText userPassword;

    @BindView(R.id.need_new_account_link)
    private TextView needNewAccountLink;

    @BindView(android.R.id.content)
    View parentView;

    private ProgressDialog loadingBar;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loadingBar = new ProgressDialog(this);

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

        // Setting fields validator
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(TextView.class, new TextViewStringAdapter());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void tryLogIn() {
        final String userNameVal = username.getText().toString();
        String password = userPassword.getText().toString();

        // Loading bar for signing in progress
        loadingBar.setTitle("Log in");
        loadingBar.setMessage("Please wait while you are being logged in");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        LoansHttpService.GetInstance().ValidateUser(new User(userNameVal, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean success, Throwable throwable) {
                        loadingBar.dismiss();
                        if (success)
                            handleLoginSuccess(userNameVal);
                        else {
                            handleLoginFailed();
                            Snackbar.make(parentView, "Login failed, user or password incorrect", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });
    }


    private void handleLoginSuccess(String userNameVal) {
        // Saving username to sharedPreferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        settings.edit().putString("username", userNameVal).apply();
        sendUserToMainActivity();
    }

    private void handleLoginFailed() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onValidationSucceeded()
    {
        tryLogIn();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        username.setError(null);
        userPassword.setError(null);

        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof TextView)
                ((TextView) view).setError(message);
            else
                Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show();
        }

        Snackbar.make(parentView, R.string.invalid_fields, Snackbar.LENGTH_LONG).show();
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);

        // Adds the new activity (main) to the lifecycle, removes the current (register activity) from the stack
        // This is to make sure that after logging in, pressing back won't redirect to login activity.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

        // onPause() -> onStop() -> onDestroy() are called in that order.
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
