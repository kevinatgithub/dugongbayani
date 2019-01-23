package doh.nvbsp.imu.dugongbayani;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCallManager;
import doh.nvbsp.imu.dugongbayani.lib.helpers.ApiCalls;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Callback;
import doh.nvbsp.imu.dugongbayani.lib.helpers.CallbackWithResponse;
import doh.nvbsp.imu.dugongbayani.lib.helpers.Session;
import doh.nvbsp.imu.dugongbayani.lib.models.User;
import doh.nvbsp.imu.dugongbayani.registration.Registration;

public class Login extends AppCompatActivity {

    TextInputLayout tl_username;
    TextInputLayout tl_password;
    EditText txt_username;
    EditText txt_password;

    Session session;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);
        gson = new Gson();

        User sUser = session.getUser();
        if(sUser != null ){
            Toast.makeText(this, "Welcome back " + sUser.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),Registration.class);
            startActivity(intent);
            finish();
        }

        tl_username = findViewById(R.id.tl_username);
        tl_password = findViewById(R.id.tl_password);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(new Callback() {
                    @Override
                    public void execute() {
                        performLogin();
                    }
                });
            }
        });

        Button btn_queue = findViewById(R.id.btn_queue);
        btn_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AwardsQueue.class);
                startActivity(intent);
            }
        });

        Button btn_settings = findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void performLogin() {
        String username = txt_username.getText().toString();
        String password = txt_password.getText().toString();

        ApiCalls.login(this, username, password, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                LoginResponse res = gson.fromJson(response.toString(),LoginResponse.class);
                if(res.data == null){
                    tl_username.setError("Username is not registered");
                    Toast.makeText(Login.this, "Login failed! Check Username/Password", Toast.LENGTH_SHORT).show();
                }else{
                    session.setUser(res.data);
                    Toast.makeText(Login.this, "Welcome " + res.data.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Registration.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class LoginResponse{
        public User data;
    }

    private void validate(Callback callback) {
        boolean hasError = false;
        if(txt_username.getText().length() == 0){
            tl_username.setError("Please enter valid username");
            hasError = true;
        }

        if(txt_password.getText().length() == 0){
            tl_password.setError("Password is required");
            hasError = true;
        }

        if(!hasError){
            tl_username.setError(null);
            tl_password.setError(null);
            callback.execute();
        }
    }
}
