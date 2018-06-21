package g.rezza.moch.mobilesales.Activity.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.Database.SchedulesDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.ItemDetailVIew;
import g.rezza.moch.mobilesales.holder.EventCategoryHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class EventDetailActivity extends ActivityDtl implements ItemDetailVIew.OnClickPanelListener{

    private ItemDetailVIew itdt_desc_00;
    private ItemDetailVIew itdt_venue_00;
    private ItemDetailVIew itdt_term_00;

    private RelativeLayout rvly_desc_00;
    private RelativeLayout rvly_venue_00;
    private RelativeLayout rvly_term_00;

    private TextView       txvw_name_00;
    private TextView       txvw_desc_00;
    private TextView       txvw_venue_00;
    private TextView       txvw_date_00;

    private Button         bbtn_action_00;
    private ImageView       imvw_event_00;
    private BookingDB      myBooking = new BookingDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        myBooking.getData(this);
        insertDummy();
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.event_detail));

        itdt_desc_00    = (ItemDetailVIew) findViewById(R.id.itdt_desc_00);
        itdt_venue_00   = (ItemDetailVIew) findViewById(R.id.itdt_venue_00);
        itdt_term_00    = (ItemDetailVIew) findViewById(R.id.itdt_term_00);

        rvly_desc_00    = (RelativeLayout) findViewById(R.id.rvly_desc_00);
        rvly_venue_00   = (RelativeLayout) findViewById(R.id.rvly_venue_00);
        rvly_term_00    = (RelativeLayout) findViewById(R.id.rvly_term_00);

        txvw_name_00    = (TextView)       findViewById(R.id.txvw_name_00);
        txvw_desc_00    = (TextView)       findViewById(R.id.txvw_desc_00);
        txvw_venue_00   = (TextView)       findViewById(R.id.txvw_venue_00);
        txvw_date_00    = (TextView)       findViewById(R.id.txvw_date_00);

        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);
        imvw_event_00   = (ImageView)       findViewById(R.id.imvw_event_00);

        itdt_venue_00.setTitle(r.getString(R.string.venue_event_date));
        itdt_desc_00.setTitle(r.getString(R.string.description));
        itdt_term_00.setTitle(r.getString(R.string.terms_condition));


        rvly_venue_00.setVisibility(View.GONE);
        rvly_desc_00.setVisibility(View.GONE);
        rvly_term_00.setVisibility(View.GONE);

        registerListener();

        String urlImage     = myBooking.event_image;
        String eventname    = myBooking.event_name;
        if (urlImage != null && !urlImage.isEmpty()){
            Glide.with(this).load(urlImage).into(imvw_event_00);
        }
        txvw_name_00.setText(eventname);
    }

    private void registerListener(){
        itdt_desc_00.setOnClickPanelListener(this);
        itdt_venue_00.setOnClickPanelListener(this);
        itdt_term_00.setOnClickPanelListener(this);
        itdt_venue_00.setOpen();
        itdt_desc_00.setOpen();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, EventCategoryActivity.class);
                startActivity(intent);
                EventDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View view, boolean open) {
        if (view == itdt_desc_00){
            openPanel(rvly_desc_00 ,open);
        }
        else if (view == itdt_venue_00){
            openPanel(rvly_venue_00 ,open);
        }
        else if (view == itdt_term_00){
            openPanel(rvly_term_00 ,open);
        }
    }

    private void openPanel(View view, boolean open){
        if (open){
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
        scvw_body_00.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scvw_body_00.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);
    }

    private void insertDummy(){
        String data = "{\"status\":200,\"message\":\"Success\",\"code\":1000,\"data\":[{\"event_name\":\"April Concert\",\"event_description\":\"<p>description<\\/p>\",\"event_term\":\"<p>description<\\/p>\",\"event_live_date\":\"2018-03-23 10:35:09\",\"event_sales_date\":\"2018-03-23 10:35:09\",\"schedule_data\":[{\"schedule_name\":\"Jakarta\",\"schedule_date\":\"2018-03-31 17:00:00\",\"venue_name\":\"GOR Jakarta Utara\",\"section_data\":[{\"section_name\":\"Early Bird\",\"ticket_price\":700000},{\"section_name\":\"Normal\",\"ticket_price\":69999}]},{\"schedule_name\":\"Jakarta Selatan\",\"schedule_date\":\"2018-05-10 17:00:00\",\"venue_name\":\"GOR Jakarta Selatan\",\"section_data\":[{\"section_name\":\"VIP\",\"ticket_price\":40000},{\"section_name\":\"Normal\",\"ticket_price\":69000}]}]}]}";
        try {
            JSONObject server_dummy = new JSONObject(data);
            JSONObject  section = server_dummy.getJSONArray("data").getJSONObject(0);
            createScheduleData(section.getJSONArray("schedule_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                schedule.insert(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
