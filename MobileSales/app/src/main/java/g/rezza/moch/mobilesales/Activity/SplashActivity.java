package g.rezza.moch.mobilesales.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.IdentityTypeDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.R;

public class SplashActivity extends AppCompatActivity {

    private  Thread splashTread;
    private  ImageView imvw_logo_00;
    private TextView txvw_loading_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imvw_logo_00    = (ImageView) findViewById(R.id.imvw_logo_00);
        txvw_loading_00 = (TextView) findViewById(R.id.txvw_loading_00);

        final UserDB user  = new UserDB();
        user.getMine(this);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    if (user.id == 0){
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        requestPemission();
    }

    private void requestPemission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            requestMasterData();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG","requestCode : "+requestCode +" | GENRETED");
                requestMasterData();

        } else {
            Toast.makeText(SplashActivity.this, getResources().getString(R.string.please_give_permision),
                    Toast.LENGTH_LONG).show();
            Log.d("TAG","requestCode : "+requestCode +" | DENIED");
            mHandler.sendEmptyMessageDelayed(1,3000);

        }

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            SplashActivity.this.finish();
            return false;
        }
    });

    private void requestMasterData(){
        loadingText();
        PostManager pos = new PostManager(this);
        pos.setApiUrl("identity-type");
//        pos.setData(new ArrayList<KeyValueHolder>());
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                splashTread.start();
                if (code == ErrorCode.OK){
                    IdentityTypeDB idDB = new IdentityTypeDB();
                    idDB.clearData(SplashActivity.this);
                    try {
                        JSONArray arrType = obj.getJSONArray("IDENTITY");
                        for (int i=0; i<arrType.length(); i++){
                            idDB.id       = arrType.getJSONObject(i).getInt("id");
                            idDB.category = arrType.getJSONObject(i).getInt("category");
                            idDB.name     = arrType.getJSONObject(i).getString("name");
                            idDB.insert(SplashActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadingText(){
        final String[] message = {txvw_loading_00.getText().toString()};
        final String[] ori_message = {txvw_loading_00.getText().toString()};
        Thread t = new Thread(){
            @Override
            public void run() {
                int counter = 0;
                while (true){
                    try {
                        sleep(1000);
                        counter ++;
                        if (counter <= 3){
                            message[0] = message[0] + ".";
                        }
                        else {
                            counter = 0;
                            message[0] = ori_message[0];
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
    }

}
