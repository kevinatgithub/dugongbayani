package doh.nvbsp.imu.dugongbayani.lib.helpers;

import org.json.JSONObject;

public interface CallbackWithResponse{

    void execute(JSONObject response);
}