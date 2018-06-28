package g.rezza.moch.chatid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity {

    private Button bbtn_sign_in_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        bbtn_sign_in_00 = (Button) findViewById(R.id.bbtn_sign_in_00);

        initListener();
    }

    private void initListener(){
        bbtn_sign_in_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, VerifyActivity.class));
                SignInActivity.this.finish();
            }
        });
    }
}
