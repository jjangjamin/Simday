package com.example.q.simday.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.q.simday.R;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");

        ArrayList<String> dataList;
        dataList = new ArrayList<String>();
        Cursor c = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null );
        if(c.moveToFirst()) do {
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            dataList.add(data);
        } while(c.moveToNext());


        ImageView imageView = (ImageView) findViewById(R.id.FullimageView);
        Bitmap bmp = BitmapFactory.decodeFile(dataList.get(position));
        imageView.setImageBitmap(bmp);

    }
}
