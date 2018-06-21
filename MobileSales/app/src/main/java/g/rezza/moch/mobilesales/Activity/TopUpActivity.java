package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Activity.TopUp.ConfirmPaymentActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.PaymentAdapter;
import g.rezza.moch.mobilesales.component.EditextCurrency;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.PaymentMethod;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;
import g.rezza.moch.mobilesales.view.Dialog.TopUpAvalibale;

public class TopUpActivity extends ActivityDtl implements PaymentMethod.OnSelectedItemListener{

    private EditextCurrency edtx_nominal_00;
    private EditextStandardC edtx_account_00;
    private EditextStandardC edtx_invoice_00;
    private PaymentMethod pymt_banktf_00;
    private Button        bbtn_action_00;

    private String        payment       = "";
    private String        payment_name  = "";
    TopUpAvalibale topup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader("TOP UP");
        hideRightMenu(true);

        edtx_nominal_00 =(EditextCurrency) findViewById(R.id.edtx_nominal_00);
        edtx_invoice_00 =(EditextStandardC) findViewById(R.id.edtx_invoice_00);
        edtx_account_00 =(EditextStandardC) findViewById(R.id.edtx_account_00);
        pymt_banktf_00  = (PaymentMethod)   findViewById(R.id.pymt_banktf_00);
        bbtn_action_00  = (Button)      findViewById(R.id.bbtn_action_00);
        edtx_account_00.setTitle("Account Number");
        edtx_account_00.setReadOnly(true);
        edtx_account_00.setValue(userDB.account);
        edtx_nominal_00.setTitle(r.getString(R.string.nominal));


        edtx_invoice_00.setVisibility(View.GONE);

        pymt_banktf_00.init("Bank Transfer");
        pymt_banktf_00.addData("405","BCA KlikPay", getResources().getDrawable(R.mipmap.bca_click_pay));
        pymt_banktf_00.addData("406","Mandiri Clickpay", getResources().getDrawable(R.mipmap.mandiri_clickpay));
        pymt_banktf_00.addData("801","BNI Virtual Account", getResources().getDrawable(R.mipmap.bni));
        pymt_banktf_00.addData("802","Mandiri Virtual Account", getResources().getDrawable(R.mipmap.logo_bank_mandiri_coreldraw));
        pymt_banktf_00.create();
        pymt_banktf_00.setOnSelectedItemListener(this);


        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nominal  = edtx_nominal_00.getValue();
                if (nominal.toString().isEmpty() || nominal.equals("0")){
                    edtx_nominal_00.showNotif(r.getString(R.string.field_required));
                    return;
                }
                Long nominal_l = Long.parseLong(nominal);
                if (nominal_l < 100000){
                    edtx_nominal_00.showNotif(r.getString(R.string.nominal_minimum) +" "+ Parse.toCurrnecy(100000));
                    return;
                }

                Intent intent = new Intent(TopUpActivity.this, ConfirmPaymentActivity.class);
                intent.putExtra("NOMINAL", nominal);
                intent.putExtra("PAYMENT", payment);
                intent.putExtra("PAYMENT_NAME", payment_name);
                intent.putExtra("API", "topup");
                startActivity(intent);
                finish();
            }
        });

        request();

    }

    @Override
    public void OnItemSelected(int index, PaymentAdapter.PaymentMethodeHolder mHolder) {
        pymt_banktf_00.notifyChange(mHolder);
        payment         = mHolder.id;
        payment_name    = mHolder.name;
    }


    private void request(){
        PostManager post = new PostManager(this);
        post.setApiUrl("topup-check");
        post.setData(new ArrayList<KeyValueHolder>());
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject resps = obj.getJSONObject("ResponPayment");
                        JSONObject trans = obj.getJSONObject("Transactions");

                        topup = new TopUpAvalibale(TopUpActivity.this);
                        topup.showData(trans, resps);
                        topup.setActivity(TopUpActivity.this);
                        topup.setOnCloseListener(new TopUpAvalibale.OnCloseListener() {
                            @Override
                            public void onClose() {
                                TopUpActivity.this.finish();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


}
