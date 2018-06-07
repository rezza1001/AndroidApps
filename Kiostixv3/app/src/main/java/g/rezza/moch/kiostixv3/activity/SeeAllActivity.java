package g.rezza.moch.kiostixv3.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.SeeAllAdapter;
import g.rezza.moch.kiostixv3.component.SearchViw;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.holder.EventsList;
import io.fabric.sdk.android.Fabric;

public class SeeAllActivity extends AppCompatActivity {

    public static final String TAG = "SeeAllActivity";

    private ListView lsvw_see_all_00;
    private ArrayList<EventsList> list          = new ArrayList<>();
    private ArrayList<EventsList> list_filter   = new ArrayList<>();
    private SeeAllAdapter mAdapter ;
    private ImageView   imvw_back_00;
    private ImageView   imvw_serach_00;
    private SearchViw   serchvw_00;
    private TextView    txvw_title_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_see_all);

        lsvw_see_all_00 = (ListView) findViewById(R.id.lsvw_see_all_00);
        mAdapter        = new SeeAllAdapter(this, list_filter);
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        imvw_serach_00  = (ImageView) findViewById(R.id.imvw_serach_00);
        txvw_title_00   = (TextView)  findViewById(R.id.txvw_title_00);
        serchvw_00      = (SearchViw) findViewById(R.id.serchvw_00);

        lsvw_see_all_00.setAdapter(mAdapter);
        requestData();

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAdapter.setOnSelectedListener(new SeeAllAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(EventsList event) {
                Intent intent = new Intent(SeeAllActivity.this, EventDetailActivity.class);
                BookingDB mybooking = new BookingDB();
                mybooking.event_id  = event.id;
                mybooking.event_name= event.name;
                mybooking.event_image = event.img_url;
                mybooking.event_venue = event.venue;
                mybooking.insert(SeeAllActivity.this);
                startActivity(intent);
            }
        });


        txvw_title_00.setText(getIntent().getStringExtra("CATEGORY_DESC"));

        imvw_serach_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serchvw_00.show();
                imvw_serach_00.setVisibility(View.GONE);
            }
        });

        final View v = this.getCurrentFocus();
        serchvw_00.setOnBackListener(new SearchViw.OnBackListener() {
            @Override
            public void onBack() {
                imvw_serach_00.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) SeeAllActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (v != null){
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });
        serchvw_00.setOnSearchListenr(new SearchViw.OnSearchListenr() {
            @Override
            public void onSearch(String text) {
                filterData(text);
            }
        });
    }

    private void filterData(String filter){
        list_filter.clear();
        if (!filter.isEmpty()){
            for (EventsList event: list){
                if (event.name.toUpperCase().contains(filter.toUpperCase()) || event.venue.toUpperCase().contains(filter.toUpperCase())){
                    list_filter.add(event);
                }
            }
        }
        else {
            for (EventsList event: list){
                list_filter.add(event);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void requestData(){
        list.clear();
        EventsDB eventsDB = new EventsDB();
        ArrayList<EventsDB> events = eventsDB.getDatas(this, getIntent().getStringExtra("CATEGORY"));
        for (EventsDB event: events){
            list.add(new EventsList(event.id,event.name,event.img_url, event.venue));
        }
        filterData("");

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
