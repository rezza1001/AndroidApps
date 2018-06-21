package g.rezza.moch.mobilesales.Activity.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.Database.SchedulesDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.CategoryAdapter;
import g.rezza.moch.mobilesales.component.CategoryQty;
import g.rezza.moch.mobilesales.component.MyDatePicker;
import g.rezza.moch.mobilesales.component.MyTimePicker;
import g.rezza.moch.mobilesales.lib.Parse;

public class EventCategoryActivity extends Activity implements CategoryQty.OnActionListener {

    String TAG = "EventCategoryActivity";

    private ListView            lsvw_category_00;
    private ScrollView          rvly_qty_00;
    private LinearLayout        lnly_body_00;
    private LinearLayout        lnly_category_00;
    private MyDatePicker        rvly_date_00;
    private MyTimePicker        rvly_time_00;
    private TextView            txvw_total_00;
    private TextView            txvw_title_00;
    private Button              bbtn_action_00;
    private ImageView           imvw_back_00;

    private long            mTotal = 0;
    private CategoryAdapter mAdapter;
    private ArrayList<SchedulesDB> filter_list = new ArrayList<>();
    private ArrayList<SchedulesDB> list = new ArrayList<>();
    private ArrayList<CategoryQty> mCategories = new ArrayList<>();
    private SchedulesDB schedule = new SchedulesDB();

    private BookingDB mybooking = new BookingDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        lsvw_category_00 = (ListView)       findViewById(R.id.lsvw_category_00);
        rvly_qty_00      = (ScrollView)     findViewById(R.id.rvly_qty_00);
        lnly_body_00     = (LinearLayout)   findViewById(R.id.lnly_body_00);
        lnly_category_00 = (LinearLayout)   findViewById(R.id.lnly_category_00);
        txvw_total_00    = (TextView)       findViewById(R.id.txvw_total_00);
        txvw_title_00    = (TextView)       findViewById(R.id.txvw_hdr_00);
        rvly_date_00     = (MyDatePicker)   findViewById(R.id.rvly_date_00);
        rvly_time_00     = (MyTimePicker)   findViewById(R.id.rvly_time_00);
        bbtn_action_00   = (Button)         findViewById(R.id.bbtn_action_00);
        imvw_back_00     = (ImageView)      findViewById(R.id.imvw_back_00);
        mAdapter         = new CategoryAdapter(this, filter_list);

        txvw_total_00.setText("IDR 0");
        lsvw_category_00.setAdapter(mAdapter);
        mybooking.getData(this);
        getSchedule();
        resetData();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (buildData()){
                Intent intent = new Intent(EventCategoryActivity.this, EventSummaryActivity.class);
                startActivity(intent);
                EventCategoryActivity.this.finish();
            }
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txvw_title_00.setText(mybooking.event_name);
    }

    private void getSchedule(){
        SchedulesDB schedule = new SchedulesDB();
        ArrayList<SchedulesDB> list = schedule.getDatas(this);
        for (SchedulesDB sclist: list){
            this.list.add(sclist);
            this.filter_list.add(sclist);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnSelectedListener(new CategoryAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(SchedulesDB event, int position) {
                mAdapter.notifyDataSetChanged();
                if (event.selected){
                    resetData();

                }
                else {
                    selectedSchedule(event, position);
                    createCategory();
                }
            }
        });
    }

    private void selectedSchedule(SchedulesDB scdb, int position ){
        schedule = scdb;
        filter_list.clear();
        for (SchedulesDB sc: EventCategoryActivity.this.list){
            if (sc.id.equals(schedule.id)){
                filter_list.add(sc);
            }
        }
        rvly_qty_00.setVisibility(View.VISIBLE);
        mAdapter.getItem(0).selected = true;
        startAnimation(lnly_body_00, position);
        mAdapter.notifyDataSetChanged();
    }


    private void startAnimation(View convertView, int position){
        TranslateAnimation animate = new TranslateAnimation(0,0,(position*300),0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        convertView.startAnimation(animate);
    }
    private void startAnimation2(View convertView, int position){
        TranslateAnimation animate = new TranslateAnimation(0,0,-100,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        convertView.startAnimation(animate);
    }

    private void createCategory(){
        mCategories.clear();
        lnly_category_00.removeAllViews();
        try {
            JSONArray jData = new JSONArray(schedule.data);
            for (int i=0; i<jData.length(); i++){
                JSONObject data = jData.getJSONObject(i);
                CategoryQty categoryView = new CategoryQty(this, null);
                categoryView.create("XXX"+i,data.getString("section_name"),data.getString("ticket_price"),0,10);
                lnly_category_00.addView(categoryView);
                categoryView.setOnActionListener(this);
                mCategories.add(categoryView);
            }
            Log.d(TAG, "JDAta "+ jData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void resetData(){
        schedule = null;
        mTotal = 0;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
        filter_list.clear();
        for (SchedulesDB sc: EventCategoryActivity.this.list){
            filter_list.add(sc);
        }
        rvly_qty_00.setVisibility(View.GONE);
        startAnimation2(lsvw_category_00, 1);
        mAdapter.clearAllSelected();
        mAdapter.notifyDataSetChanged();
    }

    public boolean buildData(){
        if (rvly_date_00.getValue().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.mdtp_select_day), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (rvly_time_00.getValue().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.mdtp_select_hours), Toast.LENGTH_SHORT).show();
            return false;
        }


        JSONArray jAdata = new JSONArray();
        if (mCategories.size() > 0){
            for (CategoryQty category: mCategories){
                if (category.getValueQty()>0){
                    JSONObject jsonCategory = new JSONObject();
                    try {
                        jsonCategory.put("ID", category.getValue().key);
                        jsonCategory.put("NAME", category.getTitle());
                        jsonCategory.put("EVENT_NAME", mybooking.event_name);
                        jsonCategory.put("VENUE",schedule.venue);
                        jsonCategory.put("QTY", category.getValue().value);
                        jsonCategory.put("PRICE", category.getPrice());
                        jsonCategory.put("TOTAL", category.getTotalAmount());
                        jAdata.put(jsonCategory);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (jAdata.length() > 0){
                mybooking.event_category    = jAdata.toString();
                mybooking.event_venue       = schedule.venue;
                mybooking.event_date        = rvly_date_00.getValue();
                mybooking.event_time        = rvly_time_00.getValue();
                mybooking.insert(this);
                return true;
            }
            else {
                Toast.makeText(this,getResources().getString(R.string.select_category),Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            Toast.makeText(this,getResources().getString(R.string.select_category),Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventCategoryActivity.this, EventDetailActivity.class);
        startActivity(intent);
        EventCategoryActivity.this.finish();
    }

    @Override
    public void onPlus(String pID, int qty, long value) {
        mTotal = mTotal+ value;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
    }

    @Override
    public void onMinus(String pID, int qty, long value) {
        mTotal = mTotal- value;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
    }
}
