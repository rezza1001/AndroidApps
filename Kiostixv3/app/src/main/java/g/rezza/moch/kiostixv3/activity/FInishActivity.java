package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.DetailOrderView;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import io.fabric.sdk.android.Fabric;

public class FInishActivity extends AppCompatActivity {

    private DetailOrderView dovw_detail_00;
    private TextView        txvw_title_00;
    private TextView        txvw_datetime_00;
    private TextView        txvw_time_00;
    private TextView        txvw_location_00;
    private TextView        txvw_payment_00;
    private TextView        txvw_status_00;
    private TextView        txvw_transid_00;
    private TextView        txvw_virtual_00;
    private Button          bbtn_action_00;
    private LinearLayout    lnly_virtual_00;
    private LinearLayout    lnly_time_00;
    private BookingDB       mybookking = new BookingDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_finish);
        dovw_detail_00      = (DetailOrderView) findViewById(R.id.dovw_detail_00);
        txvw_time_00        = (TextView)        findViewById(R.id.txvw_time_00);
        txvw_title_00       = (TextView)        findViewById(R.id.txvw_title_00);
        txvw_time_00        = (TextView)        findViewById(R.id.txvw_time_00);
        txvw_datetime_00    = (TextView)        findViewById(R.id.txvw_datetime_00);
        txvw_location_00    = (TextView)        findViewById(R.id.txvw_location_00);
        txvw_payment_00     = (TextView)        findViewById(R.id.txvw_payment_00);
        txvw_status_00      = (TextView)        findViewById(R.id.txvw_status_00);
        txvw_transid_00     = (TextView)        findViewById(R.id.txvw_transid_00);
        txvw_virtual_00     = (TextView)        findViewById(R.id.txvw_virtual_00);
        bbtn_action_00      = (Button)          findViewById(R.id.bbtn_action_00);
        lnly_virtual_00     = (LinearLayout)    findViewById(R.id.lnly_virtual_00);
        lnly_time_00        = (LinearLayout)    findViewById(R.id.lnly_time_00);

        create();
    }



    public void create(){
        mybookking.getData(this);
        PaymentDB paymentDB = new PaymentDB();
        paymentDB.getData(this, mybookking.payment_id);

        if (paymentDB.name.equals("Credit Card")){
            lnly_virtual_00.setVisibility(View.GONE);
        }

        if (mybookking.trans_status.equals("Pending")){
            Calendar calendar = Calendar.getInstance();
            DateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = dtformat.parse(mybookking.expired_date);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//        calendar.add(Calendar.MINUTE,30);
            calculateDiff(calendar.getTime());
        }
        else {
            lnly_time_00.setVisibility(View.GONE);
        }

        txvw_payment_00.setText(paymentDB.name);
        txvw_status_00.setText(mybookking.trans_status);
        txvw_transid_00.setText(mybookking.order_id );
        txvw_virtual_00.setText(mybookking.virtual_account);


        dovw_detail_00.setReadOnly();
        txvw_title_00.setText(mybookking.event_name);
        dovw_detail_00.setFee(paymentDB.fee_value);
        dovw_detail_00.create(mybookking.event_category);
        txvw_datetime_00.setText(TextUtils.concat(mybookking.event_date,"    ",mybookking.event_time).toString());
        txvw_location_00.setText(mybookking.event_venue);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void calculateDiff(final Date workTime){
        Timer updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    long mills = workTime.getTime() - new Date().getTime();
                    int hours = (int) ((mills / (1000 * 60 * 60)));
                    int minutes = (int) ((mills / (1000 * 60)) % 60);
                    int seconds = (int) (mills / 1000) % 60 ;

                    String diff = hours + " hour " + minutes+" min " +" "+seconds+" sec";
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("diff",diff);
                    bundle.putString("status","RUN");

                    if (hours == 0 && minutes == 0 && seconds == 0){
                        this.cancel();
                        bundle.putString("status","OFF");
                    }
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000); // here 1000 means 1000 mills i.e. 1 second
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle b = message.getData();
            switch (message.what){
                case 1:
                    txvw_time_00.setText(b.getString("diff"));
                    break;
            }
            return  false;
        }
    });

    @Override
    public void onBackPressed() {
        finish();
    }
}
