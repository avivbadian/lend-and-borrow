package com.example.lendandborrowclient;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lendandborrowclient.Models.User;
import com.example.lendandborrowclient.Models.UserAction;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyActivityFragment extends Fragment {

    private View myActivityView;

    private RecyclerView borrowsRecyclerView;
    private RecyclerView lendingsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ObjectMapper objMapper;

    private String currentUserId;
    private FirebaseAuth mAuth;

    public MyActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_activity, container, false);

        // Firebase authentication.
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        borrowsRecyclerView = view.findViewById(R.id.items_you_borrowed);
        lendingsRecyclerView = view.findViewById(R.id.lent_items);

        // use a linear layout manager
        borrowsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lendingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        objMapper = new ObjectMapper();

        // GET ALL ACTIONS related to the current user
        String borrowsUrl = "http://10.0.2.2:8080/users/" + currentUserId + "/borrows";
        String lentsUrl = "http://10.0.2.2:8080/users/" + currentUserId + "/lents";;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, borrowsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<UserAction> borrows = objMapper.readValue(response.toString(),
                                    objMapper.getTypeFactory().constructCollectionType(List.class, UserAction.class));

                            borrowsRecyclerView.setAdapter(new UserActionsAdapter(borrows, false));
                        } catch (Exception e1) {
                            e1.printStackTrace();
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

        JsonObjectRequest lentsJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, lentsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<UserAction> lents = objMapper.readValue(response.toString(),
                                    objMapper.getTypeFactory().constructCollectionType(List.class, UserAction.class));

                            borrowsRecyclerView.setAdapter(new UserActionsAdapter(lents, true));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // Access the RequestQueue through the singleton accessor
        RequestsManager.getInstance(getContext()).addToRequestQueue(lentsJsonObjectRequest);

        return view;
    }
}
