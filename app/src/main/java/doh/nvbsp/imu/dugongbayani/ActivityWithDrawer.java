package doh.nvbsp.imu.dugongbayani;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.lib.datasource.Agencies;
import doh.nvbsp.imu.dugongbayani.lib.datasource.Awards;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Callback;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Session;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;
import doh.nvbsp.imu.dugongbayani.lib.models.Award;
import doh.nvbsp.imu.dugongbayani.lib.models.User;
import doh.nvbsp.imu.dugongbayani.registration.Registration;
import doh.nvbsp.imu.dugongbayani.winner.Winners;

public class ActivityWithDrawer extends AppCompatActivity {

    protected Agency agency;
    private Session session;
    private Gson gson;
    protected ArrayList<Agency> agencies;
    protected ArrayList<Award> awards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);
        gson = new Gson();

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        DrawerLayout dl_patient_list = findViewById(R.id.dl_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,dl_patient_list,toolbar, R.string.drawer_open,R.string.drawer_closed);
        dl_patient_list.addDrawerListener(toggle);

        provideDrawerOnClickListeners();
        if(!session.getUser().getRole().toUpperCase().equals("ORGANIZER")){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nv_drawer);
            Menu navMenu = navigationView.getMenu();
//            navMenu.getItem(0).getSubMenu().getItem(2).setVisible(false);
            navMenu.getItem(2).setVisible(false);
        }
    }

    protected void provideDrawerOnClickListeners() {
        NavigationView navView = findViewById(R.id.nv_drawer);
        View headerView = navView.getHeaderView(0);

        TextView txt_drawer_user_fullname = headerView.findViewById(R.id.txt_drawer_user_fullname);
        TextView txt_drawer_user_designation = headerView.findViewById(R.id.txt_drawer_user_designation);
        User user = session.getUser();
        txt_drawer_user_fullname.setText(user.getName());
        txt_drawer_user_designation.setText(user.getRole());

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.nav_register:
                        Intent intent = new Intent(getApplicationContext(),Registration.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_queue:
                        Intent intent3 = new Intent(getApplicationContext(),ManageQueue.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.nav_winners:
                        Intent intent2 = new Intent(getApplicationContext(),Winners.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_logout:
                        confirmLogout();
                        break;
                }
                return false;
            }
        });


    }

    private void confirmLogout() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Logout");
        dialog.setMessage("Do you want to logout?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                performLogout();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void performLogout() {
            session.removeUser();
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }

    private boolean isBackButtonPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (isBackButtonPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.isBackButtonPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                isBackButtonPressedOnce =false;
            }
        }, 2000);
    }
}
