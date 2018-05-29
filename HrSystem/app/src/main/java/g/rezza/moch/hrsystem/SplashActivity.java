package g.rezza.moch.hrsystem;

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
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.hrsystem.connection.PostManager;
import g.rezza.moch.hrsystem.database.SynDB;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import g.rezza.moch.hrsystem.lib.ErrorCode;

public class SplashActivity extends AppCompatActivity {

    private  Thread splashTread;
    private UserDB user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_splash);

        user = new UserDB();
        user.getMine(this);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 4000) {
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
        String[] PERMISSIONS = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            splashTread.start();
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
            splashTread.start();

        } else {
            Toast.makeText(SplashActivity.this, getResources().getString(R.string.please_give_permision),
                    Toast.LENGTH_LONG).show();
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

    private void SyncrhonizeData(){
        SynDB syn = new SynDB();
        PostManager post = new PostManager(this);
        post.setApiUrl("synchronize");
        post.setData(new ArrayList<KeyValueHolder>());
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                try {
                    if (ErrorCode.OK == code){

                    }
                }catch (Exception e){

                }


            }
        });
    }
}
