package doh.nvbsp.imu.dugongbayani;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.SocketQueue;
import doh.nvbsp.imu.dugongbayani.lib.adapters.ManageQueueAdapter;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Session;
import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;

public class ManageQueue extends ActivityWithDrawer {

    FloatingActionButton fab_queue;
    ListView lv_queue;
    private Gson gson;
    Vibrator vibrator;
    Session session;
    private com.github.nkzawa.socketio.client.Socket mSocket;
    ArrayList<SocketAward> socketAwards = new ArrayList<>();
    private Emitter.Listener queueEmitListener;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.manage_queue_container);
        super.onCreate(savedInstanceState);

        gson = new Gson();
        session = new Session(this);
        vibrator  = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        fab_queue = findViewById(R.id.fab_next);
        fab_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNext();
            }
        });

        lv_queue = findViewById(R.id.lv_queue);

        if(!session.getUser().getRole().toUpperCase().equals("ORGANIZER")){
            fab_queue.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareWebSocketListeners() {
        queueEmitListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        Gson gson = new Gson();
                        SocketQueue apiResponse = gson.fromJson(data.toString(),SocketQueue.class);
                        socketAwards = apiResponse.getData();
                        ArrayList<SocketAward> qualified = new ArrayList<>();
                        for(SocketAward s: socketAwards){
                            if(s.getRecipients() != null && s.isCompleted() == false){
                                qualified.add(s);
                            }
                        }
                        ManageQueueAdapter manageQueueAdapter = new ManageQueueAdapter(getApplicationContext(),qualified);
                        lv_queue.setAdapter(manageQueueAdapter);
                    }
                });
            }
        };

        mSocket.on("queue",queueEmitListener);
    }

    private void confirmNext() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Confirm Receive of Award?");
        dialog.setMessage("By proceeding, you confirm the receive of award by the awardee and the Queue will move.");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "AGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                performNext();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "DISAGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void performNext() {
        mSocket.emit("next","");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            mSocket = IO.socket(session.getSocketServer(getResources().getString(R.string.socket_server)));
        } catch (URISyntaxException e) {}
        prepareWebSocketListeners();
        mSocket.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSocket != null){
            mSocket.disconnect();
            mSocket.off("queue");
        }
    }
}
