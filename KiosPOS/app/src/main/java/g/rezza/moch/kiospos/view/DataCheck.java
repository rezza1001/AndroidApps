package g.rezza.moch.kiospos.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiospos.lib.LongRunningGetIO;
import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.Activitty.MainActivity;
import g.rezza.moch.kiospos.Activitty.PairingActivity;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.MyEditextMail;
import g.rezza.moch.kiospos.component.PopupConfig;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.AttendenzHolder;
import g.rezza.moch.kiospos.holder.BluethoothHolder;
import g.rezza.moch.kiospos.holder.KeyValueHolder;

/**
 * Created by Rezza on 10/12/17.
 */

public class DataCheck extends MasterView {

    private static final String TAG = "DataCheck";

    private RelativeLayout  rvly_parent_00;
    private RelativeLayout  rvly_printer_20;
    private MyEditextMail   myedtx_mail_30;
    private Button          bbtn_new_30;
    private Button          bbtn_cek_31;
    private TextView        txvw_connectdevice_41;

    public DataCheck(Context context) {
        super(context);
    }

    public DataCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_data_check, this, true);
    }

    public void create(JSONObject jo){
        disableLogo();
        mData           = new JSONObject();
        myedtx_mail_30  = (MyEditextMail) findViewById(R.id.myedtx_mail_30);
        bbtn_new_30     = (Button) findViewById(R.id.bbtn_new_30);
        bbtn_cek_31     = (Button) findViewById(R.id.bbtn_cek_31);
        rvly_parent_00  = (RelativeLayout) findViewById(R.id.rvly_parent_00);
        rvly_printer_20 = (RelativeLayout) findViewById(R.id.rvly_printer_20);
        txvw_connectdevice_41   = (TextView)    findViewById(R.id.txvw_connectdevice_41);

        final boolean[] emailvalid = {false};
        myedtx_mail_30.setValue("");
        myedtx_mail_30.setTitle("Email Address");

        myedtx_mail_30.setOnChangeListener(new MasterComponent.ChangeListener() {
            @Override
            public void after(String s) {
                if (MyEditextMail.emailValidation(s)){
                    bbtn_cek_31.setBackgroundResource(R.drawable.button_blue06_round_right);
                    emailvalid[0] = true;
                }
                else {
                    bbtn_cek_31.setBackgroundResource(R.drawable.button_blue06t30_round_right);
                }
            }
        });

        rvly_parent_00.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupConfig poup = new PopupConfig(getContext());
                poup.show();
                return false;
            }
        });

        bbtn_cek_31.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFinishListener != null && emailvalid[0]){
                    chekData();
                }
            }
        });


        bbtn_new_30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFinishListener != null){
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("status", "1");
                        jo.put("description", "New Email");
                        mData.put("DataCheck",jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mFinishListener.onNext(DataCheck.this, mData);
                }
            }
        });
    }

    public void chekData(){
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setMessage("Please Wait...");
        loading.show();
        LongRunningGetIO client = new LongRunningGetIO(getContext());
        client.setUrl("mobile/cekmail/");
        client.addParam(new KeyValueHolder("email",myedtx_mail_30.getValue()));
        client.execute();
        client.setOnReceiveListener(new LongRunningGetIO.onReceiveListener() {
            @Override
            public void onReceive(Object obj) {
                loading.dismiss();
                String      data        = (String) obj;
                JSONObject  jResponse   = null;

                Message message = new Message();
                message.what    = 1;
                Bundle bundle   = new Bundle();

                try {
                    jResponse = new JSONObject(data);
                    String code         = jResponse.get("code").toString() ;
                    String description  = jResponse.get("description").toString() ;
                    JSONArray ja        = jResponse.getJSONArray("body");
                    if (!ja.getJSONObject(0).get("code").toString().equals("null")){
                        AttendenzHolder att = new AttendenzHolder();
                        att.nama        = ja.getJSONObject(0).get("customer_name").toString();
                        att.birth       = ja.getJSONObject(0).get("customer_birthdate").toString();;
                        att.city        = ja.getJSONObject(0).get("customer_city").toString();;
                        att.email       = ja.getJSONObject(0).get("customer_email").toString();;
                        att.gender      = ja.getJSONObject(0).get("customer_gender").toString();;
                        att.complete    = true;
                        JSONObject jodata = new JSONObject();
                        jodata.put("GuestInformation", att.pack());
                        bundle.putString("DATA",jodata.toString());
                    }
                    else {
                        bundle.putString("DATA","-1");
                        Toast.makeText(getContext(),"Invalid Email !", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    bundle.putString("DATA","-1");
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Invalid Email !", Toast.LENGTH_SHORT).show();
                }

                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });

    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    try {
                        JSONObject jo = new JSONObject(bundle.getString("DATA"));
                        mData = jo;
                        Log.d(TAG, mData.toString());
                        mFinishListener.onSave(DataCheck.this, mData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
            return false;
        }
    });

    public void checkPrinter(){
        rvly_printer_20.setVisibility(View.GONE);
        Database mDB = new Database(getContext());
        BluethoothHolder bt = new BluethoothHolder().getData(mDB);
        if (bt.macaddress == null){
            rvly_printer_20.setVisibility(View.VISIBLE);
        }

        txvw_connectdevice_41.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PairingActivity.class);
                ((Activity)getContext()).startActivityForResult(intent, MainActivity.REQUEST_REGISTER_PRINTER);
            }
        });
    }

}
