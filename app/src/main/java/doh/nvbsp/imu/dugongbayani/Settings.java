package doh.nvbsp.imu.dugongbayani;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import doh.nvbsp.imu.dugongbayani.lib.helpers.Session;

public class Settings extends AppCompatActivity {

    TextInputLayout tl_socketserver;
    EditText txt_socketserver;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tl_socketserver = findViewById(R.id.tl_socketServer);
        txt_socketserver = findViewById(R.id.txt_socketserver);

        String defValue = getResources().getString(R.string.socket_server);
        session = new Session(this);
        String socketServer = session.getSocketServer(defValue);

        txt_socketserver.setText(socketServer);

        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setSocketServer(txt_socketserver.getText().toString());
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });

        Button btn_checkUpdate = findViewById(R.id.btn_checkupdate);
        btn_checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://test.nbbnets.net/db/public/app.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
