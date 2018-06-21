package g.rezza.moch.mobilesales.Activity.TopUp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Activity.BrowseActivity;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.HomeActivity;
import g.rezza.moch.mobilesales.Activity.TopUpActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.PaymentAdapter;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class ConfirmPaymentActivity extends ActivityDtl {

    private EditextStandardC edtx_account_00;
    private EditextStandardC edtx_balance_00;
    private EditextStandardC edtx_total_00;
    private TextView         txvw_info_00;
    private TextView         txvw_info_01;
    private TextView         txvw_rek_00;
    private PaymentAdapter   pad_method_00;
    private Button           bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_payment);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.top_up_payment));
        hideRightMenu(true);

        edtx_account_00 = (EditextStandardC) findViewById(R.id.edtx_account_00);
        edtx_balance_00 = (EditextStandardC) findViewById(R.id.edtx_balance_00);
        edtx_total_00   = (EditextStandardC) findViewById(R.id.edtx_total_00);
        txvw_info_00    = (TextView)         findViewById(R.id.txvw_info_00);
        txvw_info_01    = (TextView)         findViewById(R.id.txvw_info_01);
        txvw_rek_00     = (TextView)         findViewById(R.id.txvw_rek_00);
        pad_method_00   = (PaymentAdapter)   findViewById(R.id.pad_method_00);

        bbtn_action_00 = (Button)            findViewById(R.id.bbtn_action_00);

        create();
    }

    public void create(){
        long nominal = 0;
        try {
            String nominal1 = getIntent().getStringExtra("NOMINAL");
            nominal = Long.parseLong(nominal1);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        edtx_account_00.setTitle(r.getString(R.string.account_number));
        edtx_account_00.setReadOnly(true);
        edtx_account_00.setValue(userDB.account);

        edtx_balance_00.setTitle(r.getString(R.string.nominal));
        edtx_balance_00.setReadOnly(true);
        edtx_balance_00.setValue("Rp. "+ Parse.toCurrnecy(nominal));

        edtx_total_00.setTitle(r.getString(R.string.total_payment));
        edtx_total_00.setReadOnly(true);
        edtx_total_00.setValue("Rp. "+ Parse.toCurrnecy(nominal));
        edtx_total_00.setTextStyle(R.style.TextView_Black_Montserrat_Bold);

        txvw_info_01.setText(TextUtils.concat("Silahkan transfer ke rekening berikut atas nama : \n",
                Parse.BoldText("PT. Pulau Kencana")));
        txvw_rek_00.setText("1022086257");

        txvw_info_00.setText(TextUtils.concat("Silahkan melakukan pembayaran sebesar ",
                Parse.BoldText("Rp. "+ Parse.toCurrnecy(nominal)+ " "),
                "untuk proses pengisian saldo anda"));

        final long finalNominal         = nominal;
        final String url_api            = getIntent().getStringExtra("API");
        final String payment_channel    = getIntent().getStringExtra("PAYMENT");
        final String payment_name       = getIntent().getStringExtra("PAYMENT_NAME");

        PaymentAdapter.PaymentMethodeHolder holder = new PaymentAdapter.PaymentMethodeHolder();
        holder.check = false;
        holder.id    = payment_channel;
        holder.name  = getIntent().getStringExtra("PAYMENT_NAME");
        if (holder.id.equals("405")){
            holder.icon  = getResources().getDrawable(R.mipmap.bca_click_pay);
        }
        else if (holder.id.equals("406")){
            holder.icon  = getResources().getDrawable(R.mipmap.mandiri_clickpay);
        }
        else if (holder.id.equals("801")){
            holder.icon  = getResources().getDrawable(R.mipmap.bni);
        }
        else if (holder.id.equals("802")){
            holder.icon  = getResources().getDrawable(R.mipmap.logo_bank_mandiri_coreldraw);
        }

        pad_method_00.create(holder);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostManager pos = new PostManager(ConfirmPaymentActivity.this);
                pos.setApiUrl(url_api);
                ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                kvs.add(new KeyValueHolder("nominal", finalNominal +""));
                kvs.add(new KeyValueHolder("method", pad_method_00.mHolder.name));
                kvs.add(new KeyValueHolder("payment_channel", payment_channel));
                kvs.add(new KeyValueHolder("pg_name", payment_name));
                pos.setData(kvs);
                pos.execute("POST");
                pos.setOnReceiveListener(new PostManager.onReceiveListener() {
                    @Override
                    public void onReceive(JSONObject obj, int code) {

                        if (code == ErrorCode.OK){
                            BalanceDB balanceDB = new BalanceDB();
                            balanceDB.Syncronize(ConfirmPaymentActivity.this);
                            Intent intent = new Intent(ConfirmPaymentActivity.this, BrowseActivity.class);
                            try {
                                JSONObject data = obj.getJSONObject("DATA");
                                intent.putExtra("URL",data.getString("redirect_url"));
                                startActivity(intent);
                                ConfirmPaymentActivity.this.finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Toast.makeText(ConfirmPaymentActivity.this,  r.getString(R.string.failed_to_send_request), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    public void showNotif(){
        new AlertDialog.Builder(this)
                .setMessage(r.getString(R.string.your_request_balance_sent))
                .setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(ConfirmPaymentActivity.this, HomeActivity.class);
                        startActivity(intent);
                        ConfirmPaymentActivity.this.finish();
                    }}).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TopUpActivity.class);
        startActivity(intent);
        finish();
    }
}
