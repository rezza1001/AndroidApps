package g.rezza.moch.mobilesales.Activity.OrderCheck;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.PaymentAdapter;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.component.PaymentMethod;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;

public class OrderCheckActivity extends ActivityWthHdr implements PaymentMethod.OnSelectedItemListener{

    private static final String TAG = "OrderCheckActivity";
    private static final int REQUEST_BARCODE = 1;

    private FieldTransDtl    ftdl_type_00;
    private FieldTransDtl    ftdl_code_00;
    private FieldTransDtl    ftdl_time_00;
    private FieldTransDtl    ftdl_at_00;
    private FieldTransDtl    ftdl_qty_00;
    private FieldTransDtl    ftdl_amount_00;
    private FieldTransDtl    ftdl_fee_00;
    private FieldTransDtl    ftdl_total_00;
    private LinearLayout     lnly_payinfo_00;
    private LinearLayout     lnly_payment_00;
    private TextView         txvw_payinfo_00;
    private TextView         txvw_payment_00;
    private FieldTransDtl    ftdl_trans_status_00;
    private FieldTransDtl    ftdl_trans_id_00;
    private FieldTransDtl    ftdl_trans_method_00;
    private Button           bbtn_action_00;
    private PaymentMethod    pymt_banktf_00;
    private PaymentMethod    pymt_credit_00;
    private PaymentMethod    pymt_cash_00;

    private boolean          canPay = false;

    private PaymentAdapter.PaymentMethodeHolder payment_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check);

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.order_detail));
        hideMenuRight(true);

        ftdl_trans_status_00    = (FieldTransDtl)    findViewById(R.id.ftdl_trans_status_00);
        ftdl_trans_id_00        = (FieldTransDtl)    findViewById(R.id.ftdl_trans_id_00);
        ftdl_trans_method_00    = (FieldTransDtl)    findViewById(R.id.ftdl_trans_method_00);
        ftdl_type_00       = (FieldTransDtl)    findViewById(R.id.ftdl_type_00);
        ftdl_code_00       = (FieldTransDtl)    findViewById(R.id.ftdl_code_00);
        ftdl_time_00       = (FieldTransDtl)    findViewById(R.id.ftdl_time_00);
        ftdl_at_00         = (FieldTransDtl)    findViewById(R.id.ftdl_at_00);
        ftdl_qty_00        = (FieldTransDtl)    findViewById(R.id.ftdl_qty_00);
        ftdl_amount_00     = (FieldTransDtl)    findViewById(R.id.ftdl_amount_00);
        ftdl_fee_00        = (FieldTransDtl)    findViewById(R.id.ftdl_fee_00);
        ftdl_total_00      = (FieldTransDtl)    findViewById(R.id.ftdl_total_00);
        lnly_payinfo_00    = (LinearLayout)     findViewById(R.id.lnly_payinfo_00);
        lnly_payment_00    = (LinearLayout)     findViewById(R.id.lnly_payment_00);
        txvw_payinfo_00    = (TextView)         findViewById(R.id.txvw_payinfo_00);
        txvw_payment_00    = (TextView)         findViewById(R.id.txvw_payment_00);
        bbtn_action_00     = (Button)           findViewById(R.id.bbtn_action_00);
        pymt_banktf_00     = (PaymentMethod)   findViewById(R.id.pymt_banktf_00);
        pymt_credit_00     = (PaymentMethod)   findViewById(R.id.pymt_credit_00);
        pymt_cash_00       = (PaymentMethod)   findViewById(R.id.pymt_cash_00);

        lnly_payinfo_00.setVisibility(View.GONE);
        txvw_payinfo_00.setVisibility(View.GONE);

        ftdl_code_00.setTitle(r.getString(R.string.code));
        ftdl_type_00.setTitle(r.getString(R.string.status));
        ftdl_time_00.setTitle(r.getString(R.string.booking_time));
        ftdl_at_00.setTitle(r.getString(R.string.store));
        ftdl_qty_00.setTitle(r.getString(R.string.quantity));
        ftdl_amount_00.setTitle(r.getString(R.string.amount));
        ftdl_fee_00.setTitle(r.getString(R.string.fee));
        ftdl_fee_00.setVisibility(View.GONE);
        ftdl_total_00.setTitle(r.getString(R.string.amount_payable));

        ftdl_trans_status_00.setTitle(r.getString(R.string.trans_status));
        ftdl_trans_method_00.setTitle(r.getString(R.string.method));
        ftdl_trans_id_00.setTitle(r.getString(R.string.trans_code));

        pymt_banktf_00.init();
        pymt_banktf_00.addData("Transfer BCA","Bank BCA", getResources().getDrawable(R.drawable.ic_payment_bca));
        pymt_banktf_00.create();
        pymt_banktf_00.setOnSelectedItemListener(this);

        pymt_credit_00.init();
        pymt_credit_00.addData("Credit Card/Debit","Credit Card/Debit", getResources().getDrawable(R.drawable.ic_payment_visa));
        pymt_credit_00.create();
        pymt_credit_00.setOnSelectedItemListener(this);

        pymt_cash_00.init();
        pymt_cash_00.addData("Cash","Cash Payment", getResources().getDrawable(R.drawable.ic_menu_money));
        pymt_cash_00.create();
        pymt_cash_00.setOnSelectedItemListener(this);

        loadData();
    }



    protected void registerListener(){

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canPay){
                    pay();
                }

            }
        });
    }

    private void loadData(){
        canPay = false;
        final String order_no = getIntent().getStringExtra("CODE");
        PostManager pos = new PostManager(this);
        pos.setApiUrl("detail-order");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("order_no",order_no));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject date = obj.getJSONObject("ORDER_DATE");
                        ftdl_code_00.setValue(order_no);
                        ftdl_type_00.setValue(obj.getString("STATUS_ORDER"));
                        ftdl_time_00.setValue(date.getString("date").substring(0,19));
                        ftdl_at_00.setValue(obj.getString("CREATED_BY"));
                        ftdl_qty_00.setValue(obj.getString("TOTAL_QTY"));
                        ftdl_amount_00.setValue(obj.getString("TOTAL_AMOUNT"));
                        ftdl_total_00.setValue(obj.getString("TOTAL_AMOUNT"));
                        bbtn_action_00.setVisibility(View.VISIBLE);
                        txvw_payment_00.setVisibility(View.VISIBLE);
                        lnly_payment_00.setVisibility(View.VISIBLE);
                        lnly_payinfo_00.setVisibility(View.GONE);
                        txvw_payinfo_00.setVisibility(View.GONE);
                        canPay = true;

                        if (obj.getString("STATUS_ID").equals("4")){
                            bbtn_action_00.setVisibility(View.GONE);
                            txvw_payment_00.setVisibility(View.GONE);
                            lnly_payment_00.setVisibility(View.GONE);

                            lnly_payinfo_00.setVisibility(View.VISIBLE);
                            txvw_payinfo_00.setVisibility(View.VISIBLE);
                            canPay = false;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    ftdl_code_00.setValue("");
                    ftdl_type_00.setValue("");
                    ftdl_time_00.setValue("");
                    ftdl_at_00.setValue("");
                    ftdl_qty_00.setValue("");
                    ftdl_amount_00.setValue("");
                    ftdl_total_00.setValue("");
                    OrderCheckActivity.this.finish();
                    setResult(0);
                }
            }
        });
    }

    private void pay(){
        if (payment_method == null){
            Toast.makeText(this,"Please choose payment method!",Toast.LENGTH_SHORT).show();
            return;
        }
        lnly_payinfo_00.setVisibility(View.GONE);
        txvw_payinfo_00.setVisibility(View.GONE);

        PostManager pos = new PostManager(this);
        pos.setApiUrl("update-order");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("order_no",getIntent().getStringExtra("CODE")));
        kvs.add(new KeyValueHolder("payment_method",payment_method.id));
        kvs.add(new KeyValueHolder("bank",payment_method.id));
        kvs.add(new KeyValueHolder("amount_payable",ftdl_total_00.getValue()));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    canPay = false;
                    BalanceDB balanceDB = new BalanceDB();
                    balanceDB.Syncronize(OrderCheckActivity.this);

                    lnly_payinfo_00.setVisibility(View.VISIBLE);
                    txvw_payinfo_00.setVisibility(View.VISIBLE);

                    try {
                        String message = obj.getString("ERROR MESSAGE");
                        Toast.makeText(OrderCheckActivity.this,message,Toast.LENGTH_SHORT ).show();
                        JSONObject transaction = obj.getJSONObject("TRANSACTION");
                        ftdl_trans_status_00.setValue(transaction.getString("status_description"));
                        ftdl_trans_method_00.setValue(transaction.getString("payment_method"));
                        ftdl_trans_id_00.setValue(transaction.getString("transaction_code"));
                        bbtn_action_00.setVisibility(View.GONE);
                        txvw_payment_00.setVisibility(View.GONE);
                        lnly_payment_00.setVisibility(View.GONE);

                        OrderCheckActivity.this.finish();
                        setResult(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        String message = obj.getString("ERROR MESSAGE");
                        Toast.makeText(OrderCheckActivity.this,message,Toast.LENGTH_SHORT ).show();
                    }catch (Exception e){
                        Toast.makeText(OrderCheckActivity.this,r.getString(R.string.error_transaction),Toast.LENGTH_SHORT ).show();
                    }

                }
            }
        });
    }

    @Override
    public void OnItemSelected(int index, PaymentAdapter.PaymentMethodeHolder mHolder) {
        pymt_cash_00.notifyChange(mHolder);
        pymt_credit_00.notifyChange(mHolder);
        pymt_banktf_00.notifyChange(mHolder);
        payment_method = mHolder;
    }
}
