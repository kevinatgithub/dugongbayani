package doh.nvbsp.imu.dugongbayani.registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import doh.nvbsp.imu.dugongbayani.ActivityWithDrawer;
import doh.nvbsp.imu.dugongbayani.R;
import doh.nvbsp.imu.dugongbayani.lib.JsonResponse.SocketQueue;
import doh.nvbsp.imu.dugongbayani.lib.models.SocketAward;

public class Registration extends ActivityWithDrawer {

    Spinner spinner_agency;

    CardView cv_agency;
    CardView cv_award_details;
    LinearLayout ll_loading;
    TextView lbl_agency_name;
    TextView lbl_seats;
    TextView lbl_table;
    TextView lbl_award;
    EditText txt_recipient;
    ImageView img_photo;
    private String base64converted;
    private SocketAward socketAward;
    private Gson gson;
    private ArrayList<SocketAward> socketAwards = new ArrayList<>();
    private com.github.nkzawa.socketio.client.Socket mSocket;
    Emitter.Listener queueEmitListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.registration_container);
        super.onCreate(savedInstanceState);

        cv_agency = findViewById(R.id.cv_agency);
        gson = new Gson();

        spinner_agency = findViewById(R.id.spinner_agency);
        cv_award_details = findViewById(R.id.cv_award_details);
        ll_loading = findViewById(R.id.ll_loading);

        lbl_agency_name = findViewById(R.id.lbl_prev_agency_name);
        lbl_seats = findViewById(R.id.lbl_recipients);
        lbl_table = findViewById(R.id.lbl_table);
        lbl_award = findViewById(R.id.lbl_current_award);
        txt_recipient = findViewById(R.id.txt_recipient);
        img_photo = findViewById(R.id.img_photo);
        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        spinner_agency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) spinner_agency.getItemAtPosition(position);
                if(value.length() > 0){
                    SocketAward award = new SocketAward(null,null,null,null,null,null,null,false);
                    for(SocketAward a: socketAwards){
                        if(a.getAgency().equals(value)){
                            award = a;
                        }
                    }
                    applyFieldValues(award);
                    base64converted = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void listenToWebSocket() {
        try {
            mSocket = IO.socket(getResources().getString(R.string.socket_server));
        } catch (URISyntaxException e) {}
        if(mSocket.hasListeners("queue")){
            mSocket.disconnect();
            mSocket.off("queue");
        }
        prepareWebSocketListeners();
        mSocket.connect();
        cv_agency.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        listenToWebSocket();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSocket != null){
            mSocket.off();
            mSocket.disconnect();
            mSocket.off("queue",queueEmitListener);
        }
    }

    private boolean agencyLoaded = false;
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
                        if(!agencyLoaded){
                            applyAgencies(apiResponse.getData());
                            agencyLoaded = true;
                        }
                    }
                });
            }
        };

        mSocket.on("queue",queueEmitListener);
    }



    private void applyFieldValues(SocketAward socketAward) {
        this.socketAward = socketAward;
        lbl_agency_name.setText(socketAward.getAgency());
        lbl_seats.setText(socketAward.getSeats() + " seats alloted");
        lbl_table.setText("Table No. " +socketAward.getTableAssignment());
        lbl_award.setText(socketAward.getAward());
        txt_recipient.setText(socketAward.getRecipients());
        if(socketAward.getPhoto() != null){
            String base64Image = socketAward.getPhoto();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_photo.setImageBitmap(decodedByte);
            base64converted = socketAward.getPhoto();
        }else{
            img_photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
        }
        cv_award_details.setVisibility(View.VISIBLE);
    }

    private void register(){
        if(socketAward != null){
            if(txt_recipient.getText().length()> 0 && base64converted != null){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",socketAward.getId());
                    jsonObject.put("recipients",txt_recipient.getText().toString());
                    jsonObject.put("photo",base64converted);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                agency = null;
                base64converted = null;
                cv_award_details.setVisibility(View.INVISIBLE);
                spinner_agency.setSelection(0);
                img_photo.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));
                Toast.makeText(Registration.this, "Recipient of the award has been registered", Toast.LENGTH_SHORT).show();

                mSocket.emit("register",jsonObject);
            }else{
                Toast.makeText(this, "Please enter recipients name/s and Take Photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void applyAgencies(ArrayList<SocketAward> awards){
        Collections.sort(awards,new SocketAwardComparator());
        List<String> agencyNames = new ArrayList<>();
        agencyNames.add("");
        for (SocketAward a: awards){
            agencyNames.add(a.getAgency());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,agencyNames);
        spinner_agency.setAdapter(arrayAdapter);
    }

    private class SocketAwardComparator implements  Comparator<SocketAward>{

        @Override
        public int compare(SocketAward o1, SocketAward o2) {
            return o1.getAgency().compareTo(o2.getAgency());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap bitmap = getResizedBitmap(imageBitmap,100);
            img_photo.setImageBitmap(bitmap);
            performBase64Convertion(bitmap);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void performBase64Convertion(final Bitmap imageBitmap) {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] b = baos.toByteArray();

                return Base64.encodeToString(b,Base64.NO_WRAP);
            }

            @Override
            protected void onPostExecute(String s) {
                base64converted = s;
                super.onPostExecute(s);
            }
        }.execute();
    }
}
