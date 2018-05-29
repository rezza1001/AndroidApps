package g.rezza.moch.kiospos.Activitty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.MyEditextMail;
import g.rezza.moch.kiospos.component.PopupConfig;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.AttendenzHolder;
import g.rezza.moch.kiospos.holder.BluethoothHolder;
import g.rezza.moch.kiospos.holder.KeyValueHolder;
import g.rezza.moch.kiospos.lib.LongRunningGetIO;
import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.lib.MasterTransActivity;
import g.rezza.moch.kiospos.view.GuestInformation;

/**
 * Created by rezza on 17/11/17.
 */

public class DataCheckActivity extends MasterTransActivity{
    private static final String TAG = "DataCheckActivity";

    private RelativeLayout  rvly_parent_0;
    private RelativeLayout  rvly_printer_20;
    private MyEditextMail   myedtx_mail_30;
    private Button          bbtn_new_30;
    private Button          bbtn_cek_31;
    private TextView        txvw_connectdevice_41;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_check);
        showStep(6,0);

        myedtx_mail_30  = (MyEditextMail)   findViewById(R.id.myedtx_mail_30);
        bbtn_new_30     = (Button)          findViewById(R.id.bbtn_new_30);
        bbtn_cek_31     = (Button)          findViewById(R.id.bbtn_cek_31);
        rvly_parent_0   = (RelativeLayout)  findViewById(R.id.rvly_parent_0);
        rvly_printer_20 = (RelativeLayout)  findViewById(R.id.rvly_printer_20);
        txvw_connectdevice_41
                        = (TextView)        findViewById(R.id.txvw_connectdevice_41);

        registerListener();
        checkPrinter();

    }

    @Override
    protected void registerListener() {
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


        rvly_parent_0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupConfig poup = new PopupConfig(DataCheckActivity.this);
                poup.show();
                return false;
            }
        });

        bbtn_cek_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailvalid[0]){
                    chekData();
                }
            }
        });


        bbtn_new_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("status", "1");
                    jo.put("description", "New Email");
                   addObject(jo);
                    changeActivity(GuestInformationActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void chekData(){
        final ProgressDialog loading = new ProgressDialog(DataCheckActivity.this);
        loading.setMessage("Please Wait...");
        loading.show();
        LongRunningGetIO client = new LongRunningGetIO(DataCheckActivity.this);
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
                        addObject(att.pack());

                        changeActivity(GuestInformationActivity.class);
                    }
                    else {
                        Toast.makeText(DataCheckActivity.this,"Invalid Email !", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DataCheckActivity.this,"Invalid Email !", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }




    private void checkPrinter(){
        rvly_printer_20.setVisibility(View.GONE);
        Database mDB = new Database(DataCheckActivity.this);
        BluethoothHolder bt = new BluethoothHolder().getData(mDB);
        if (bt.macaddress == null){
            rvly_printer_20.setVisibility(View.VISIBLE);
        }

        txvw_connectdevice_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCheckActivity.this, PairingActivity.class);
                (DataCheckActivity.this).startActivityForResult(intent, MainActivity.REQUEST_REGISTER_PRINTER);
            }
        });
    }

}
