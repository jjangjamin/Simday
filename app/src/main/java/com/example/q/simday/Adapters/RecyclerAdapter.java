package com.example.q.simday.Adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
    private static Context context;
    private ItemClick itemClick;

    public interface ItemClick {
        public void onClick(View view,int position);
    }
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public RecyclerAdapter(List<ContactFragment.User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final ContactFragment.User user = userList.get(position);
        viewHolder.itemImage.setImageResource(user.getImageResourceId());
        viewHolder.itemTitle.setText(user.getProfileName());
        viewHolder.itemDetail.setText(user.getPhoneNumber());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                final int Position = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getRootView().getContext());
                alert.setTitle("연락하기");
                alert.setMessage(user.getProfileName() + " 님께 연락할까요?").setIcon(android.R.drawable.ic_dialog_dialer)
                        .setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + user.getPhoneNumber()));
                                try {
                                    if (ActivityCompat.checkSelfPermission(view.getRootView().getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("문자보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",user.getPhoneNumber(),null));
                                try {
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        .setNeutralButton("취소", null).show();
            }
        });
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