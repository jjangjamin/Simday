package com.example.q.simday.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.simday.Fragments.ContactFragment;
import com.example.q.simday.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<ContactFragment.User> userList;
    private Context context;

    public RecyclerAdapter(List<ContactFragment.User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ContactFragment.User user = userList.get(i);

        viewHolder.itemImage.setImageResource(user.getImageResourceId());
        viewHolder.itemTitle.setText(user.getProfileName());
        viewHolder.itemDetail.setText(user.getPhoneNumber());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contact, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDetail;
        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemDetail = (TextView) itemView.findViewById(R.id.item_detail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
}


/* private List<AllUsers.User> userList;
    private Context context;
    public AllUsersAdapter(List<AllUsers.User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cardview_layout, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        AllUsers.User user = userList.get(position);

        holder.ivProfilePic.setImageResource(user.getImageResourceId());
        holder.tvProfileName.setText(user.getProfileName());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());
        holder.tvEmailId.setText(user.getEmailId());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvProfileName;
        TextView tvPhoneNumber;
        TextView tvEmailId;
        public UserViewHolder(View itemView) {
            super(itemView);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) itemView.findViewById(R.id.tvProfileName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
        }
    }*/