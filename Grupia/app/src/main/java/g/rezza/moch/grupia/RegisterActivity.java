package g.rezza.moch.grupia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bbtn_register_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bbtn_register_00 = (Button) findViewById(R.id.bbtn_register_00);
        bbtn_register_00.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == bbtn_register_00){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
