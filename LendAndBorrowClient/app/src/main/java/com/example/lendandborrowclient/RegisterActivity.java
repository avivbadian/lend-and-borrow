package com.example.lendandborrowclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;

    private Button createAccountButton;
    private EditText userEmail, userPassword;
    private TextView alreadyHaveAccountLink;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initializeFields();

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }


    private void initializeFields() {
        createAccountButton = findViewById(R.id.register_button);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        alreadyHaveAccountLink = findViewById(R.id.already_have_account_link);
        loadingBar = new ProgressDialog(this);
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // Validations
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Loading bar for account creation progress
        loadingBar.setTitle("Creating new account");
        loadingBar.setMessage("Please wait while the new account is being created.");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // TODO: Decide what data to keep in SQL db regarding each user.
                    // String currentUserID = mAuth.getCurrentUser().getUid();
                    
                    sendUserToMainActivity();
                    Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                loadingBar.dismiss();
            }
        });
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
