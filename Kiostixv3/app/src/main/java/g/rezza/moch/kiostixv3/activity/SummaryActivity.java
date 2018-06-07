package g.rezza.moch.kiostixv3.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.gson.JsonObject;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.DetailOrderAdapter;
import g.rezza.moch.kiostixv3.component.DetailOrderView;
import g.rezza.moch.kiostixv3.component.paymentMethod;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import g.rezza.moch.kiostixv3.database.SchedulesDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.lib.LoadingScreen;
import io.fabric.sdk.android.Fabric;

public class SummaryActivity extends AppCompatActivity {

    public static Activity summaryActivity ;
    private String TAG = "SummaryActivity";

    private DetailOrderView dovw_detail_00;
    private paymentMethod   payment_00;
    private TextView        txvw_title_00;
    private TextView        txvw_datetime_00;
    private TextView        txvw_time_00;
    private TextView        txvw_location_00;
    private TextView        txvw_name_00;
    private TextView        txvw_email_00;
    private TextView        txvw_phone_00;
    private BookingDB       mybookking = new BookingDB();
    private Button          bbtn_action_00;
    private ScrollView      scvw_body_00;
    private ImageView       imvw_back_00;
    private LoadingScreen   mLoading;
    private RelativeLayout  rvly_expired_00;
    private Button          bbtn_close_00;

    private CustomerDB      customerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summaryActivity = this;
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_summary);
        mybookking.getData(this);
        dovw_detail_00      = (DetailOrderView) findViewById(R.id.dovw_detail_00);
        payment_00          = (paymentMethod)   findViewById(R.id.payment_00);
        txvw_title_00       = (TextView)        findViewById(R.id.txvw_title_00);
        txvw_time_00        = (TextView)        findViewById(R.id.txvw_time_00);
        txvw_datetime_00    = (TextView)        findViewById(R.id.txvw_datetime_00);
        txvw_location_00    = (TextView)        findViewById(R.id.txvw_location_00);
        txvw_name_00        = (TextView)        findViewById(R.id.txvw_name_00);
        txvw_email_00       = (TextView)        findViewById(R.id.txvw_email_00);
        txvw_phone_00       = (TextView)        findViewById(R.id.txvw_phone_00);
        bbtn_action_00      = (Button)          findViewById(R.id.bbtn_action_00);
        bbtn_close_00       = (Button)          findViewById(R.id.bbtn_close_00);
        scvw_body_00        = (ScrollView)      findViewById(R.id.scvw_body_00);
        imvw_back_00        = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_expired_00     = (RelativeLayout)  findViewById(R.id.rvly_expired_00);
//        imvw_back_00.setVisibility(View.INVISIBLE);
        rvly_expired_00.setVisibility(View.GONE);
        bbtn_action_00.setVisibility(View.VISIBLE);
        create();
    }

    public void create(){
        mLoading = new LoadingScreen(this);

        customerDB = new CustomerDB();
        customerDB.getData(this);

        txvw_name_00.setText(customerDB.name);
        txvw_email_00.setText(customerDB.email);
        txvw_phone_00.setText(customerDB.phone);



        Calendar calendar = Calendar.getInstance();
        DateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Log.d(TAG,"EXPIRED : "+ mybookking.expired_date);
            Date date = dtformat.parse(mybookking.expired_date);
            calendar.setTime(date);
            Log.d(TAG,"EXPIRED : "+ calendar.getTime().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        calendar.add(Calendar.MINUTE,30);
        calculateDiff(calendar.getTime());


        payment_00.create();
        txvw_title_00.setText(mybookking.event_name);
        dovw_detail_00.setFee("0");
        dovw_detail_00.create(mybookking.event_category);
        txvw_datetime_00.setText(TextUtils.concat(mybookking.event_date,"    ",mybookking.event_time).toString());
        txvw_location_00.setText(mybookking.event_venue);


        bbtn_action_00.setVisibility(View.GONE);
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scvw_body_00.fullScroll(View.FOCUS_DOWN);
                if (dovw_detail_00.getSize() == 0){
                    Toast.makeText(SummaryActivity.this, getResources().getString(R.string.please_choose_category),
                            Toast.LENGTH_SHORT).show();
                }
                else if (payment_00.getData() == null){
                    Toast.makeText(SummaryActivity.this, getResources().getString(R.string.choose_payment_method),
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    mybookking.event_payment = payment_00.getData().toString();
                    mybookking.insert(SummaryActivity.this);

                }
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_close_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybookking.clearData(SummaryActivity.this);
                SchedulesDB schedulesDB = new SchedulesDB();
                schedulesDB.clearData(SummaryActivity.this);
                PaymentDB paymentDB = new PaymentDB();
                paymentDB.clearData(SummaryActivity.this);
                SummaryActivity.this.finish();
            }
        });

        payment_00.setOnPaymentClickListener(new paymentMethod.OnPaymentClickListener() {
            @Override
            public void onClick(JSONObject data) {
                try {
                    String paymentname = data.getString("NAME");
                    Intent intent = new Intent(SummaryActivity.this, PaymentActivity.class);
                    if (paymentname.equals("Credit Card")){
                        intent.putExtra("PAYMENT_CATEGORY",1);
                        intent.putExtra("PAYMENT_ID",data.getString("ID"));
                    }
                    else {
                        intent.putExtra("PAYMENT_CATEGORY",2);
                        intent.putExtra("PAYMENT_ID",data.getString("ID"));
                    }
                    SummaryActivity.this.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                SummaryActivity.this.finish();
            }
        });
    }

    public void calculateDiff(final Date workTime){
        Log.d(TAG,"calculateDiff : "+workTime.toString()+" - "+new Date().toString() );
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
                    if (b.getString("status").equals("OFF")){
                        rvly_expired_00.bringToFront();
                        rvly_expired_00.setVisibility(View.VISIBLE);
                        bbtn_action_00.setVisibility(View.GONE);
                    }
                    break;
            }
            return  false;
        }
    });


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.Confirmation))
                .setMessage(getResources().getString(R.string.confirm_cancel_order))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        clearBooking();
                        Intent intent = new Intent(SummaryActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        SummaryActivity.this.finish();
                    }})
                .setNegativeButton(getResources().getString(R.string.no), null).show();
    }

    private void clearBooking(){
        PaymentDB paymentDB = new PaymentDB();
        paymentDB.clearData(this);
    }
}
