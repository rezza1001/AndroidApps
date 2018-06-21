package g.rezza.moch.clientdashboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.MyApplication;
import g.rezza.moch.clientdashboard.view.LoginView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RZ MainActivity";

    private RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate() ");
        if (savedInstanceState != null){
            onSaveInstanceState(savedInstanceState);
        }
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        main_layout     = (RelativeLayout)      findViewById(R.id.main_layout);
        createLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private void createLogin(){
        main_layout.removeAllViews();
        LoginView loginView = new LoginView(MainActivity.this, null);
        main_layout.addView(loginView);
        loginView.setOnLoginListener(new LoginView.OnLoginListener() {
            @Override
            public void onLogin(String status) {
                mHandler.sendEmptyMessageDelayed(1,1);
            }
        });

        loginView.setOnRegisterClickListener(new LoginView.OnRegisterClickListener() {
            @Override
            public void onclik() {
            // Do Something
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("DATA", "Data Login");
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;
            }
        }
    };

}
