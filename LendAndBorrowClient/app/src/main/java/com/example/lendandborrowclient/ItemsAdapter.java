package com.example.lendandborrowclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lendandborrowclient.Models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private Context context;
    private List<Item> itemsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dot) TextView dot;
        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.item_image)ImageView itemImage;

        public MyViewHolder(View view) {
            super(view);
        }
    }

    public ItemsAdapter(Context context, List<Item> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemsList.get(position);

        holder.itemName.setText(item.Name);

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Loading item image
        Picasso.get().load(item.ImagePath).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}