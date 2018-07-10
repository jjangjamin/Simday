package com.example.q.simday.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.example.q.simday.Activity.LoginActivity;
import com.example.q.simday.Activity.MainActivity;
import com.example.q.simday.Adapters.RecyclerAdapter;
import com.example.q.simday.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.q.simday.R.drawable.contacticon;
import static com.example.q.simday.R.drawable.ic_launcher_background;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang3.CharEncoding.UTF_8;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
String master;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Cursor cursor;
    String name, phonenumber;
    int profilepic;
    List<User> userList = new ArrayList<>();
    Button callbuttons, messagebutton;
    Button uploadbutton, syncbutton;
    private HashMap<String, Contact> localContact;
    private HashMap<String, Contact> hashed_contact_list;
    private ArrayList<Contact> contact_list;
    private ArrayList<String> name_list;
    private static int POST_SUCCESS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact, container, false);
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
         checkandRequestPermissions();
         adapter = new RecyclerAdapter(getUserInformation(), getActivity());
         recyclerView.setAdapter(adapter);
         callbuttons = (Button)v.findViewById(R.id.callbutton);
         messagebutton = (Button)v.findViewById(R.id.messagebutton);




         localContact = GetContact();
         name_list = new ArrayList<>(localContact.keySet());

         uploadbutton = (Button)v.findViewById(R.id.backup);
         syncbutton = (Button)v.findViewById(R.id.update);

         syncbutton.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view) {

                 SynchronizeServer();
             }
         });




         /*uploadbutton.setOnClickListener(new View.OnClickListener(){
            Cursor cursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            @Override
            public void onClick(View view) {
                if(cursor2.getCount() >0){
                    while (cursor2.moveToNext()){

                        new UploadTask(localContact.get(name)).execute();
                    }
                }



            }
        });*/

        return v;
    }

    public void onActivityResult (int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        SynchronizeServer();
    }





    private List<User> getUserInformation() {

        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                profilepic = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                userList.add(new User(contacticon, name, phonenumber, "haha"));
                new UploadTask().execute("http://52.231.64.79:8080/api/contacts/", name, phonenumber);
            }
            cursor.close();
        }

        return userList;
    }

    private boolean checkandRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
        int permissioncallPhone = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        int permissionReceiveMessage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS);
        int readContacts = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        List<String> listPermissionNeeded = new ArrayList<>();
        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissioncallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionReceiveMessage != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), 1);
            return false;
        }
        return true;
    }

    private void GetContactsIntoArrayList() {
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
         inflater.inflate(R.menu.menu_card_demo, menu);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class User {
        private int imageResourceId;
        private String profileName;
        private String phoneNumber;
        private String emailId;

        public int getImageResourceId() {
            return imageResourceId;
        }
        public String getProfileName() {
            return profileName;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public String getEmailId() {
            return emailId;
        }

        public User(int imageResourceId, String profileName, String phoneNumber, String emailId) {
            this.imageResourceId = imageResourceId;
            this.profileName = profileName;
            this.phoneNumber = phoneNumber;
            this.emailId = emailId;
        }
    }


    public HashMap<String,Contact> GetContact() {
        HashMap<String, Contact> return_hashed = new HashMap<>();
        Cursor c = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            Contact contact = new Contact(name);
            Cursor cursorPhone = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);
            if (cursorPhone.moveToFirst()) {
                contact.setPhone(cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            return_hashed.put(name, contact);
            cursorPhone.close();

        }
        return return_hashed;
    }


    public void SynchronizeServer() {
        new SynchronizeTask().execute();
    }

    private class SynchronizeTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String jsonResponse = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                String urlString = "http://52.231.64.79:8080/api/contacts/:강아지";
                URI url = new URI(urlString);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpGet);
                jsonResponse = EntityUtils.toString(response.getEntity(), UTF_8);
                JSONArray arr = new JSONArray(jsonResponse);
                int datalength = arr.length();
                for (int i = 0; i < arr.length(); i++) {
                    String obj_name = arr.getJSONObject(i).getString("name");
                    String obj_phone = arr.getJSONObject(i).getString("phone");
                    String obj_profileImage = arr.getJSONObject(i).getString("profileImage");
                    obj_name = obj_name.replace('+', ' ');
                    localContact.put(obj_name, new Contact(obj_name, obj_phone, obj_profileImage));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

        private  class UploadTask extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... params) {
                try{


                    Intent master2 = getActivity().getIntent();
                    master = master2.getExtras().getString("master");
                    Log.d("master","222222222"+ master);


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("name",params[1]);
                    jsonObject.accumulate("number",params[2]);
                    jsonObject.accumulate("master",master);
                    jsonObject.accumulate("photo","");
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





        /*@Override
            protected String doInBackground(String... params) {
                String jsonResponse = "";
                Log.d("**************", "aaaaaaaaaaaaaaa");
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String urlString = params[0];
                    URI url = new URI(urlString);
                    HttpPost httpPost = new HttpPost(url);
                    List<NameValuePair> params2 = new ArrayList<>();
                    params2.add(new BasicNameValuePair("master", "hh"));
                    params2.add(new BasicNameValuePair("name", params[1]));
                    params2.add(new BasicNameValuePair("phone", params[2]));
                    //params2.add(new BasicNameValuePair("profileImage", UploadContact.profileImage));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params2, UTF_8);
                    httpPost.setEntity(ent);
                    HttpResponse response = httpClient.execute(httpPost);
                    jsonResponse = EntityUtils.toString(response.getEntity(), UTF_8);
                    JSONObject obj = new JSONObject(jsonResponse);
                    return obj.get("result").toString();
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Couldn't Connect to Server", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "0";
            }*/

/*
            @Override
            protected void onPostExecute(Object o) {
                if ((int) o == POST_SUCCESS) {
                    Toast.makeText(getActivity(), "success to post", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "error to post", Toast.LENGTH_SHORT).show();
                }
            }*/



}