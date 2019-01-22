package doh.nvbsp.imu.dugongbayani.agency;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.ActivityWithDrawer;
import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.adapters.AgencyAdapter;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;

public class Agencies extends ActivityWithDrawer {

    ListView lv_agencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.agency_list_container);
        super.onCreate(savedInstanceState);

    }
}
