package com.example.lendandborrowclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lendandborrowclient.Models.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    private ObjectMapper objMapper;

    private String currentUserId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Firebase authentication.
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        initalizeFields();
        objMapper = new ObjectMapper();

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateSettings();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        
        retrieveUserInfo();
    }

    private void retrieveUserInfo() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        String url = "http://10.0.2.2:8080/users/" + currentUserId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User user = objMapper.readValue(response.toString(), User.class);
                            if (!TextUtils.isEmpty(user.Username)) {
                                userName.setText(user.Username);
                                userStatus.setText(user.Status);
                            } else {
                                Toast.makeText(SettingsActivity.this, "Please update your profile", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // Access the RequestQueue through the singleton accessor
        RequestsManager.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateSettings() throws JSONException {
        final String setUsername = userName.getText().toString();
        final String setStatus = userStatus.getText().toString();

        if (TextUtils.isEmpty(setUsername)){
            Toast.makeText(this, "Please choose a user name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(setStatus)){
            Toast.makeText(this, "Please choose your status...", Toast.LENGTH_SHORT).show();
        } else {

            String json = "";
            User user = new User(){{Uid = currentUserId; Username = setUsername; Status = setStatus;}};
            ObjectWriter ow = objMapper.writer().withDefaultPrettyPrinter();
            try {
                json = ow.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String url = "http://10.0.2.2:8080/users/" + currentUserId;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            sendUserToMainActivity();
                            Toast.makeText(SettingsActivity.this, "Profile updated successfully..", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(SettingsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            // Access the RequestQueue through the singleton accessor
            RequestsManager.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void initalizeFields() {
        updateAccountSettings = findViewById(R.id.update_settings_button);
        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_profile_status);
        userProfileImage = findViewById(R.id.set_profile_image);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);

        // Adds the new activity (main) to the lifecycle, removes the current (register activity) from the stack
        // This is to make sure that after logging in, pressing back won't redirect to login activity.
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

        // onPause() -> onStop() -> onDestroy() are called in that order.
        finish();
    }
}
