package g.rezza.moch.chatid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bbtn_start_00;
    private Button bbtn_sign_in_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bbtn_start_00   = (Button)    findViewById(R.id.bbtn_start_00);
        bbtn_sign_in_00 = (Button)    findViewById(R.id.bbtn_sign_in_00);
        bbtn_start_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FindRandomActivity.class));
                MainActivity.this.finish();
            }
        });

        bbtn_sign_in_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                MainActivity.this.finish();
            }
        });
    }
}
