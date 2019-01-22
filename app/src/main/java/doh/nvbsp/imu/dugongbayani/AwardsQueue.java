package doh.nvbsp.imu.dugongbayani;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.SocketQueue;
import doh.nvbsp.imu.dugongbayani.lib.adapters.AwardsQueueAdapter;
import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCalls;
import doh.nvbsp.imu.dugongbayani.lib.helpers.CallbackWithResponse;
import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;
import doh.nvbsp.imu.dugongbayani.lib.models.WinnerAward;
import doh.nvbsp.imu.dugongbayani.registration.Registration;

public class AwardsQueue extends AppCompatActivity {

    TextView lbl_prev_agency_name;
    TextView lbl_prev_award;

    TextView lbl_current_agency_name;
    TextView lbl_current_award;
    ImageView img_current_photo;
    TextView lbl_current_recipients;

    ListView lv_nextinline;
    private Gson gson;
    Vibrator vibrator;
    TextToSpeech tts;
    private com.github.nkzawa.socketio.client.Socket mSocket;
    Emitter.Listener queueEmitListener;
    CardView cv_last;
    CardView cv_agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards_queue);

        cv_last = findViewById(R.id.cv_last);
        cv_agency = findViewById(R.id.cv_agency);
        lv_nextinline = findViewById(R.id.lv_nextInLine);

        final Button btn_start = findViewById(R.id.btn_refresh);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSocket = IO.socket(getResources().getString(R.string.socket_server));
                } catch (URISyntaxException e) {}
                mSocket.connect();
                prepareWebSocketListeners();
                btn_start.setVisibility(View.INVISIBLE);
                cv_agency.setVisibility(View.VISIBLE);
                cv_last.setVisibility(View.VISIBLE);
                lv_nextinline.setVisibility(View.VISIBLE);
            }
        });

        gson = new Gson();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        tts.setLanguage(Locale.US);
        tts.setSpeechRate(2f);

        lbl_prev_agency_name = findViewById(R.id.lbl_prev_agency_name);
        lbl_prev_award = findViewById(R.id.lbl_prev_award);
        lbl_current_agency_name = findViewById(R.id.lbl_current_agency_name);
        lbl_current_award = findViewById(R.id.lbl_current_award);
        img_current_photo = findViewById(R.id.img_current_photo);
        lbl_current_recipients = findViewById(R.id.lbl_current_recipients);



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
                        applyQueueData(apiResponse);
                    }
                });
            }
        };

        mSocket.on("queue",queueEmitListener);
    }

    private void applyQueueData(SocketQueue apiResponse) {
        if(apiResponse.getLast() != null){
            lbl_prev_agency_name.setText(apiResponse.getLast().getAgency());
            lbl_prev_award.setText(apiResponse.getLast().getAward());
        }

        if(apiResponse.getNextInLine() != null){
            if(apiResponse.getNextInLine().size() > 0){
                SocketAward current = apiResponse.getNextInLine().get(0);

                if(current.getId() != currentId){
                    notifyUserOfChange();
                    currentId = current.getId();
                }
                lbl_current_agency_name.setText(current.getAgency());
                lbl_current_award.setText(current.getAward());
                if(current.getPhoto() != null){
                    String base64Image = current.getPhoto();
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    img_current_photo.setImageBitmap(decodedByte);

                }
                lbl_current_recipients.setText(current.getRecipients());
            }

            ArrayList<SocketAward> nil = new ArrayList<>();
            for(int i = 1; i < apiResponse.getNextInLine().size(); i++){
                nil.add(apiResponse.getNextInLine().get(i));
            }

            AwardsQueueAdapter awardsQueueAdapter = new AwardsQueueAdapter(getApplicationContext(),nil);
            lv_nextinline.setAdapter(awardsQueueAdapter);
        }
    }

    private String currentId = null;

    private void notifyUserOfChange() {
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            tts.speak("Queue changed", TextToSpeech.QUEUE_ADD,null);
        }
    }
}
