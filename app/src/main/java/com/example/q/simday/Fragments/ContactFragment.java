package com.example.q.simday.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.q.simday.Adapters.RecyclerAdapter;
import com.example.q.simday.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.q.simday.R.drawable.contacticon;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
         checkandRequestPermissions();
         adapter = new RecyclerAdapter(getUserInformation(), getActivity());
         recyclerView.setAdapter(adapter);
         callbuttons = (Button)v.findViewById(R.id.callbutton);
         messagebutton = (Button)v.findViewById(R.id.messagebutton);
        return v;
    }

    public class SaveContact {

        OkHttpClient client = new OkHttpClient();
        String put(String url, File file, String name, String number) throws IOException{
        RequestBody formBody;
        if(file!=null){
            String filenameArray[] = file.getName().split("\\.");
            String ext = filenameArray[filenameArray.length - 1];
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("number", number)
                    .addFormDataPart("profile_image", file.getName(), RequestBody.create(MediaType.parse("image/" + ext), file))
                    .build();
        } else {
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("number", number)
                    .build();
        }

            Request request = new Request.Builder().url(url).put(formBody).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }

    }



    private List<User> getUserInformation() {
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                profilepic = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                userList.add(new User(contacticon, name, phonenumber, "haha"));
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

    public class User {
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



}
