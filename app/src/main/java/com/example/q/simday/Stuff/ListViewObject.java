package com.example.q.simday.Stuff;

import android.graphics.Bitmap;

public class ListViewObject {
    private Bitmap img;
    private String Title;
    private String context;

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}