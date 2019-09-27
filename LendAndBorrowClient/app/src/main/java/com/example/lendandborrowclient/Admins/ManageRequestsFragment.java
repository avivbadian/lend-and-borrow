package com.example.lendandborrowclient.Admins;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.lendandborrowclient.ListAdapters.RequestClickedListener;
import com.example.lendandborrowclient.ListAdapters.RequestsListAdapter;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.Models.Status;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManageRequestsFragment extends Fragment implements RequestClickedListener {


    /** because a Fragment may continue to exist after its Views are destroyed,
     *  we manually call .unbind() from fragments to release reference to Views (and allow associated memory to be reclaimed)*/
    private Unbinder _unbinder;
    private RequestsListAdapter _requestsListAdapter;
    public static ManageRequestsFragment newInstance() {
        return new ManageRequestsFragment();
    }

    @BindView(R.id.rv_requests)
    RecyclerView requestsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_requests, container, false);
        _unbinder = ButterKnife.bind(this, v);
        _requestsListAdapter = new RequestsListAdapter(getContext(), this);
        requestsRecyclerView.setAdapter(_requestsListAdapter);
        requestsRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 1));
        requestsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ReloadPendingRequests();
        return v;
    }

    void ReloadPendingRequests() {
        HandyServiceFactory.GetInstance().GetAllPendingRequests().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((requests, throwable) -> {
                    if (throwable == null) {
                        _requestsListAdapter.InitData(requests);
                    } else {
                        Toast.makeText(getContext(), "Failed loading pending requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void RequestConfirmClicked(Borrow request, Item relatedItem, Availability relatedAvailability) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Confirmation dialog");
        alert.setMessage(R.string.request_approve_confirm);
        alert.setPositiveButton("Yes", (dialog, which) ->
        {
            ApproveRequest(request, relatedItem, relatedAvailability);
            dialog.dismiss();
        });

        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public void RequestDeclineClicked(Borrow request, Item relatedItem, Availability relatedAvailability) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Confirmation dialog");
        alert.setMessage(R.string.request_decline_confirm);
        alert.setPositiveButton("Yes", (dialog, which) ->
        {
            DeclineRequest(request, relatedItem, relatedAvailability);
            dialog.dismiss();
        });

        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private void DeclineRequest(Borrow borrow, Item relatedItem, Availability relatedAvailability) {
        HandyServiceFactory.GetInstance().UpdateBorrowStatus(borrow.Id, Status.declined).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseBody, throwable) -> {
                    if (throwable == null) {
                        ReloadPendingRequests();
                        ((ManagementActivity)getActivity()).sendSMS(borrow, relatedItem, relatedAvailability, false);
                    } else {
                        Toast.makeText(getContext(), "There was an error declining the request. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void DeclineRequests(List<Borrow> borrowsToDecline, Item relatedItem, Availability relatedAvailability) {
        for (Borrow borrow : borrowsToDecline) {
            DeclineRequest(borrow, relatedItem, relatedAvailability);
        }
    }


    private void ApproveRequest(Borrow borrow, Item relatedItem, Availability relatedAvailability) {
        HandyServiceFactory.GetInstance().UpdateBorrowStatus(borrow.Id, Status.approved).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((requests, throwable) -> {
                    if (throwable == null) {
                        ReloadPendingRequests();
                        // Declines other requests for that particular item on the same dates
                        DeclineRequests(requests, relatedItem, relatedAvailability);
                        ((ManagementActivity)getActivity()).sendSMS(borrow, relatedItem, relatedAvailability, true);
                    } else {
                        Toast.makeText(getContext(), "There was an error approving the request. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onDestroy() {
        _unbinder.unbind();
        super.onDestroy();
    }
}
