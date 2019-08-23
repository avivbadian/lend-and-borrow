//package com.example.lendandborrowclient;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.lendandborrowclient.Models.UserAction;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class UserActionsAdapter extends RecyclerView.Adapter<UserActionsAdapter.ActionViewHolder> {
//
//    private ArrayList<UserAction> actions;
//    private boolean owner;
//
//    public UserActionsAdapter(ArrayList<UserAction> userActions, boolean owner){
//        actions = userActions;
//        this.owner = owner;
//    }
//
//    @NonNull
//    @Override
//    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        // Create a new view
//        View viewLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_action, null);
//
//        // Create a view holder
//        return new ActionViewHolder(viewLayout);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
//        holder.borrowDate.setText(actions.get(position).BorrowDate.toString());
//
//        if (owner)
//            holder.userName.setText(actions.get(position).BorrowerName);
//        else
//            holder.userName.setText(actions.get(position).OwnerName);
//
//        holder.itemName.setText(actions.get(position).ItemName);
//        Picasso.get().load(actions.get(position).ImagePath).into(holder.itemImage);
//    }
//
//    @Override
//    public int getItemCount() {
//        return actions.size();
//    }
//
//    public static class ActionViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView itemName;
//        public TextView userName;
//        public TextView borrowDate;
//        public ImageView itemImage;
//
//        public ActionViewHolder(@NonNull View itemView) {
//            super(itemView);
//            itemName = itemView.findViewById(R.id.item_name);
//            userName = itemView.findViewById(R.id.user_name);
//            itemImage = itemView.findViewById(R.id.item_image);
//            borrowDate = itemView.findViewById(R.id.item_borrow_date);
//        }
//    }
//}
