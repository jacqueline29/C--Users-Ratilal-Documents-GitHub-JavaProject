package com.frendz.testingapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.frendz.testingapi.utils.ButtonCustom;
import com.frendz.testingapi.utils.EditTextCustom;
import com.frendz.testingapi.utils.NetworkConnection;
import com.frendz.testingapi.utils.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    EditTextCustom edtPFname,edtPFmobile,edtPFemail;
    ButtonCustom btnUpdate;
    NetworkConnection nw;
    ProgressDialog prgDialog;
    Boolean netConnection = false;
    SessionManager sm;
    HashMap<String,String> getLoginDetail;
    String str_pfsername,str_profileemail,str_Contactprofile;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    String jsonResponse;
    String updatedata;
    CircleImageView Profileimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtPFname = (EditTextCustom) findViewById(R.id.activity_profile_edt_Username);
        edtPFemail = (EditTextCustom) findViewById(R.id.activity_profile_edt_email);
        edtPFmobile = (EditTextCustom) findViewById(R.id.activity_profile_edt_mobile);
        btnUpdate = (ButtonCustom) findViewById(R.id.activity_login_btn_login);
        Profileimg = (CircleImageView) findViewById(R.id.img_update_slider_Profile);

        nw = new NetworkConnection(ProfileActivity.this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        sm = new SessionManager(ProfileActivity.this);
        getLoginDetail=sm.getUserDetails();


        Log.e("Login Detail", getLoginDetail.get(SessionManager.KEY_AVATAR));
        edtPFname.setText(getLoginDetail.get(SessionManager.KEY_USERNAME));
        edtPFemail.setText(getLoginDetail.get(SessionManager.KEY_EMAIL));
        edtPFmobile.setText(getLoginDetail.get(SessionManager.KEY_PHONE));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true).build();

        imageLoader.displayImage(getLoginDetail.get(SessionManager.KEY_AVATAR), Profileimg, options);

    }


    private void UpdateProfile() {

        str_pfsername = edtPFname.getText().toString();
        str_profileemail = edtPFemail.getText().toString();
        str_Contactprofile = edtPFmobile.getText().toString();

        if(nw.isConnectingToInternet()==true){
            try{
                if(ValidateUser.isNotNull(str_pfsername) && ValidateUser.isNotNull(str_profileemail)
                        && ValidateUser.isNotNull(str_Contactprofile)){

                    if(ValidateUser.isValidMobile(str_Contactprofile))
                    {
                        Log.e("loginURL", "loginURL");
                        JSONObject updateobj = new JSONObject();
                        updateobj.put("UserId",getLoginDetail.get(SessionManager.KEY_USER_ID));
                        updateobj.put("Name",str_pfsername);
                        updateobj.put("MobileNo",str_Contactprofile);
                        updatedata=updateobj.toString();

                        Log.e("ENTITY",updatedata);

                        new UpdateCustomerProfileOperation().execute();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter correct phone number", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill the entire form", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            catch (Exception ex){
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Internet is not available. Please turn on and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }



    private class UpdateCustomerProfileOperation extends AsyncTask<String, Void, Void> {
        String status, message;

        @Override
        protected void onPreExecute() {

            prgDialog.setMessage("Logging...");
            prgDialog.show();
        }

        @Override
        protected Void doInBackground(String... urls) {

            if (nw.isConnectingToInternet() == true) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://trinityworld.co.in/Serv/UpdateUserProfile.php");

                    //HttpPost httppost = new HttpPost("http://trinityworld.co.in/Serv/UserLogin.php?");

                    httppost.setEntity(new StringEntity(updatedata));
                    Log.d("Postdata", updatedata);
                    HttpResponse response = httpclient.execute(httppost);
                    Log.d("", ""+response);
                    if (response.getStatusLine().getStatusCode() != org.apache.http.HttpStatus.SC_OK)
                    {
                        Log.i("SEND", "send ok1 " + response.getStatusLine());
                    }
                    else
                    {
                        Log.i("SEND", "send ok " + response.getStatusLine());
                    }
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null)
                    {
                        Log.e("HttpResponse", line);
                        jsonResponse = line.toString();
                    }

                    JSONObject js = new JSONObject(jsonResponse);
                    status = js.getString("success");
                    if (status.contains("0"))
                    {
                        message = js.getString("msg");
                    }
                    else
                    {
                        message = js.getString("msg");

                    }

                } catch (Exception ex) {

                }
                netConnection = true;
            } else {
                netConnection = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            prgDialog.dismiss();
            if (netConnection == false) {
                Toast toast = Toast.makeText(getApplicationContext(), "Internet is not available. Please turn on and try again.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                if (status.contains("1"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
            super.onPostExecute(result);
        }
    }

}
