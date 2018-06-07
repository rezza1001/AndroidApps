package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private  Thread splashTread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_splash);

        final CustomerDB user = new CustomerDB();
        user.getData(this);
        Log.d("SPLASH", user.token);
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
                    if (user.id.isEmpty()){
                        intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                    }



                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }
}
