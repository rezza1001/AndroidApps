package g.rezza.moch.mobilesales.Activity.Billing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.PaymentAdapter;
import g.rezza.moch.mobilesales.component.EditextCurrency;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.PaymentMethod;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class PayBillActivity extends ActivityDtl implements PaymentMethod.OnSelectedItemListener{

    private EditextCurrency edtx_nominal_00;
    private EditextStandardC edtx_account_00;
    private EditextStandardC edtx_invoice_00;
    private PaymentMethod pymt_banktf_00;
    private Button        bbtn_action_00;


    private PaymentAdapter.PaymentMethodeHolder  payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.billing_payment));
        hideRightMenu(true);

        edtx_nominal_00.setTitle(r.getString(R.string.nominal));
        edtx_nominal_00 =(EditextCurrency) findViewById(R.id.edtx_nominal_00);
        edtx_account_00 =(EditextStandardC) findViewById(R.id.edtx_account_00);
        edtx_invoice_00 =(EditextStandardC) findViewById(R.id.edtx_invoice_00);
        pymt_banktf_00  = (PaymentMethod)   findViewById(R.id.pymt_banktf_00);
        bbtn_action_00  = (Button)      findViewById(R.id.bbtn_action_00);
        edtx_account_00.setTitle("Account Number");
        edtx_account_00.setReadOnly(true);
        edtx_account_00.setValue(userDB.account);

        edtx_invoice_00.setTitle(r.getString(R.string.billing_id));

        pymt_banktf_00.init("Bank Transfer");
        pymt_banktf_00.addData("1","Bank Mandiri", getResources().getDrawable(R.drawable.ic_payment_mandiri));
        pymt_banktf_00.addData("2","Bank BCA", getResources().getDrawable(R.drawable.ic_payment_bca));
        pymt_banktf_00.create();
        pymt_banktf_00.setOnSelectedItemListener(this);



        edtx_nominal_00.setValue(getIntent().getStringExtra("NOMINAL"));
        edtx_nominal_00.setReadOnly(true);

        edtx_invoice_00.setValue(getIntent().getStringExtra("CODE"));
        edtx_invoice_00.setReadOnly(true);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nominal  = edtx_nominal_00.getValue();
                if (nominal.isEmpty() || nominal.equals("0")){
                    edtx_nominal_00.showNotif(r.getString(R.string.field_required));
                    return;
                }


                PostManager pos = new PostManager(PayBillActivity.this);
                pos.setApiUrl("billing-payment");
                ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                kvs.add(new KeyValueHolder("code", edtx_invoice_00.getValue()));
                kvs.add(new KeyValueHolder("payment_method", payment.category));
                kvs.add(new KeyValueHolder("bank", payment.name));
                pos.setData(kvs);
                pos.execute("POST");
                pos.setOnReceiveListener(new PostManager.onReceiveListener() {
                    @Override
                    public void onReceive(JSONObject obj, int code) {
                        if (code == ErrorCode.OK){
                            Toast.makeText(PayBillActivity.this,
                                    r.getString(R.string.transaction_success), Toast.LENGTH_SHORT).show();
                            PayBillActivity.this.finish();
                        }
                        else {
                            Toast.makeText(PayBillActivity.this,
                                    r.getString(R.string.transaction_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

    @Override
    public void OnItemSelected(int index, PaymentAdapter.PaymentMethodeHolder mHolder) {
        pymt_banktf_00.notifyChange(mHolder);

        payment = mHolder;
    }
}
