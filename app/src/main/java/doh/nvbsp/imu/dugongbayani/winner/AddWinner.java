package doh.nvbsp.imu.dugongbayani.winner;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.AgenciesResponse;
import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.AwardsResponse;
import doh.nvbsp.imu.dugongbayani.lib.datasource.Agencies;
import doh.nvbsp.imu.dugongbayani.lib.datasource.Awards;
import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCallManager;
import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCalls;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Callback;
import doh.nvbsp.imu.dugongbayani.lib.helpers.CallbackWithResponse;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;
import doh.nvbsp.imu.dugongbayani.lib.models.Award;

public class AddWinner extends AppCompatActivity {

    ArrayList<Award> awards;
    ArrayList<Agency> agencies;
    Gson gson;
    Spinner spinner_award;
    Spinner spinner_agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_winner);

        gson = new Gson();
        spinner_award = findViewById(R.id.spinner_award);
        spinner_agency = findViewById(R.id.spinner_agency);

        Button btn_create_winner = findViewById(R.id.btn_create_winner);
        btn_create_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });


        fetchReferences(new Callback() {
            @Override
            public void execute() {
                Collections.sort(agencies,new AgencyComparator());
                ArrayList<String> awardNames = new ArrayList<>();
                for(Award a: awards){
                    awardNames.add(a.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,awardNames);
                spinner_award.setAdapter(adapter);

                ArrayList<String> agencyNames = new ArrayList<>();
                for(Agency a: agencies){
                    agencyNames.add(a.getName());
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,agencyNames);
                spinner_agency.setAdapter(adapter2);
            }
        });
    }

    private class AgencyComparator implements Comparator<Agency>{

        @Override
        public int compare(Agency o1, Agency o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    // TODO: 16/01/2019 Should be optimized
    protected void fetchReferences(final Callback callback){
        fetchAwards(new Callback() {
            @Override
            public void execute() {
                fetchAgencies(callback);
            }
        });
    }

    private void fetchAwards(final Callback callback) {
        awards = Awards.all();
        callback.execute();
    }

    private void fetchAgencies(final Callback callback) {
        agencies = Agencies.all();
        callback.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void validateForm() {

        saveWinner();

    }

    private void saveWinner() {

        Award award = null;
        for(Award a: awards){
            if(a.getName().equals(spinner_award.getSelectedItem().toString())){
                award = a;
            }
        }

        Agency agency = null;
        for(Agency a: agencies){
            if(a.getName().equals(spinner_agency.getSelectedItem().toString())){
                agency = a;
            }
        }

        if(award == null || agency == null){
            Toast.makeText(this, "Error while saving", Toast.LENGTH_SHORT).show();

        }else{
            ApiCalls.addWinner(this, award.getId(), agency.getId(),new CallbackWithResponse() {
                @Override
                public void execute(JSONObject response) {
                    performSuccess();
                }
            });
        }

    }

    private void performSuccess() {
        Toast.makeText(this, "WinnerAward has been saved", Toast.LENGTH_SHORT).show();
//        finish();
    }
}
