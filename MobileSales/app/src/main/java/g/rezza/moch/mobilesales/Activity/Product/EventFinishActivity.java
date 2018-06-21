package g.rezza.moch.mobilesales.Activity.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import g.rezza.moch.mobilesales.Activity.HomeActivity;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class EventFinishActivity extends ActivityDtl {

    private TextView txvw_subject_00;
    private TextView txvw_when_00;
    private FieldTransDtl ftdl_code_00;
    private FieldTransDtl ftdl_desc_00;
    private FieldTransDtl ftdl_status_00;
    private FieldTransDtl ftdl_amount_00;
    private FieldTransDtl ftdl_fee_00;
    private FieldTransDtl ftdl_payable_00;
    private Button        bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_finish);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.header_order_complete));

        txvw_subject_00     = (TextView)    findViewById(R.id.txvw_subject_00);
        txvw_when_00        = (TextView)    findViewById(R.id.txvw_when_00);
        ftdl_code_00        = (FieldTransDtl) findViewById(R.id.ftdl_code_00);
        ftdl_desc_00        = (FieldTransDtl) findViewById(R.id.ftdl_desc_00);
        ftdl_status_00      = (FieldTransDtl) findViewById(R.id.ftdl_status_00);
        ftdl_amount_00      = (FieldTransDtl) findViewById(R.id.ftdl_amount_00);
        ftdl_fee_00         = (FieldTransDtl) findViewById(R.id.ftdl_fee_00);
        ftdl_payable_00     = (FieldTransDtl) findViewById(R.id.ftdl_payable_00);
        bbtn_action_00      = (Button)        findViewById(R.id.bbtn_action_00);

        ftdl_code_00.setTitle(r.getString(R.string.trans_code));
        ftdl_desc_00.setTitle(r.getString(R.string.description));
        ftdl_status_00.setTitle(r.getString(R.string.trans_status));
        ftdl_amount_00.setTitle(r.getString(R.string.amount));
        ftdl_fee_00.setTitle(r.getString(R.string.fee));
        ftdl_payable_00.setTitle(r.getString(R.string.amount_payable));

        buildData();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFinishActivity.this, HomeActivity.class);
                startActivity(intent);
                EventFinishActivity.this.finish();
            }
        });
    }

    private void buildData(){
        txvw_subject_00.setText("ALINA BARAZ PEKING DUK THE PAPER KITES");
        txvw_when_00.setText("20 Januari 2018 09:00");
    }
}
