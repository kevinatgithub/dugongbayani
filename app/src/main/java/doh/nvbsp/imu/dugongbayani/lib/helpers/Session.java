package doh.nvbsp.imu.dugongbayani.lib.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import doh.nvbsp.imu.dugongbayani.lib.models.User;

public class Session {
    private SharedPreferences prefs;
    private Gson gson;

    public Session(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    public void setUser(User user){
        String jsonValue = gson.toJson(user);
        prefs.edit().putString("user",jsonValue).commit();
    }

    public User getUser(){
        String jsonValue = prefs.getString("user","");
        if(jsonValue.equals("")){
            return null;
        }
        return gson.fromJson(jsonValue,User.class);
    }

    public void removeUser(){
        prefs.edit().remove("user").commit();
    }

    public void setDomain(String domain){
        prefs.edit().putString("domain",domain).commit();
    }

    public String getDomain(){
        String domain = prefs.getString("domain","");
        if(domain.equals("")){
            return null;
        }
        return domain;
    }

    public void removeDomain(){
        prefs.edit().remove("domain").commit();
    }

    public void setSocketServer(String socketServer){
        prefs.edit().putString("socketServer",socketServer).commit();
    }

    public String getSocketServer(String defValue){
        String domain = prefs.getString("socketServer","");
        if(domain.equals("")){
            setSocketServer(defValue);
            return defValue;
        }
        return domain;
    }

    public void removeSocketServer(){
        prefs.edit().remove("socketServer").commit();
    }
}
