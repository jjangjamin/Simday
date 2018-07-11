package com.example.q.simday.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.q.simday.Activity.FullImageActivity;
import com.example.q.simday.Adapters.AlbumsAdapter;
import com.example.q.simday.Adapters.RecyclerAdapter;
import com.example.q.simday.R;
import com.facebook.AccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

import static com.example.q.simday.R.drawable.contacticon;
import static com.example.q.simday.R.drawable.uploading;

public class GalleryFragment extends Fragment {

    protected BottomNavigationView navigationView;
    private ArrayList<String> dataList1;
    private ArrayList<String> dataList2;
    private ArrayList<String> dataListname;
    private GridView mGridView;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    private Integer onchange ;
    String master;
    JSONArray down;
    ImageView uploadingicon;
    FrameLayout upload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_gallery, container, false);
        super.onCreate(savedInstanceState);

        mGridView = (GridView)v.findViewById(R.id.gridView);
        ImageView backdrop = (ImageView)v.findViewById(R.id.backdropimage);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(backdrop);
        Glide.with(getActivity()).load(R.drawable.backdropp).into(gifImage);

        uploadingicon = (ImageView)v.findViewById(R.id.uploading);
        GlideDrawableImageViewTarget gifImage2 = new GlideDrawableImageViewTarget(uploadingicon);
        Glide.with(getActivity()).load(R.drawable.uploading).into(gifImage2);

        uploadingicon.setVisibility(View.GONE);
        Button refresh = (Button)v.findViewById(R.id.showupload);
        checkPermission();

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (onchange==1) {
                    gallery();
                }else {
                    try {
                        DownImages();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


        return v;
    }


    private void DownImages() throws JSONException, IOException {
        if (AccessToken.getCurrentAccessToken()!=null)master= AccessToken.getCurrentAccessToken().getUserId();
        if (master==null) master="JJJ";
        Log.d("master","222222222"+ master);

        down=new JSONArray();
        new DownTask().execute();
    }

    private class DownTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            String getURL = "http://52.231.69.25:8080/api/photos/"+master;
            HttpGet get = new HttpGet(getURL);
            HttpResponse responseGet = null;
            try {
                responseGet = client.execute(get);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity resEntityGet = responseGet.getEntity();
            String json_string = null;
            try {
                json_string = EntityUtils.toString(resEntityGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                down=new JSONArray(json_string);
                return new JSONArray(json_string).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getURL;
        }

        @Override
        protected void onPostExecute(String s) {
            dataList2 = new ArrayList<>();

            for(int i=0; i < down.length(); i++) {
                JSONObject obj= null;
                try {
                    obj = down.getJSONObject(i);
                    String tn = obj.getString("img");
                    dataList2.add(tn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mGridView.setAdapter(new AlbumsAdapter(getActivity(), dataList2));
            onchange=1;

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {

                    new AlertDialog.Builder(getActivity()).setTitle("삭제").setMessage("선택하신 사진을 삭제 하시겠습니까?").setIcon(android.R.drawable.ic_input_add)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (AccessToken.getCurrentAccessToken()!=null)master= AccessToken.getCurrentAccessToken().getUserId();
                                    if (master==null) master="JJJ";
                                    new deleteDownTask().execute("http://52.231.69.25:8080/api/deleteimage", dataListname.get(position) ,dataList2.get(position));
                                    Log.i("master",dataListname.get(position)+dataList2.get(position));

                                }
                            })
                            .setNegativeButton("아니오", null).show();
                }
            });


        }

    }

    private class deleteDownTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name",params[1]);
                jsonObject.accumulate("img",params[2]);
                jsonObject.accumulate("master",master);
                HttpURLConnection con;
                URL url = new URL(params[0]);
                Log.i("master",params[0]);
                Log.i("master",jsonObject.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.connect();


                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();
                InputStream stream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpClient client2 = new DefaultHttpClient();
            String getURL2 = "http://52.231.69.25:8080/api/photos/"+master;
            HttpGet get2 = new HttpGet(getURL2);
            HttpResponse responseGet2 = null;
            try {
                responseGet2 = client2.execute(get2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity resEntityGet = responseGet2.getEntity();
            String json_string = null;
            try {
                json_string = EntityUtils.toString(resEntityGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                down=new JSONArray(json_string);
                return new JSONArray(json_string).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getURL2;

        }

        @Override
        protected void onPostExecute(String s) {
            dataList2 = new ArrayList<>();
            dataListname = new ArrayList<>();

            for(int i=0; i < down.length(); i++) {
                JSONObject obj= null;
                try {
                    obj = down.getJSONObject(i);
                    String tn = obj.getString("img");
                    dataList2.add(tn);
                    String name = obj.getString("name");
                    dataListname.add(name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mGridView.setAdapter(new AlbumsAdapter(getActivity(), dataList2));
            onchange=1;
        }

    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            // requestPermissions => call onRequestPermissionsResult
        } else {
            gallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    gallery();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    public String thumbnail(String id) {
        Cursor tn_c = getActivity().getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Images.Thumbnails.DATA},
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[] {id},
                null );
        if(tn_c.moveToFirst()) {
            return tn_c.getString(tn_c.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        } else {
            MediaStore.Images.Thumbnails.getThumbnail(
                    getActivity().getContentResolver(),
                    Long.parseLong(id),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null );
            tn_c.close();
            return thumbnail(id);
        }
    }

    public void gallery(){
        dataList1 = new ArrayList<String>();
        dataList2 = new ArrayList<String>();
        dataListname = new ArrayList<String>();
        onchange=0;
        Cursor c = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null );

        if(c.moveToFirst()) do {
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            dataList1.add(data);

            String id = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
            dataListname.add(id);
            String tn = thumbnail(id);
            dataList2.add(tn);
        } while (c.moveToNext());

        c.close();
        mGridView.setAdapter(new AlbumsAdapter(getActivity(), dataList2));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long id) {
                new AlertDialog.Builder(getActivity()).setTitle("업로드").setMessage("선택하신 사진을 업로드 하시겠습니까?").setIcon(android.R.drawable.ic_input_add)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (AccessToken.getCurrentAccessToken()!=null)master= AccessToken.getCurrentAccessToken().getUserId();
                                if (master==null) master="JJJ";

                                new UploadTask().execute("http://52.231.69.25:8080/api/photos", dataListname.get(position),dataList2.get(position));
                                Log.i("master",dataListname.get(position)+dataList2.get(position));

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                      uploadingicon.setVisibility(View.VISIBLE);
                                    }
                                }, 1500);  //
                            }
                        })
                        .setNegativeButton("아니오", null).show();


                //Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                //i.putExtra("id", position);
                //startActivity(i);
            }
        });
    }



    private  class UploadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try{

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name",params[1]);
                jsonObject.accumulate("image",params[2]);
                jsonObject.accumulate("master",master);
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(params[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();
                    OutputStream outStream = con.getOutputStream();

                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(con!=null){
                        con.disconnect();
                    }
                    try{
                        if(reader!=null){
                            reader.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }





}
