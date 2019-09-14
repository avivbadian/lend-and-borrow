package com.example.lendandborrowclient;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lendandborrowclient.Models.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemsAdapterViewHolder>
{
    List<Item> _itemsList;
    ItemClickedListener m_listener;
    private List<Item> _displayedItems;

    public ItemsListAdapter(ItemClickedListener listener)
    {
        m_listener = listener;
    }

    @Override
    public ItemsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        return new ItemsAdapterViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.items_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ItemsAdapterViewHolder holder, int position)
    {
        if (position < _displayedItems.size())
            holder.bind(_displayedItems.get(position));
        else
            Log.d("RecyclerView",
                    "onBindViewHolder: Trying to bind position: " +
                            position +
                            "when data size is: " +
                            _itemsList.size());
    }

    @Override
    public int getItemCount()
    {
        return _displayedItems == null ? 0 : _displayedItems.size();
    }

    public void SetData(List<Item> items)
    {
        _itemsList = items;
        _displayedItems = items;
        notifyDataSetChanged();
    }

    public void FilterDataByItemName(String query)
    {
        _displayedItems = _itemsList.
                stream().
                filter(item -> item.Title.toLowerCase().contains(query.toLowerCase())).
                collect(Collectors.toList());

        notifyDataSetChanged();
    }

//    public void SetImageAt(Bitmap image, int position)
//    {
//        if (position < _itemsList.size())
//            _itemsList.get(position).MoviePicture = image;
//
//        notifyItemChanged(position);
//    }

    public void ClearFilteredData()
    {
        _displayedItems = _itemsList;

        notifyDataSetChanged();
    }

    class ItemsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_item_desc_preview)
        TextView _itemTitle;
        @BindView(R.id.iv_item_image_preview)
        ImageView _itemPicture;
        @BindView(R.id.card_view) CardView _cardView;

        private StorageReference _itemsImagesRef;

        ItemsAdapterViewHolder(View view)
        {
            super(view);
            _itemsImagesRef = FirebaseStorage.getInstance().getReference().child("Items images");
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.card_view, R.id.iv_item_image_preview, R.id.tv_item_desc_preview})
        public void OnChooseItemClicked()
        {
            m_listener.OnItemClicked(_displayedItems.get(getAdapterPosition()));
        }

        public void bind(final Item item)
        {
            _itemTitle.setText(item.Title);

            _itemsImagesRef.child(item.Id + ".jpg").getDownloadUrl().addOnSuccessListener((OnSuccessListener<UploadTask.TaskSnapshot>) taskSnapshot -> {
                String image = taskSnapshot.getDownloadUrl().toString();
                Picasso.get().load(image).into(_itemPicture);
            });

    }
}
