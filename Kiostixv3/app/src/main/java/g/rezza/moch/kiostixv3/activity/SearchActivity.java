package g.rezza.moch.kiostixv3.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.util.ArrayList;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.SeeAllAdapter;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.holder.EventsList;
import io.fabric.sdk.android.Fabric;

public class SearchActivity extends Activity {

    private ImageView imvw_back_00;
    private EditText edtx_search_00;
    private ListView lsvw_see_all_00;
    private ArrayList<EventsList> list          = new ArrayList<>();
    private ArrayList<EventsList> list_filter   = new ArrayList<>();
    private SeeAllAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_search);

        imvw_back_00    = (ImageView)   findViewById(R.id.imvw_back_00);
        edtx_search_00  = (EditText)    findViewById(R.id.edtx_search_00);
        lsvw_see_all_00 = (ListView)    findViewById(R.id.lsvw_see_all_00);
        mAdapter        = new SeeAllAdapter(this, list_filter);

        lsvw_see_all_00.setAdapter(mAdapter);

        edtx_search_00.requestFocus();
        imvw_back_00.setImageResource(R.drawable.ic_back_black);
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAdapter.setOnSelectedListener(new SeeAllAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(EventsList event) {
                Intent intent = new Intent(SearchActivity.this, EventDetailActivity.class);
                BookingDB mybooking = new BookingDB();
                mybooking.event_id  = event.id;
                mybooking.event_name= event.name;
                mybooking.event_image = event.img_url;
                mybooking.event_venue = event.venue;
                mybooking.insert(SearchActivity.this);
                startActivity(intent);
                SearchActivity.this.finish();
            }
        });

        edtx_search_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtx_search_00.getText().toString();
                if (text.length() > 3){
                    filterData(text);
                }
                else {
                    filterData("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        requestData();
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

        mAdapter.notifyDataSetChanged();
    }

    private void requestData(){
        list.clear();
        EventsDB eventsDB = new EventsDB();
        ArrayList<EventsDB> events = eventsDB.getDatas(this, App.CATEGORY_ALL);
        for (EventsDB event: events){
            list.add(new EventsList(event.id,event.name,event.img_url, event.venue));
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
