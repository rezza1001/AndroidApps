package g.rezza.moch.kiospos.Activitty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import g.rezza.moch.kiospos.R;

public class DetailActivity extends AppCompatActivity {

    private Button bbtn_buy_40;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bbtn_buy_40     =(Button)       findViewById(R.id.bbtn_buy_40);

        bbtn_buy_40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this, MainPosActivity.class);
                startActivity(i);
            }
        });
    }
}
