package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.datastatic.App;
import io.fabric.sdk.android.Fabric;

public class BlockUserActivity extends AppCompatActivity {

    private Button      bbtn_register_00;
    private Button      bbtn_login_00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_block_user);

        bbtn_login_00       = (Button) findViewById(R.id.bbtn_login_00);
        bbtn_register_00    = (Button) findViewById(R.id.bbtn_register_00);

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlockUserActivity.this, SignInActivity.class);
                intent.putExtra("FROM","3");
                startActivityForResult(intent, App.REQUEST_AUTH);
                BlockUserActivity.this.finish();
            }
        });

        bbtn_register_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlockUserActivity.this, RegisterActivity.class);
                intent.putExtra("FROM","3");
                startActivityForResult(intent,App.REQUEST_AUTH);
                BlockUserActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        super.onBackPressed();
    }
}
