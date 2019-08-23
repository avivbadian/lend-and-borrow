package com.example.lendandborrowclient;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button updateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    private ObjectMapper objMapper;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImagesRef;

    private static final int GalleryPick = 1;
    private static String currentPicPath;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Firebase authentication.
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initializeFields(view);

        // User can update his username only one time.
        userName.setVisibility(View.INVISIBLE);

        // Used to deserialize JSON
        objMapper = new ObjectMapper();

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });

        retrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data != null){

            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                final StorageReference filePath = UserProfileImagesRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    currentPicPath =  uri.toString();
                                    Picasso.get().load(currentPicPath).into(userProfileImage);
                                }
                            });
                        }
                        else {
                            String msg = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
                                TextView textView = getView().findViewById(R.id.cur_user_name);
                                textView.setText("Profile of " + user.Username);
                                userStatus.setText(user.Status);
                                if (user.ImagePath != "")
                                    Picasso.get().load(user.ImagePath).into(userProfileImage);
                            } else {
                                // User can update his username one time.
                                userName.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Please update your profile", Toast.LENGTH_SHORT).show();
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
        RequestsManager.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void updateSettings() {
        final String setUsername = userName.getText().toString();
        final String setStatus = userStatus.getText().toString();

        if (TextUtils.isEmpty(setUsername)){
            Toast.makeText(getContext(), "Please choose a user name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(setStatus)){
            Toast.makeText(getContext(), "Please choose your status...", Toast.LENGTH_SHORT).show();
        } else {
            updateUser(new User(){{Uid = currentUserId; Username = setUsername; Status = setStatus; ImagePath = currentPicPath;}});
        }
    }

    private void updateUser(User user) {
        String json = "";
        ObjectWriter ow = objMapper.writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String url = "http://10.0.2.2:8080/users/" + currentUserId;

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.PUT, url, new JSONObject(json), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getActivity(), "Successfully updated profile", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            // Don't send request multiple times
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(Integer.MAX_VALUE,
                    0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Access the RequestQueue through the singleton accessor
        RequestsManager.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void initializeFields(View view) {
        updateAccountSettings = view.findViewById(R.id.update_settings_button);
        userName = view.findViewById(R.id.set_user_name);
        userStatus = view.findViewById(R.id.set_profile_status);
        userProfileImage = view.findViewById(R.id.set_profile_image);
    }

//    private void sendUserToMainActivity() {
//        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
//
//        // Adds the new activity (main) to the lifecycle, removes the current (register activity) from the stack
//        // This is to make sure that after logging in, pressing back won't redirect to login activity.
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//    }
}
