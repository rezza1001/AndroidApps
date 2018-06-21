package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.TopUpActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Parse;

/**
 * Created by rezza on 28/12/17.
 */

public class BalanceView extends RelativeLayout {
    public BalanceView(Context context) {
        super(context);
    }

    private RelativeLayout  rvly_topup_00;
    private ProgressBar     prgb_00;
    private TextView        txvw_balance_01;
    private ImageView       imvw_sync_00;

    public BalanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_balance, this, true);

        rvly_topup_00 = (RelativeLayout) findViewById(R.id.rvly_topup_00);
        rvly_topup_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TopUpActivity.class);
                getContext().startActivity(i);
            }
        });

        prgb_00 = (ProgressBar) findViewById(R.id.prgb_00);
        prgb_00.bringToFront();
        prgb_00.setVisibility(View.GONE);

        txvw_balance_01 = (TextView)  findViewById(R.id.txvw_balance_01);
        imvw_sync_00    = (ImageView) findViewById(R.id.imvw_sync_00);

        imvw_sync_00.bringToFront();
        imvw_sync_00.setVisibility(View.GONE);

        imvw_sync_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                BalanceDB balancedb = new BalanceDB();
                balancedb.Syncronize(getContext());
                requestBalance();
            }
        });

        requestBalance();
    }

    public void notifRequest(){
        BalanceDB balancedb = new BalanceDB();
        balancedb.Syncronize(getContext());
        requestBalance();
    }

    public String getText(){
        return  txvw_balance_01.getText().toString();
    }

    public void requestBalance(){
        final BalanceDB balancedb = new BalanceDB();
        balancedb.getData(getContext());
        if (!balancedb.isSync()){
            String balance = Parse.toCurrnecy(balancedb.balance);
            txvw_balance_01.setText("Rp. "+ balance);
            imvw_sync_00.setVisibility(View.VISIBLE);
            return;
        }

        imvw_sync_00.setVisibility(View.GONE);
        prgb_00.setVisibility(View.VISIBLE);
        PostManager pos = new PostManager(getContext());
        pos.setApiUrl("check-balance");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        pos.setData(mHolders);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                Log.d("Check Balance","Check : "+ obj.toString());
                prgb_00.setVisibility(View.GONE);
                imvw_sync_00.setVisibility(View.VISIBLE);
                if (code == ErrorCode.OK){
                    try {
                        JSONObject jData = obj.getJSONArray("DATA").getJSONObject(0);
                        String balance = Parse.toCurrnecy(jData.getLong("BALANCE"));
                        txvw_balance_01.setText("Rp. "+ balance);
                        balancedb.balance = jData.getLong("BALANCE");
                        balancedb.insert(getContext());
                        if (mAfterRequestListener != null){
                            mAfterRequestListener.after();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        txvw_balance_01.setText("Rp. "+ 0 );
                    }

                }

            }
        });
    }

    private AfterRequestListener mAfterRequestListener;
    public void setAfterRequestListener(AfterRequestListener mAfterRequestListener){
        this.mAfterRequestListener = mAfterRequestListener;
    }
    public interface AfterRequestListener{
        public void after();
    }


}
