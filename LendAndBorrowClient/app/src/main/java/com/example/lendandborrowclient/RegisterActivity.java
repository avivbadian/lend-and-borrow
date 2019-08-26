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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.register_button)
    Button createAccountButton;

    @BindView(R.id.register_email) @NotEmpty(messageResId = R.string.username_empty_message)
    EditText userEmail;

    @BindView(R.id.login_password) @Password(scheme = Password.Scheme.ALPHA_NUMERIC, messageResId = R.string.password_invalid_error)
    EditText userPassword;

    @BindView(R.id.already_have_account_link)
    TextView alreadyHaveAccountLink;

    @BindView(android.R.id.content)
    View parentView;

    private ProgressDialog loadingBar;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        loadingBar = new ProgressDialog(this);

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });

        // Setting fields validator
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(TextView.class, new TextViewStringAdapter());
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void createNewAccount() {
        final String username = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // Loading bar for account creation progress
        loadingBar.setTitle("Creating new account");
        loadingBar.setMessage("Please wait while the new account is being created.");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        LoansHttpService.GetInstance().CreateUser(new User(username, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean success, Throwable throwable) {
                        loadingBar.dismiss();
                        if (success)
                            handleUserCreated(username);
                        else {
                            handleUserCreationFailed();
                            Snackbar.make(parentView, "Login failed, user or password incorrect", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });
    }

    private void handleUserCreationFailed() {
        Toast.makeText(getBaseContext(), "Account creation failed", Toast.LENGTH_LONG).show();
    }


    private void handleUserCreated(String username) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        settings.edit().putString("username", username).apply();
        sendUserToMainActivity();
    }

    @Override
    public void onValidationSucceeded() {
        createNewAccount();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        userEmail.setError(null);
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

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);

        // Adds the new activity (main) to the lifecycle, removes the current (register activity) from the stack
        // This is to make sure that after logging in, pressing back won't redirect to login activity.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

        // onPause() -> onStop() -> onDestroy() are called in that order.
        finish();
    }
}
