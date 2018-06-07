package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPwdActivity extends AppCompatActivity {

    private ImageView       imvw_back_00;
    private RelativeLayout  rvly_cart_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_title_00;
    private EditText        edtx_email_00;
    private EditText        edtx_phone_00;
    private Button          bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        initLayout();
        initListener();
    }

    private void initLayout() {
        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_cart_00    = (RelativeLayout)  findViewById(R.id.rvly_cart_00);
        rvly_notif_00   = (RelativeLayout)  findViewById(R.id.rvly_notif_00);
        txvw_title_00   = (TextView)        findViewById(R.id.txvw_title_00);
        edtx_email_00   = (EditText)        findViewById(R.id.edtx_email_00);
        edtx_phone_00   = (EditText)        findViewById(R.id.edtx_phone_00);
        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);


        rvly_cart_00.setVisibility(View.GONE);
        rvly_notif_00.setVisibility(View.GONE);
        txvw_title_00.setVisibility(View.GONE);
    }

    private void initListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void send(){
        String email = edtx_email_00.getText().toString();
        String phone = edtx_phone_00.getText().toString();

        if (email.isEmpty()){
            Toast.makeText(this, "Silahkan isi "+ getResources().getString(R.string.email), Toast.LENGTH_LONG).show();
            return;
        }
        else if (phone.isEmpty()){
            Toast.makeText(this, "Silahkan isi "+ getResources().getString(R.string.phone), Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}