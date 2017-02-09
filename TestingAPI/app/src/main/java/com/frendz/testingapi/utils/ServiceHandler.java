package com.frendz.testingapi.utils;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Ratilal on 5/2/2015.
 */
public class ServiceHandler {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    //photo upload
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String s=null;
    public String url = null;
    public String param = null;
    public String filepath = null;
    public String requestString=null;
    public String url1 = null;
    public String param1 = null;
    public String filepath1 = null;
    public String requestString1=null;
    public String filepath2 = null;
    public String requestString2=null;
    public String url2 = null;


    OkHttpClient client = new OkHttpClient();

    String doGetRequest(String url) throws IOException
    {
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public ServiceHandler() {

    }

    /*
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /*
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                Log.e("in POST", "in POST");
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    Log.e("in POST params", "in POST params");
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                Log.e("url in post service", url);
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                Log.e("in GET", "in GET");
                if (params != null) {
                    Log.e("in GET params", "in GET params");
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                Log.e("url in get service", url);
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         return response;

    }
    public String makeServiceCallIMAGE(String url, int method,
                                       List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));

                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    /*********************************** Profile Update Image Upload Code Start *****************************/

    public Response run() throws Exception{
        Log.e("img","image"+filepath);

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"user_avtar\";filename=\"10566design.jpg\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filepath)) )
                        //Headers.of("Content-Disposition", "form-data; name=\"status_image\""),
                        //RequestBody.create(MEDIA_TYPE_PNG, new File(filepath)))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        Log.e(""," res : "+ response.priorResponse());
        Log.e("", " res : " + response.networkResponse().toString());
        return response;
    }

    public Response uploadImage(String tempURL,String tempFilePath,String param1/*,String tempRequestString*/) throws Exception {
        url      =  tempURL;
        filepath =  tempFilePath;
        param    =  param1;
        //requestString= tempRequestString;

        Response serviceresponse = run();
        return serviceresponse;
    }

    /*********************************** Profile Update Image Upload Code End *****************************/


    /*********************************** Event Image Upload Code Start *****************************/

    public Response run1() throws Exception{
        Log.e("img","image"+filepath1);

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"event_pic\";filename=\"10567design.jpg\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filepath1)) )
                        //Headers.of("Content-Disposition", "form-data; name=\"status_image\""),
                        //RequestBody.create(MEDIA_TYPE_PNG, new File(filepath)))
                .build();

        Request request = new Request.Builder()
                .url(url1)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        Log.e(""," res : "+ response.priorResponse());
        Log.e("", " res : " + response.networkResponse().toString());
        return response;
    }

    public Response uploadImageEvent(String tempURL,String tempFilePath,String param1/*,String tempRequestString*/) throws Exception {
        url1     =  tempURL;
        filepath1 =  tempFilePath;
        param1    =  param1;
        //requestString= tempRequestString;

        Response serviceresponse = run1();
        return serviceresponse;
    }

    /*********************************** Event Image Upload Code Code *****************************/



    /*********************************** Participant Image Upload Code Start *****************************/

    public Response run2() throws Exception{
        Log.e("img","image"+filepath2);

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"participant_pic\";filename=\"10568design.jpg\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(filepath2)) )
                        //Headers.of("Content-Disposition", "form-data; name=\"status_image\""),
                        //RequestBody.create(MEDIA_TYPE_PNG, new File(filepath)))
                .build();

        Request request = new Request.Builder()
                .url(url2)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        Log.e(""," res : "+ response.priorResponse());
        Log.e("", " res : " + response.networkResponse().toString());
        return response;
    }

    public Response participantImage(String tempURL,String tempFilePath,String param1/*,String tempRequestString*/) throws Exception {
        url2     =  tempURL;
        filepath2 =  tempFilePath;
        param1    =  param1;
        //requestString= tempRequestString;

        Response serviceresponse = run2();
        return serviceresponse;
    }

    /*********************************** Participant Image Upload Code End *****************************/

}
