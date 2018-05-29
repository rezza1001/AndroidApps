package g.rezza.moch.hrsystem.view.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import g.rezza.moch.hrsystem.R;
import g.rezza.moch.hrsystem.adapter.AbsentHistory;
import g.rezza.moch.hrsystem.component.ScanOffice;
import g.rezza.moch.hrsystem.connection.PostManager;
import g.rezza.moch.hrsystem.database.AbsentDB;
import g.rezza.moch.hrsystem.database.EmployeesDB;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.AbsentHistoryHolder;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import g.rezza.moch.hrsystem.lib.ErrorCode;
import g.rezza.moch.hrsystem.lib.GPSTracker;
import io.fabric.sdk.android.Fabric;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Konstantin on 22.12.2014.
 */
public class AbsenFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String NAME     = "Attendance";
    public static final String TAG      = "AbsenFragment";

    private View            containerView;
    private ScanOffice      scn_office_00;
    private TextView        txvw_date_00;
    private TextView        txvw_diff_00;
    private TextView        txvw_diffout_00;
    private TextView        txvw_work_out_00;
    private TextClock       txcl_time_00;
    private ListView        lsvw_history_00;
    private TextView        txvw_empty_00;
    private TextView        txvw_work_time_00;
    private RippleBackground content_loading_00;
    private AbsentHistory   adapter;
    private ArrayList<AbsentHistoryHolder> list_absent = new ArrayList<>();
    private UserDB user;

    private Resources r;

    public static AbsenFragment newInstance() {
        AbsenFragment contentFragment = new AbsenFragment();
        Bundle bundle = new Bundle();
        contentFragment.setArguments(bundle);

        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fabric.with(getActivity(), new Crashlytics(), new CrashlyticsNdk());
        r = getResources();
        user = new UserDB();
        user.getMine(getActivity());
        this.containerView  = view.findViewById(R.id.container);
        scn_office_00       = view.findViewById(R.id.scn_office_00);
        txvw_date_00        = view.findViewById(R.id.txvw_date_00);
        txvw_diff_00        = view.findViewById(R.id.txvw_diff_00);
        txvw_diffout_00        = view.findViewById(R.id.txvw_diffout_00);
        txvw_work_out_00        = view.findViewById(R.id.txvw_work_out_00);
        txcl_time_00        = view.findViewById(R.id.txcl_time_00);
        txvw_work_time_00   = view.findViewById(R.id.txvw_work_time_00);
        content_loading_00  = view.findViewById(R.id.content_loading_00);
        lsvw_history_00     = view.findViewById(R.id.lsvw_history_00);
        txvw_empty_00       = view.findViewById(R.id.txvw_empty_00);
        adapter             = new AbsentHistory(getActivity(), list_absent);
        lsvw_history_00.setAdapter(adapter);

        content_loading_00.setVisibility(View.GONE);
        content_loading_00.stopRippleAnimation();

        scn_office_00.getLonglat();
        content_loading_00.bringToFront();
        createCurrentDate();

        scn_office_00.setOnScanningListener(new ScanOffice.OnScanningListener() {
            @Override
            public void onScan() {
                content_loading_00.setVisibility(View.VISIBLE);
                content_loading_00.startRippleAnimation();
            }

            @Override
            public void onfinish() {
                content_loading_00.setVisibility(View.GONE);
                content_loading_00.stopRippleAnimation();
            }
        });

        content_loading_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scn_office_00.setOnAfterListener(new ScanOffice.AfterActionListener() {
            @Override
            public void after() {
                requestDataToday();
            }
        });

        AbsentDB absent = new AbsentDB();
        ArrayList<AbsentDB> list = absent.getData(getActivity());
        if (list.size() == 0){
            requestDataHistory();
        }
        else {
            loadDataHistory();
            requestDataToday();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }



    private void requestDataHistory(){
        PostManager post = new PostManager(getActivity());
        post.setApiUrl("absent_history");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("employee_id",user.id));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray data = obj.getJSONArray("data");

                        AbsentDB absent = new AbsentDB();
                        absent.clearData(getActivity());
                        for (int i=0; i<data.length(); i++){
                            absent = new AbsentDB();
                            absent.absent_header = data.getJSONObject(i).getInt("id");
                            absent.check_in      = data.getJSONObject(i).getString("check_in");
                            absent.check_out     = data.getJSONObject(i).getString("check_out");
                            absent.status_desc   = data.getJSONObject(i).getString("status");
                            absent.note          = data.getJSONObject(i).getString("note");
                            absent.work_date     = data.getJSONObject(i).getString("work_date");
                            absent.work_time     = data.getJSONObject(i).getString("work_start");
                            absent.work_end_time = data.getJSONObject(i).getString("work_end");
                            absent.late          = data.getJSONObject(i).getString("max_late");
                            absent.insert(getActivity());
                        }
                        loadDataHistory();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                requestDataToday();;
            }
        });
    }

    private void loadDataHistory(){
        list_absent.clear();
        AbsentDB absent = new AbsentDB();
        ArrayList<AbsentDB> list = absent.getData(getActivity());
        if (list.size() > 0){
            txvw_empty_00.setVisibility(View.GONE);
            lsvw_history_00.setVisibility(View.VISIBLE);
        }
        else {
            lsvw_history_00.setVisibility(View.GONE);
            txvw_empty_00.setVisibility(View.VISIBLE);
        }
        for (AbsentDB absentDB : list){
            AbsentHistoryHolder history = new AbsentHistoryHolder();
            history.hitory_header = absentDB.absent_header + "";
            history.check_in      = absentDB.check_in.equalsIgnoreCase("null")?"-":absentDB.check_in;
            history.check_out     = absentDB.check_out.equalsIgnoreCase("null")?"-":absentDB.check_out;

            TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
            DateFormat formatDate1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat formatDate2 = new SimpleDateFormat("EEEE, dd MMM yyyy");
            formatDate2.setTimeZone(tz);
            try {
                Date work_date = formatDate1.parse(absentDB.work_date);
                history.date   = absentDB.work_date;
                history.date_view = formatDate2.format(work_date);
                Log.d(TAG,formatDate2.format(work_date) +" | "+ absentDB.work_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            list_absent.add(history);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG","ONRESUME");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.component_absen_fragment, container, false);
        return rootView;
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
//                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
//                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                containerView.draw(canvas);
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    private void requestDataToday(){
        EmployeesDB  employeesDB = new EmployeesDB();
        employeesDB.getData(getActivity(), ""+user.id);
        PostManager post = new PostManager(getActivity());
        post.setApiUrl("absent_today");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("employee_id",employeesDB.id));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject data = obj.getJSONObject("data");
                        AbsentDB absent = new AbsentDB();
                        absent.deleteData(getActivity(),AbsentDB.FIELD_ABSENT_HEDAER+" = "+ data.getInt("id"));
                        absent.absent_header = data.getInt("id");
                        absent.check_in      = data.getString("check_in");
                        absent.check_out     = data.getString("check_out");
                        absent.status_desc   = data.getString("status");
                        absent.note          = data.getString("note");
                        absent.work_date     = data.getString("work_date");
                        absent.work_time     = data.getString("work_start");
                        absent.work_end_time = data.getString("work_end");
                        absent.late          = data.getString("max_late");
                        absent.insert(getActivity());
                        txvw_work_time_00.setText(absent.work_time);
                        createDivTime(absent.work_time,1);
                        createDivTime(absent.work_end_time, 2);
                        loadDataHistory();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void createCurrentDate(){
        DateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
        Date currentTime      = Calendar.getInstance().getTime();
        Calendar calendar     = Calendar.getInstance();
        int dayOfWeek         = calendar.get(Calendar.DAY_OF_WEEK);
        txvw_date_00.setText(formatDate.format(currentTime));

        String[] days = new String[] {"-", r.getString(R.string.sunday),
                r.getString(R.string.monday), r.getString(R.string.tuesday),
                r.getString(R.string.wednesday),  r.getString(R.string.thursday),
                r.getString(R.string.friday), r.getString(R.string.saturday) };
        txvw_date_00.setText( days[dayOfWeek]+", " + formatDate.format(currentTime));

    }

    private void createDivTime(String worktime, int type){
        Calendar calendar     = Calendar.getInstance();
        int year              = calendar.get(Calendar.YEAR);
        int month             = calendar.get(Calendar.MONTH);
        int day                     = calendar.get(Calendar.DAY_OF_MONTH);
        final Date currentTime      = calendar.getTime();

        final Calendar workTime     = Calendar.getInstance();
        String work_time    = worktime.substring(0,5);
        int hour            = Integer.parseInt(work_time.substring(0,2));
        int minute          = Integer.parseInt(work_time.substring(3,5));
        Log.d(TAG,"Hour "+ hour+" : "+ work_time.substring(3,5));
        workTime.set(year,month,day,hour,minute);
        DateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
        boolean isToday       = currentTime.getTime() > workTime.getTime().getTime();

        if (isToday){
            workTime.add(Calendar.DAY_OF_MONTH, 1);
            calculateDiff(workTime.getTime(), type);
        }
        else {
            calculateDiff(workTime.getTime(), type);
        }

    }

    public void calculateDiff(final Date workTime, final int status){
        Timer updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    long mills = workTime.getTime() - new Date().getTime();
                    int hours = (int) ((mills / (1000 * 60 * 60)));
                    int minutes = (int) ((mills / (1000 * 60)) % 60);

                    String diff = hours + " H : " + minutes+" M"; // updated value every1 second
                    if (hours >= 8){
                        diff = "-";
                    }


                    Message message = new Message();
                    message.what = status;
                    Bundle bundle = new Bundle();
                    bundle.putString("diff",diff);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 0, 60000); // here 1000 means 1000 mills i.e. 1 second
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Bundle b = message.getData();
            switch (message.what){
                case 1:
                    txvw_diff_00.setText("( "+b.getString("diff")+" )");
                    break;
                case 2:
                    txvw_diffout_00.setText("( "+b.getString("diff")+" )");
                    break;
            }

            return  false;
        }
    });

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult "+ requestCode);
        if (requestCode == GPSTracker.REQUEST_SETTING){
            if (resultCode != 0 ){
                scn_office_00.getLonglat();
            }
        }
    }
}

