package com.example.q.simday;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {

    private int thumbnail;

    public Album() {
    }

    public Album(int thumbnail) {
        this.thumbnail = thumbnail;
    }


    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}