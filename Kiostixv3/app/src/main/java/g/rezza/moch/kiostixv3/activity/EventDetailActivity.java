package g.rezza.moch.kiostixv3.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.MyScrollView;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import g.rezza.moch.kiostixv3.database.SchedulesDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.lib.MyViewPager;
import g.rezza.moch.kiostixv3.view.fragment.detail_event.EventDetailFragment;
import g.rezza.moch.kiostixv3.view.fragment.detail_event.TermConditionFragment;
import io.fabric.sdk.android.Fabric;

public class EventDetailActivity extends AppCompatActivity {

    private PagerSlidingTabStrip tabs;
    private MyViewPager     pager;
    private MyPagerAdapter  adapter;
    private int             currentColor = 0;
    private RelativeLayout  rvly_header_00;
    private RelativeLayout  rvly_sparator_01;
    private MyScrollView    scvw_body_00;
    private ImageView       imageView2;
    private ImageView       imvw_back_00;
    private ImageView       imvw_share_00;
    private RelativeLayout  rvly_sparator_00;
    private TextView        txvw_title_00;
    private Button          bbtn_action_00;

    private BookingDB       mybooking = new BookingDB();
    private EventDetailFragment eventDetailFragment;
    private TermConditionFragment termConditionFragment;

    private boolean inBottom = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_event_detail);
        mybooking.getData(this);

        tabs            = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager           = (MyViewPager)          findViewById(R.id.pager);
        adapter         = new MyPagerAdapter(getSupportFragmentManager());

        scvw_body_00    = (MyScrollView)        findViewById(R.id.scvw_body_00);
        rvly_header_00  = (RelativeLayout)      findViewById(R.id.rvly_header_00);

        imageView2      = (ImageView)           findViewById(R.id.imageView2);
        imvw_back_00    = (ImageView)           findViewById(R.id.imvw_back_00);
        imvw_share_00   = (ImageView)           findViewById(R.id.imvw_share_00);
        rvly_sparator_00= (RelativeLayout)      findViewById(R.id.rvly_sparator_00);
        rvly_sparator_01= (RelativeLayout)      findViewById(R.id.rvly_sparator_01);

        txvw_title_00   = (TextView)            findViewById(R.id.txvw_title_00);
        bbtn_action_00  = (Button)              findViewById(R.id.bbtn_action_00);

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setAllCaps(false);

        scvw_body_00.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scvw_body_00.getScrollY(); // For ScrollView

                Rect scrollBounds = new Rect();
                scvw_body_00.getHitRect(scrollBounds);
                if (rvly_sparator_00.getLocalVisibleRect(scrollBounds)) {
                    animateColor(rvly_header_00, "backgroundColor", 0);
                } else {
                    animateColor(rvly_header_00, "backgroundColor", getResources().getColor(R.color.colorPrimaryDark));;
                }

                if (rvly_sparator_01.getLocalVisibleRect(scrollBounds)){
                    inBottom = true;
                }
                else {
                    inBottom = false;
                }
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (eventDetailFragment != null){

                CustomerDB customerDB = new CustomerDB();
                customerDB.getData(EventDetailActivity.this);
                if (customerDB.id.isEmpty()){
                    Intent intent = new Intent(EventDetailActivity.this, BlockUserActivity.class);
                    startActivityForResult(intent,App.REQUEST_AUTH);
                    return;
                }

                Intent intent = new Intent(EventDetailActivity.this, CategoryActivity.class);
                startActivity(intent);
                EventDetailActivity.this.finish();
            }
            else {
                Toast.makeText(EventDetailActivity.this,
                        getResources().getString(R.string.error_process),
                        Toast.LENGTH_SHORT).show();
            }
            }
        });

        imvw_share_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        requestData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(this).load(mybooking.event_image).into(imageView2);
        txvw_title_00.setText(mybooking.event_name);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Detail Event", "Syarat & Ketentuan"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if (eventDetailFragment == null){
                        eventDetailFragment= new EventDetailFragment();
                    }
                    return eventDetailFragment;
                default:
                    if (termConditionFragment == null){
                        termConditionFragment = new TermConditionFragment();
                    }
                    return termConditionFragment;
            }

        }

    }

    public void animateColor(View v, String propertyName, int endColor){
        if (currentColor == endColor){
            return;
        }
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(
                v, // Target object
                propertyName, // Property name
                currentColor, // Value
                endColor // Value
        );
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();

        currentColor = endColor;
    }

    private void requestData(){
        PostManager post = new PostManager(this);
        post.setApiUrl("event/details");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("event_id",mybooking.event_id));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONObject  data = obj.getJSONArray("data").getJSONObject(0);
                        createScheduleData(data.getJSONArray("schedule_data"));
                        mybooking.event_desc =  data.getString("event_description");
                        mybooking.event_term =  data.getString("event_term");
                        JSONArray schedule  =  data.getJSONArray("schedule_data");
                        mybooking.event_start_date  = schedule.getJSONObject(0).getString("schedule_start_date");
                        mybooking.event_end_date    = schedule.getJSONObject(0).getString("schedule_end_date");
                        mybooking.insert(EventDetailActivity.this);
                        eventDetailFragment.create();
                        termConditionFragment.create();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(EventDetailActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(1,1000);
                }

            }
        });
    }

    private void share(){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "https://kiostix.com/id/event/138/motogp-thailand-2018");
        startActivity(Intent.createChooser(share, "Share Event Via"));
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            onBackPressed();
            return false;
        }
    });


    private void createScheduleData(JSONArray data){
        SchedulesDB scDB = new SchedulesDB();
        scDB.clearData(this);
        try {
            for (int i=0; i<data.length(); i++){
                JSONObject sc = data.getJSONObject(i);
                SchedulesDB schedule = new SchedulesDB();
                schedule.id     = "x"+i;
                schedule.name   = sc.getString("schedule_name");
                schedule.venue   = sc.getString("venue_name");
                schedule.date   = sc.getString("schedule_date");
                schedule.data   = sc.getString("section_data");
                schedule.time   = sc.getString("schedule_time");
                schedule.insert(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == App.REQUEST_AUTH){

        }

    }
}
