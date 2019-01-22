package doh.nvbsp.imu.dugongbayani.lib.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.ShowServerResponse;

public class ApiCallManager {

    private Activity activity;
    private RequestQueue requestQueue;
    private Gson gson;
    private Session session;

    public ApiCallManager(Activity activity) {
        this.activity = activity;
        this.gson = new Gson();
        this.requestQueue = Volley.newRequestQueue(activity);
        this.session = new Session(activity);
    }

    private String getDomainName(){
        String domain = session.getDomain();
        if(domain == null || domain.equals("")){
            return activity.getResources().getString(R.string.api_domain);
        }
        return domain;
    }

    private void checkConnection(final Callback CALLBACK){

        ConnectivityManager conMgr =  (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            LayoutInflater inflater = LayoutInflater.from(activity);
            final AlertDialog DIALOG = new AlertDialog.Builder(activity).create();
            DIALOG.setTitle(activity.getResources().getString(R.string.internet_error_title));
            View customView = inflater.inflate(R.layout.network_fail,null);
            DIALOG.setView(customView);
            DIALOG.setCancelable(false);
            DIALOG.setButton(AlertDialog.BUTTON_POSITIVE, "TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DIALOG.dismiss();
                    checkConnection(CALLBACK);
                }
            });

            DIALOG.setOnShowListener(new DialogInterface.OnShowListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    DIALOG.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimary);
                }
            });
            DIALOG.show();
        }else{
            CALLBACK.execute();
        }
    }

    public void get(final String URL, final CallbackWithResponse CALLBACK, @Nullable final Callback ON_ERROR_CALLBACK){
        checkConnection(new Callback() {
            @Override
            public void execute() {

                requestQueue.add(
                        new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CALLBACK.execute(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(activity, "Poor Connection Detected..", Toast.LENGTH_SHORT).show();
//                                handleAPIExceptionResponse(error);
                                if(ON_ERROR_CALLBACK != null){
                                    ON_ERROR_CALLBACK.execute();
                                }
                            }
                        }
                        )
                );
            }
        });
    }

    public void call(final String URL, final int METHOD, final HashMap<String,String> HEADERS, final CallbackWithResponse CALLBACK, @Nullable final Callback ON_ERROR_CALLBACK){
        checkConnection(new Callback() {
            @Override
            public void execute() {

                requestQueue.add(
                        new JsonObjectRequest(METHOD, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CALLBACK.execute(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleAPIExceptionResponse(error);
                                if(ON_ERROR_CALLBACK != null){
                                    ON_ERROR_CALLBACK.execute();
                                }
                            }
                        }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                return HEADERS;
                            }
                        }
                );
            }
        });
    }

    void executeApiCall(final String URL, final int METHOD, @Nullable final JSONObject jsonObject, final CallbackWithResponse CALLBACK, @Nullable final Callback ON_ERROR_CALLBACK){
        checkConnection(new Callback() {
            @Override
            public void execute() {

                requestQueue.add(
                        new JsonObjectRequest(METHOD, URL, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CALLBACK.execute(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(activity, "Poor Connection Detected..", Toast.LENGTH_SHORT).show();
//                                handleAPIExceptionResponse(error);
                                if(ON_ERROR_CALLBACK != null){
                                    ON_ERROR_CALLBACK.execute();
                                }
                            }
                        }
                        )
                );
            }
        });
    }

    private void handleAPIExceptionResponse(VolleyError error){
        NetworkResponse networkResponse = error.networkResponse;

        Intent intent = new Intent(activity,ShowServerResponse.class);
        if(networkResponse == null){
            intent.putExtra("message","NETWORK ERROR \nPlease check your Internet/Wifi settings.");
        }else if(networkResponse.statusCode == 400){
            intent.putExtra("message","NETWORK ERROR 400 \nPlease contact administrator.");
        }else{
            intent.putExtra("message","ERROR      "+networkResponse.statusCode+" " + "Please contact administrator.");        }
        activity.startActivity(intent);
    }
}