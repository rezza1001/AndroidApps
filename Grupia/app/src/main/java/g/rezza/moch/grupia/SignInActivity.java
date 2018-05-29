package g.rezza.moch.grupia;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import g.rezza.moch.grupia.component.ChapterDialog;
import g.rezza.moch.grupia.lib.AudioPlayer;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bbtn_signin_00;
    private Button bbtn_register_00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        bbtn_signin_00   = (Button) findViewById(R.id.bbtn_signin_00);
        bbtn_register_00 = (Button) findViewById(R.id.bbtn_register_00);

        bbtn_signin_00.setOnClickListener(this);
        bbtn_register_00.setOnClickListener(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAGRZ", "Refreshed token: " + refreshedToken);


    }

    @Override
    public void onClick( View v) {
        if (v == bbtn_register_00){
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(intent);
            this.finish();
        }
        else if (v == bbtn_signin_00){
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}
