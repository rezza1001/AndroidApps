package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.CategoryAdapter;
import g.rezza.moch.kiostixv3.component.CategoryQty;
import g.rezza.moch.kiostixv3.component.MyDatePicker;
import g.rezza.moch.kiostixv3.component.SimpleSpinner;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import g.rezza.moch.kiostixv3.database.SchedulesDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.SpinerHolder;
import g.rezza.moch.kiostixv3.lib.Parse;
import io.fabric.sdk.android.Fabric;

public class CategoryActivity extends AppCompatActivity implements CategoryQty.OnActionListener{
    private String TAG = "CategoryActivity";

    private ListView        lsvw_category_00;
    private ScrollView      rvly_qty_00;
    private LinearLayout    lnly_body_00;
    private LinearLayout    lnly_category_00;
    private MyDatePicker    rvly_date_00;
    private SimpleSpinner   rvly_time_00;
    private TextView        txvw_total_00;
    private TextView        txvw_title_00;
    private Button          bbtn_action_00;
    private ImageView       imvw_back_00;

    private long            mTotal = 0;
    private BookingDB       mybooking = new BookingDB();
    private CategoryAdapter mAdapter;
    private ArrayList<SchedulesDB> filter_list = new ArrayList<>();
    private ArrayList<SchedulesDB> list = new ArrayList<>();
    private ArrayList<CategoryQty> mCategories = new ArrayList<>();

    private SchedulesDB schedule = new SchedulesDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_category);

        lsvw_category_00 = (ListView)       findViewById(R.id.lsvw_category_00);
        rvly_qty_00      = (ScrollView)     findViewById(R.id.rvly_qty_00);
        lnly_body_00     = (LinearLayout)   findViewById(R.id.lnly_body_00);
        lnly_category_00 = (LinearLayout)   findViewById(R.id.lnly_category_00);
        txvw_total_00    = (TextView)       findViewById(R.id.txvw_total_00);
        txvw_title_00    = (TextView)       findViewById(R.id.txvw_title_00);
        rvly_date_00     = (MyDatePicker)   findViewById(R.id.rvly_date_00);
        rvly_time_00     = (SimpleSpinner)   findViewById(R.id.rvly_time_00);
        bbtn_action_00   = (Button)         findViewById(R.id.bbtn_action_00);
        imvw_back_00     = (ImageView)      findViewById(R.id.imvw_back_00);
        mAdapter         = new CategoryAdapter(this, filter_list);

        txvw_total_00.setText("IDR 0");
        lsvw_category_00.setAdapter(mAdapter);
        mybooking.getData(this);
        getSchedule();
        resetData();

        txvw_title_00.setText(mybooking.event_name);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buildData()){
                    sendBooking();
                }
            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
        for (SchedulesDB sc: CategoryActivity.this.list){
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

    @Override
    public void onBackPressed() {
        if (schedule == null){
            Intent intent = new Intent(this,EventDetailActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            resetData();
        }
    }

    private void createCategory(){
        mCategories.clear();
        lnly_category_00.removeAllViews();
        try {
            JSONArray jData = new JSONArray(schedule.data);
            for (int i=0; i<jData.length(); i++){

                // Create list of section
                JSONObject data = jData.getJSONObject(i);
                CategoryQty categoryView = new CategoryQty(this, null);
                categoryView.create(data.getString("ticket_id"),data.getString("ticket_name"),data.getString("ticket_price"),0,10);
                lnly_category_00.addView(categoryView);
                categoryView.setOnActionListener(this);
                mCategories.add(categoryView);
            }


            // Create Time Schedule
            JSONArray times = new JSONArray(schedule.time);
            ArrayList<SpinerHolder> spnr = new ArrayList<>();
            for (int i=0; i<times.length(); i++){
                spnr.add(new SpinerHolder(times.getJSONObject(i).getString("id"),times.getJSONObject(i).getString("time"),1));
            }
            if (spnr.size() <= 1){
                rvly_time_00.setVisibility(View.GONE);
            }
            else {
                rvly_time_00.setVisibility(View.VISIBLE);
            }
            rvly_time_00.setChoosers(spnr);

            // Create Date Schedule
            Calendar defaultDate = Calendar.getInstance();
            JSONArray dates = new JSONArray(schedule.date);
            ArrayList<Calendar> mCals = new ArrayList<>();
            DateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i=0; i<dates.length(); i++){

               Calendar calendar = Calendar.getInstance();
               calendar.setTime(dtformat.parse(dates.getString(i)));
               defaultDate.setTime(dtformat.parse(dates.getString(0)));
               mCals.add(calendar);
               if (i ==0){
                   rvly_date_00.setValue(calendar.getTime());
               }
            }
            rvly_date_00.init();
            rvly_date_00.enableDates(mCals);
            if (mCals.size() == 1){
                rvly_date_00.setValue(defaultDate.getTime());
            }
            else if (mCals.size() == 0){
                rvly_date_00.disable();
                rvly_date_00.setOnChangeListener(new MyDatePicker.ChangeListener() {
                    @Override
                    public void after(String s) {
                        if (s.equals(MyDatePicker.DISABLE)){
                            Toast.makeText(CategoryActivity.this, getResources().getString(R.string.date_and_time_not_available),Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            Log.d(TAG, "JDAta "+ jData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPlus(int qty, long price) {
        mTotal = mTotal+ price;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
    }

    @Override
    public void onMinus(int qty, long price) {
        mTotal = mTotal- price;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
    }

    private void resetData(){
        mCategories.clear();
        rvly_date_00.init();
        schedule = null;
        mTotal = 0;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(""+mTotal));
        filter_list.clear();
        for (SchedulesDB sc: CategoryActivity.this.list){
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

        JSONArray jAdata = new JSONArray();
        if (mCategories.size() > 0){
            for (CategoryQty category: mCategories){
                if (category.getValueQty()>0){
                    JSONObject jsonCategory = new JSONObject();
                    try {
                        jsonCategory.put("ID", category.getValue().getKey());
                        jsonCategory.put("NAME", category.getTitle());
                        jsonCategory.put("EVENT_NAME", mybooking.event_name);
                        jsonCategory.put("VENUE",schedule.venue);
                        jsonCategory.put("QTY", category.getValue().getValue());
                        jsonCategory.put("PRICE", category.getPrice());
                        jsonCategory.put("TOTAL", category.getTotal());
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
                mybooking.event_time        = rvly_time_00.getValue() == null ? "" : rvly_time_00.getValue().value;
                mybooking.event_time_code   = rvly_time_00.getValue() == null ? "" : rvly_time_00.getValue().key;
                mybooking.insert(this);
                return true;
            }
            else {
                Toast.makeText(this,getResources().getString(R.string.please_choose_category),Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            Toast.makeText(this,getResources().getString(R.string.please_choose_category),Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void sendBooking(){
        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(this);

        PostManager postman = new PostManager(this);
        postman.setApiUrl("transaction/booking");
        JSONObject datasend = new JSONObject();
        JSONObject item     = new JSONObject();
        try {
            JSONArray arr = new JSONArray(mybooking.event_category);
            for (int i=0; i<arr.length(); i++){
                JSONObject data = arr.getJSONObject(i);
                String id       = data.getString("ID");
                String qty      = data.getString("QTY");
                item.put(id,qty);
            }

            datasend.put("item",item);
            datasend.put("time",mybooking.event_time_code);
            datasend.put("name",customerDB.name);
            datasend.put("email",customerDB.email);
            datasend.put("gender",customerDB.gender);
            datasend.put("phone",customerDB.phone);
            DateFormat dateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                Date date = dateFormat1.parse(customerDB.dob);
                datasend.put("dob",dateFormat2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        postman.setData(datasend);
        postman.execute("POST");
        postman.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONObject data         = obj.getJSONArray("data").getJSONObject(0);
                        JSONObject trans_detail = data.getJSONObject("transaction_detail");
                        JSONArray payments      = data.getJSONArray("payment_detail");
                        mybooking.order_id      = trans_detail.getString("order_no");
                        mybooking.expired_date  = trans_detail.getString("expired_date");
                        mybooking.insert(CategoryActivity.this);
                        createPayment(payments);

                        Intent intent = new Intent(CategoryActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        CategoryActivity.this.finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(CategoryActivity.this, "Error Booking", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void createPayment(JSONArray data){
        PaymentDB paymentDB = new PaymentDB();
        paymentDB.clearData(this);
        try {
            for (int i=0; i<data.length(); i++){
                JSONObject sc = data.getJSONObject(i);
                PaymentDB payment = new PaymentDB();
                payment.id          = sc.getString("payment_id");
                payment.name        = sc.getString("payment_name");
                payment.fee_value   = sc.getString("fee_value");
                payment.fee_info    = sc.getString("fee_info");
                payment.image_url   = sc.getString("payment_image");
                payment.description = sc.getString("payment_description");
                payment.insert(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
