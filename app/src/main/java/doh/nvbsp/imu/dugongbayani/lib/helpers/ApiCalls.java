package doh.nvbsp.imu.dugongbayani.lib.helpers;

import android.app.Activity;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;

public class ApiCalls {

    private static String getDomainName(Activity activity){
        Session session = new Session(activity);
        String domain = session.getDomain();
        if(domain == null || domain.equals("")){
            return activity.getResources().getString(R.string.api_domain);
        }
        return domain;
    }

    public static void login(Activity activity,String username, String password, CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_login);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.executeApiCall(url,Request.Method.POST,jsonObject,callback,null);
    }

    public static void fetchAwards(Activity activity,CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_awards);
        api.get(url,callback,null);
    }

    public static void fetchAgencies(Activity activity,CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_agencies);
        api.get(url,callback,null);
    }

    public static void fetchWinners(Activity activity, CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_winners);
        api.get(url,callback,null);
    }

    public static void fetchAwardByAgency(Activity activity, Agency agency,CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_winner_award_by_agency)+"/"+agency.getId();
        api.get(url,callback,null);
    }

    public static void registerRecipient(Activity activity, String id, String recipients, String photo, boolean prioritize,CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_winner_award_register);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("recipients",recipients);
            jsonObject.put("photo",photo);
            jsonObject.put("prioritize",prioritize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.executeApiCall(url,Request.Method.POST,jsonObject,callback,null);
    }

    public static void addWinner(Activity activity, String award_id, String agency_id, CallbackWithResponse callback){
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_winners);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("award_id",award_id);
            jsonObject.put("agency_id",agency_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.executeApiCall(url,Request.Method.POST,jsonObject,callback,null);
    }

    public static void fetchQueue(Activity activity,CallbackWithResponse callback) {
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_queue);

        api.get(url,callback,null);
    }

    public static void fetchManageQueue(Activity activity,CallbackWithResponse callback) {
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_manage_queue);
        api.get(url,callback,null);
    }

    public static void completeNext(Activity activity,CallbackWithResponse callback) {
        ApiCallManager api = new ApiCallManager(activity);
        String url = getDomainName(activity) + activity.getResources().getString(R.string.api_complete_next);
        api.get(url,callback,null);
    }
}
