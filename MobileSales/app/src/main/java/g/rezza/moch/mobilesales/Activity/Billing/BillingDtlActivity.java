package g.rezza.moch.mobilesales.Activity.Billing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransHdr;
import g.rezza.moch.mobilesales.holder.BillingListHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class BillingDtlActivity extends ActivityDtl {

    private FieldTransHdr fthd_code_00;
    private FieldTransHdr fthd_created_00;
    private FieldTransHdr fthd_status_00;
    private FieldTransHdr fthd_totalbill_00;
    private FieldTransHdr fthd_note_00;
    private Button        bbtn_action_00;


    BillingListHolder billing ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_dtl);

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.my_billing));
        hideRightMenu(true);
        billing = new BillingListHolder();
        billing.code    = getIntent().getStringExtra("CODE");
        billing.date    = getIntent().getStringExtra("DATE");
        billing.status      = getIntent().getIntExtra("STATUS",0);
        billing.status_desc   = getIntent().getStringExtra("STATUS_DESC");
        billing.note   = getIntent().getStringExtra("DESCRIPTION");
        billing.amount   = getIntent().getStringExtra("AMOUNT");


        fthd_code_00        = (FieldTransHdr) findViewById(R.id.fthd_code_00);
        fthd_created_00     = (FieldTransHdr) findViewById(R.id.fthd_created_00);
        fthd_status_00      = (FieldTransHdr) findViewById(R.id.fthd_status_00);
        fthd_totalbill_00   = (FieldTransHdr) findViewById(R.id.fthd_totalbill_00);
        fthd_note_00        = (FieldTransHdr) findViewById(R.id.fthd_note_00);
        bbtn_action_00      = (Button)        findViewById(R.id.bbtn_action_00);


        fthd_code_00.setTitle(r.getString(R.string.code));
        fthd_code_00.setValue(billing.code);


        fthd_created_00.setTitle(r.getString(R.string.created_at));
        fthd_created_00.setValue(billing.date.substring(0,16));

        fthd_status_00.setTitle(r.getString(R.string.status));
        fthd_status_00.setValue(billing.status_desc);

        fthd_totalbill_00.setTitle(r.getString(R.string.total_bill));
        fthd_totalbill_00.setValue("Rp. "+ Parse.toCurrnecy(billing.amount));

        fthd_note_00.setTitle(r.getString(R.string.note));
        fthd_note_00.setValue(billing.note);

        if (billing.status == 9){
            bbtn_action_00.setVisibility(View.GONE);
        }

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalanceDB balanceDB = new BalanceDB();
                balanceDB.Syncronize(BillingDtlActivity.this);
                Intent intent = new Intent(BillingDtlActivity.this, PayBillActivity.class);
                intent.putExtra("NOMINAL",billing.amount);
                intent.putExtra("CODE",billing.code);
                startActivity(intent);
                BillingDtlActivity.this.finish();
            }
        });
    }

}
