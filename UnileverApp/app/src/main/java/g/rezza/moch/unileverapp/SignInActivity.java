package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.GPSTracker;
import g.rezza.moch.unileverapp.lib.Parse;
import io.fabric.sdk.android.Fabric;

public class SignInActivity extends AppCompatActivity {

    private Button      bbtn_action_00;
    private EditText    edtx_username_00;
    private EditText    edtx_password_00;
    private GPSTracker  gpsTracker;
    private TextView    txvw_join_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_sign_in);
        initUI();
        initListener();

        gpsTracker = new GPSTracker(this);

    }

    private void initUI(){
        bbtn_action_00      = (Button)   findViewById(R.id.bbtn_action_00);
        edtx_username_00    = (EditText) findViewById(R.id.edtx_username_00);
        edtx_password_00    = (EditText) findViewById(R.id.edtx_password_00);
        txvw_join_00        = (TextView) findViewById(R.id.txvw_join_00);

//        edtx_password_00.setText("Zahra2018");
//        edtx_username_00.setText("zahra");
    }

    private void initListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        txvw_join_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
                SignInActivity.this.finish();
            }
        });
    }

    private void login(){
        String longitude = gpsTracker.getLongitude() +"";
        String latitude = gpsTracker.getLatitude() +"";
        String type     = "android OS";
        String imei     = "9999999xxxx";
        String phone    = "+6281219976562";
        String location = latitude+","+longitude;
        String username = edtx_username_00.getText().toString();
        String password = edtx_password_00.getText().toString();
        Log.d("TAG", longitude +" | "+ latitude);

        if (username.isEmpty()){
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.username) + " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.isEmpty()){
            Toast.makeText(SignInActivity.this, getResources().getString(R.string.password) + " is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("authenticate/login");
        JSONObject send = new JSONObject();
        try {
            send.put("request_type","1");
            JSONObject data = new JSONObject();

            JSONObject authenticate = new JSONObject();
            authenticate.put("username",username);
            authenticate.put("password", password);
            data.put("authenticate",authenticate);

            JSONObject device = new JSONObject();
            device.put("type",type);
            device.put("imei",imei);
            device.put("phone_no",phone);
            device.put("location",location);
            data.put("device",device);

            send.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(send);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject data = obj.getJSONObject("data");
                        OutletDB outletDB = new OutletDB();
                        outletDB.user_id        = data.getString("user_id");
                        outletDB.outlet_id      = data.getString("outlet_id");
                        outletDB.username       = data.getString("username");
                        outletDB.outlet_name    = data.getString("outlet_name");
                        outletDB.mine           = 1;
                        outletDB.insert(SignInActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    SignInActivity.this.finish();
                }
                else {
                    Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                Log.d("TAGRZ", obj.toString());
            }
        });
    }
}