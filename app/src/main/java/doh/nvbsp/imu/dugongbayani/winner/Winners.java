package doh.nvbsp.imu.dugongbayani.winner;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import doh.nvbsp.imu.dugongbayani.ActivityWithDrawer;
import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.adapters.WinnerAdapter;
import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCalls;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Callback;
import doh.nvbsp.imu.dugongbayani.lib.helpers.CallbackWithResponse;
import doh.nvbsp.imu.dugongbayani.lib.models.Agency;
import doh.nvbsp.imu.dugongbayani.lib.models.Award;
import doh.nvbsp.imu.dugongbayani.lib.models.WinnerAward;

public class Winners extends ActivityWithDrawer {

    private Gson gson;
    ListView lv_winners;
    private ArrayList<Winners> winners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.winner_list_container);
        super.onCreate(savedInstanceState);

        gson = new Gson();
        lv_winners = findViewById(R.id.lv_winners);

        updateList();

        FloatingActionButton fab_add_winner = findViewById(R.id.fab_new_winner);
        fab_add_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddWinner.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateList();
    }

    private void updateList() {
        // TODO: 22/01/2019 make websocket
//        fetchReferences(new Callback() {
//            @Override
//            public void execute() {
//                fetchWinners();
//            }
//        });
    }

    class WinnersResponse{

        ArrayList<WinnerAward> data;
    }

    private void fetchWinners(){
        ApiCalls.fetchWinners(this, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                populateWinners(gson.fromJson(response.toString(),WinnersResponse.class).data);
            }
        });
    }

    protected void populateWinners(ArrayList<WinnerAward> winnerAwards){

        for(WinnerAward w: winnerAwards){
            for(Agency agency: agencies){
                if(agency.getId().equals(w.getAgency_id())){
                    w.setAgency(agency);
                }
            }

            for(Award award: awards){
                if(award.getId().equals(w.getAward_id())){
                    w.setAward(award);
                }
            }
        }

        Collections.sort(winnerAwards,new WinnersComparator());
        WinnerAdapter winnerAdapter = new WinnerAdapter(getApplicationContext(), winnerAwards);
        lv_winners.setAdapter(winnerAdapter);
    }

    public class WinnersComparator implements Comparator<WinnerAward>
    {
        public int compare(WinnerAward left, WinnerAward right) {
            return left.getOrder_no().compareTo(right.getOrder_no());
        }
    }
}
