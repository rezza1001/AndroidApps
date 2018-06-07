package g.rezza.moch.kiostixv3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextStandardC;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView           imvw_back_00;
    private EditextStandardC    edtx_email_00;
    private Button              bbtn_action_00;
    private TextView            txvw_forgot_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        edtx_email_00   = (EditextStandardC)findViewById(R.id.edtx_email_00);
        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);
        txvw_forgot_00  = (TextView)        findViewById(R.id.txvw_forgot_00);

        edtx_email_00.setTitle(getResources().getString(R.string.email));
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        txvw_forgot_00.setVisibility(View.GONE);

        initListener();
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtx_email_00.getValue().isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this,getResources().getString(R.string.email)+" harus diisi",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        ForgotPasswordActivity.this.finish();
    }
}
