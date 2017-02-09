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
import com.frendz.testingapi.utils.ConstantStore;
import com.frendz.testingapi.utils.EditTextCustom;
import com.frendz.testingapi.utils.NetworkConnection;
import com.frendz.testingapi.utils.ServiceHandler;
import com.frendz.testingapi.utils.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditTextCustom edtUsername, edtPassword;
    String strUsername, strPassword;
    String str_user_id, str_user_name, str_user_email_address, str_user_password, str_user_avtar, str_user_phone;
    ButtonCustom btnSignin;
    NetworkConnection nw;
    ProgressDialog prgDialog;
    Boolean netConnection = false;
    String postdata;
    String jsonResponse;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditTextCustom) findViewById(R.id.activity_login_edt_Username);
        edtPassword = (EditTextCustom) findViewById(R.id.activity_login_edt_Password);
        btnSignin = (ButtonCustom) findViewById(R.id.activity_login_btn_login);

        nw = new NetworkConnection(getApplicationContext());
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        sm = new SessionManager(LoginActivity.this);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLogin();
            }
        });
    }


    private void OpenLogin() {

        strUsername = edtUsername.getText().toString();
        strPassword = edtPassword.getText().toString();

        if (nw.isConnectingToInternet() == true) {
            try {
                if (ValidateUser.isNotNull(strUsername) && (ValidateUser.isNotNull(strPassword)))
                {
                    Log.e("loginURL", "loginURL");
                    JSONObject loginobj = new JSONObject();
                    loginobj.put("username",strUsername);
                    loginobj.put("password",strPassword);
                    postdata=loginobj.toString();

                    Log.e("ENTITY",postdata);
                    new loginOperation().execute();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill entire form.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } catch (Exception ex) {

            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Internet is not available. Please turn on and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    // Login Operation API
    private class loginOperation extends AsyncTask<String, Void, Void> {
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
                    HttpPost httppost = new HttpPost("http://trinityworld.co.in/Serv/UserLogin.php");

                    //HttpPost httppost = new HttpPost("http://trinityworld.co.in/Serv/UserLogin.php?");

                    httppost.setEntity(new StringEntity(postdata));
                    Log.d("Postdata", postdata);
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
                        JSONObject jslogin = js.getJSONObject("info");

                        str_user_id = jslogin.getString("uid");
                        str_user_name = jslogin.getString("fname");
                        str_user_email_address = jslogin.getString("email");
                        str_user_password = jslogin.getString("password");
                        str_user_phone = jslogin.getString("Mobile_No");
                        str_user_avtar = jslogin.getString("userimg");

                        Log.e("str_user_id", "" + str_user_id);
                        Log.e("str_user_name", "" + str_user_name);
                        Log.e("str_user_email_address", "" + str_user_email_address);
                        Log.e("str_user_password", "" + str_user_password);
                        Log.e("str_user_avtar", "" + str_user_avtar);
                        Log.e("str_user_phone", "" + str_user_phone);

                    }


                    /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("username", strUsername));
                    nameValuePairs.add(new BasicNameValuePair("password", strPassword));
                    ServiceHandler sh = new ServiceHandler();

                    String response = sh.makeServiceCall(ConstantStore.API_URL, ServiceHandler.POST,
                            nameValuePairs);

                    Log.e("response", response);

                    JSONObject js = new JSONObject(response);
                    status = js.getString("success");
                    Log.e("status", status);

                    if (status.contains("0"))
                    {
                        message = js.getString("msg");
                    }
                    else
                    {
                        message = js.getString("msg");
                        JSONObject jslogin = js.getJSONObject("info");

                        str_user_id = jslogin.getString("uid");
                        str_user_name = jslogin.getString("fname");
                        str_user_email_address = jslogin.getString("email");
                        str_user_password = jslogin.getString("password");
                        str_user_phone = jslogin.getString("Mobile_No");
                        str_user_avtar = jslogin.getString("userimg");

                        Log.e("str_user_id", "" + str_user_id);
                        Log.e("str_user_name", "" + str_user_name);
                        Log.e("str_user_email_address", "" + str_user_email_address);
                        Log.e("str_user_password", "" + str_user_password);
                        Log.e("str_user_avtar", "" + str_user_avtar);
                        Log.e("str_user_phone", "" + str_user_phone);

                    }*/
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
                    sm.createLoginSession(str_user_id, str_user_name, str_user_email_address, str_user_avtar, str_user_phone);
                    Intent signupintent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(signupintent);
                    finish();
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
