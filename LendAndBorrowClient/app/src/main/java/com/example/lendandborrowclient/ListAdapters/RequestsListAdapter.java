package com.example.lendandborrowclient.ListAdapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.R;
import com.example.lendandborrowclient.RestAPI.HandyServiceFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * bridge between a ListView and the data that backs the list.
 * The ListView can display any data provided that it is wrapped in a ListAdapter.
 */
public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.RequestViewHolder> {

    List<Borrow> _requestsList = new ArrayList<>();
    private RequestClickedListener _listener;
    private Context _ctx;

    public RequestsListAdapter(Context ctx, RequestClickedListener listener){
        _ctx = ctx;
        _listener = listener;
    }


    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RequestViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.request_list_item, viewGroup, false), _ctx);

    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        if (position < _requestsList.size())
            holder.bind(_requestsList.get(position));
        else
            Log.d("RecyclerView",
                    "onBindViewHolder: Trying to bind position: " +
                            position +
                            "when data size is: " +
                            _requestsList.size());
    }

    @Override
    public int getItemCount() {
        return _requestsList.size();
    }

    public void InitData(List<Borrow> requests)
    {
        _requestsList = requests;
        notifyDataSetChanged();
    }

    /**
     * The views in the list are represented by view holder objects.
     * Each view holder is in charge of displaying a single item with a view.
     */
    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_request_item_image)
        ImageView itemImage;
        @BindView(R.id.tv_request_item_name)
        TextView itemName;
        @BindView(R.id.tv_item_category)
        TextView itemCategory;
        @BindView(R.id.tv_request_dates)
        TextView requestDates;
        @BindView(R.id.iv_approve_request)
        ImageView approveRequest;
        @BindView(R.id.iv_decline_request)
        ImageView declineRequest;

        private Context _ctx;
        private Availability _relatedAvailability;
        private Item _relatedItem;

        public RequestViewHolder(View requestView, Context ctx) {
            super(requestView);
            _ctx = ctx;
            ButterKnife.bind(this, requestView);
        }

        public void bind(final Borrow request) {

            HandyServiceFactory.GetInstance().GetAvailabilityById(request.Availability)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((availability, throwable) ->
                    {
                        if (throwable == null && availability.Item_id != 0) {
                            _relatedAvailability = availability;
                            HandyServiceFactory.GetInstance().GetItem(availability.Item_id)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe((item, ex) -> {
                                        if (ex == null) {
                                            _relatedItem = item;
                                            itemName.setText(item.Title);
                                            itemCategory.setText(item.Category);
                                            try {
                                                Glide.with(_ctx).load(item.Path).into(itemImage);
                                            } catch (Exception exception) {
                                                exception.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(_ctx, "Failed Loading requested item", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            requestDates.setText(availability.toString());
                        } else
                            Toast.makeText(_ctx, "Failed Loading request availability", Toast.LENGTH_SHORT).show();
                    });
        }

        @OnClick(R.id.iv_approve_request)
        public void OnApproveRequestClicked() {
            _listener.RequestConfirmClicked(_requestsList.get(getAdapterPosition()), _relatedItem, _relatedAvailability);
        }

        @OnClick(R.id.iv_decline_request)
        public void OnDeclineRequestClicked() {
            _listener.RequestDeclineClicked(_requestsList.get(getAdapterPosition()), _relatedItem, _relatedAvailability);
        }
    }
}
