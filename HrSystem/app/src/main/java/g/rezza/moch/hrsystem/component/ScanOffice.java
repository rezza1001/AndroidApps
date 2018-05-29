package g.rezza.moch.hrsystem.component;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import g.rezza.moch.hrsystem.R;
import g.rezza.moch.hrsystem.connection.PostManager;
import g.rezza.moch.hrsystem.database.AbsentDB;
import g.rezza.moch.hrsystem.database.EmployeesDB;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import g.rezza.moch.hrsystem.lib.GPSTracker;

/**
 * Created by rezza on 25/01/18.
 */

public class ScanOffice extends RelativeLayout {

    public static final String TAG = "ScanOffice";
    public static final int START   = 1;
    public static final int STOP    = 2;

    private TextView        texvw_status_00;
    private TextView        texvw_check_00;
    private LinearLayout    lnly_status_00;
    private LinearLayout    lnly_check_00;

    private GPSTracker gps;
    private Resources r;
    private Timer updateTimer   = new Timer();
    private boolean isFound     = false;
    private boolean isAbsent     = false;
    private boolean isScanning  = false;
    private String distanceLoc  = "0 M";
    private AbsentDB absentDB;
    private UserDB   user;
    private double longitude = 0;
    private double latitude = 0;


    public ScanOffice(Context context) {
        super(context, null);
    }
    public ScanOffice(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_scan_office, this, true);
        r = context.getResources();
        texvw_status_00 = (TextView) findViewById(R.id.texvw_status_00);
        texvw_check_00  = (TextView) findViewById(R.id.texvw_check_00);
        lnly_status_00  = (LinearLayout) findViewById(R.id.lnly_status_00);
        lnly_check_00   = (LinearLayout) findViewById(R.id.lnly_check_00);

        absentDB = new AbsentDB();
        absentDB.getToday(context);

        user = new UserDB();
        user.getMine(context);

        gps = new GPSTracker(getContext());

        lnly_status_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessageDelayed(START,0);
            }
        });
        lnly_check_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"checkIn() : isFound "+ isFound+"| isAbsent "+isAbsent);
                if (isFound && isAbsent){
                    absentDB.getToday(getContext());
                    if (absentDB.check_in.isEmpty() || absentDB.check_in.equalsIgnoreCase("null")){
                        checkIn();
                    }
                    else {
                        checkOut();
                    }
                }
            }
        });

        getLonglat();
    }

    public void getLonglat(){
        PostManager post = new PostManager(getContext());
        post.setApiUrl("get_longlat");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("absent_header","123"));
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                //{"error_code":"200","error_desc":"Sukses","data":{"longitude":"106.830823","latitude":"-6.253775"}}
                Log.d(TAG,"Longlat from server "+ obj.toString());
                try {
                    JSONObject data = obj.getJSONObject("data");
                    String longitude    = data.getString("longitude");
                    String latitude     = data.getString("latitude");
                    ScanOffice.this.longitude = Double.parseDouble(longitude);
                    ScanOffice.this.latitude = Double.parseDouble(latitude);
                    if (!gps.statusGPS()){
                        return;
                    }
                    handler.sendEmptyMessage(START);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkIn(){

        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calEndWork =Calendar.getInstance();
        String workstart        = absentDB.work_date+ " " +absentDB.work_time;
        try {
            calEndWork.setTime(formatDate.parse(workstart));
            Log.d(TAG,formatDate.format(calEndWork.getTime())+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        NoteDialog noteDialog = new NoteDialog(getContext());
        if (calendar.after(calEndWork)){
            noteDialog.create(getResources().getString(R.string.note),
                    getResources().getString(R.string.you_late));
            noteDialog.setHint(getResources().getString(R.string.please_insert_note));
        }
        else {
            sendcheckIn("On Time");
        }

        noteDialog.setOnActionListener(new NoteDialog.OnActionListener() {
            @Override
            public void onPositive(String message) {
                sendcheckIn(message);
            }

            @Override
            public void onNegative() {

            }
        });

    }

    private void sendcheckIn(String note){

        EmployeesDB employeesDB = new EmployeesDB();
        employeesDB.getData(getContext(),""+ user.id);

        PostManager post = new PostManager(getContext());
        post.setApiUrl("check_in");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("absent_header",absentDB.absent_header));
        kvs.add(new KeyValueHolder("employee_id",employeesDB.id));
        kvs.add(new KeyValueHolder("note",note));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                texvw_check_00.setText("Check Out");
                lnly_check_00.setBackgroundResource(R.drawable.button_red_01);
                absentDB.update(getContext());
                absentDB.getToday(getContext());
                if (mAfetrListener != null){
                    mAfetrListener.after();
                }
            }
        });
    }

    private void checkOut(){
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calEndWork =Calendar.getInstance();
        String workend        = absentDB.work_date+ " " +absentDB.work_end_time;
        try {
            calEndWork.setTime(formatDate.parse(workend));
            Log.d(TAG,formatDate.format(calEndWork.getTime())+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        NoteDialog noteDialog = new NoteDialog(getContext());
        if (calendar.before(calEndWork)){
            noteDialog.create(getResources().getString(R.string.note),
                              getResources().getString(R.string.are_you_sure_to_checkout));
            noteDialog.setHint(getResources().getString(R.string.please_insert_note));
        }
        else {
            sendCheckout("On Time");
        }

        noteDialog.setOnActionListener(new NoteDialog.OnActionListener() {
            @Override
            public void onPositive(String message) {
                sendCheckout(message);
            }

            @Override
            public void onNegative() {

            }
        });

    }

    private void sendCheckout(String note){
        EmployeesDB employeesDB = new EmployeesDB();
        employeesDB.getData(getContext(),""+ user.id);

        PostManager post = new PostManager(getContext());
        post.setApiUrl("check_out");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("absent_header",absentDB.absent_header));
        kvs.add(new KeyValueHolder("employee_id",employeesDB.id));
        kvs.add(new KeyValueHolder("note",note));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                isFound = false;
                lnly_check_00.setBackgroundResource(R.drawable.button_grey_off);
                texvw_check_00.setText("-");
                absentDB.update(getContext());
                absentDB.getToday(getContext());
                if (mAfetrListener != null){
                    mAfetrListener.after();
                }
            }
        });
    }

    public void checkLocation(){
        Log.d(TAG,"checkLocation : "+gps.getLocation());
        if(gps.canGetLocation() && gps.getLocation()!=null ){
            Location target = new Location("target");
            target.setLongitude(longitude);
            target.setLatitude(latitude);

            double distance = gps.getLocation().distanceTo(target);
            DecimalFormat df = new DecimalFormat("#.00");
            Log.d(TAG,"gps.getLocation() "+gps.getLocation().distanceTo(target));
            if (gps.getLocation().distanceTo(target) < 50f){
                isFound = true;
            }
            else {
                isFound = false;
                distanceLoc = df.format(distance) + " M";
            }
        }
        else {
            isFound = false;
        }


    }


    private void chekPeriod(){
        updateTimer   = new Timer();
        updateTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    handler.post(new Runnable() {
                        public void run() {
                            if (!isFound){

                                checkLocation();
                            }
                            else {
                                Log.d(TAG,"Found Location !!");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }, 0, 2000); // here 1000 means 1000 mills i.e. 1 second
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case START:
                    if (!isScanning){

                        if (mListener != null){
                            mListener.onScan();
                        }
                        isScanning = true;
                        lnly_check_00.setBackgroundResource(R.drawable.button_grey_off);
                        texvw_status_00.setText(r.getString(R.string.scanning));
                        chekPeriod();
                        handler.sendEmptyMessageDelayed(STOP,10000);
                    }
                    break;
                case STOP:
                    isScanning = false;
                    updateTimer.cancel();
                    Log.d(TAG,"STOP SCANNING !! "+ isFound);

                    if (isFound){
                        texvw_status_00.setText(r.getString(R.string.in_office));
                        refreshButton();

                    }
                    else {
                        texvw_status_00.setText(r.getString(R.string.out_office)+" ("+distanceLoc+")");
                    }

                    if (mListener != null){
                        mListener.onfinish();
                    }
                    break;
            }
            return false;
        }
    });

    public void refreshButton(){
        absentDB = new AbsentDB();
        absentDB.getToday(getContext());
        Log.d(TAG, "Absent Header "+ absentDB.absent_header);
        if (absentDB.absent_header == 0){
            isFound = false;
            lnly_check_00.setBackgroundResource(R.drawable.button_grey_off);
            texvw_check_00.setText("-");
            return;
        }
        if (absentDB.check_in.isEmpty()||absentDB.check_in.equalsIgnoreCase("null") ){
            lnly_check_00.setBackgroundResource(R.drawable.button_green);
            texvw_check_00.setText("Check In");
            isAbsent = true;
        }
        else if (absentDB.check_out.isEmpty() || absentDB.check_out.equalsIgnoreCase("null")){
            lnly_check_00.setBackgroundResource(R.drawable.button_red_01);
            texvw_check_00.setText("Check Out");
            isAbsent = true;
        }
        else {
            isFound = false;
            isAbsent = false;
            lnly_check_00.setBackgroundResource(R.drawable.button_grey_off);
            texvw_check_00.setText("-");
        }
    }

    private OnScanningListener mListener;
    public void setOnScanningListener(OnScanningListener pListener){
        mListener = pListener;
    }
    public interface OnScanningListener{
        public void onScan();
        public void onfinish();
    }

    private AfterActionListener mAfetrListener;
    public void setOnAfterListener(AfterActionListener afterListener){
        mAfetrListener = afterListener;
    }
    public interface AfterActionListener{
        public void after();
    }
}
