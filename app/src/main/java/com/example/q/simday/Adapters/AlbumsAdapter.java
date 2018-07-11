package com.example.q.simday.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class  AlbumsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> dataList;

    public AlbumsAdapter(Context mContext, ArrayList<String> result) {
        context = mContext;
        dataList = result;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if(view == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2,2,2,2);
            imageView.setAdjustViewBounds(false);
        } else {
            imageView = (ImageView) view;
        }

        if(position < dataList.size()) {
            Bitmap bmp = BitmapFactory.decodeFile(dataList.get(position));
            Bitmap resized = Bitmap.createScaledBitmap(bmp, 350, 350, true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(resized);
        }

        return imageView;
    }
}
