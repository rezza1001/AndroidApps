package g.rezza.moch.kiostixv3.view.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.EventDetailActivity;
import g.rezza.moch.kiostixv3.activity.SeeAllActivity;
import g.rezza.moch.kiostixv3.adapter.MyTixAdapter;
import g.rezza.moch.kiostixv3.component.ListEvents;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.holder.EventsList;
import g.rezza.moch.kiostixv3.holder.MyTixList;

/**
 * Created by rezza on 09/02/18.
 */

public class AllEventsFragment extends Fragment implements ListEvents.OnSelectedListener, ListEvents.OnSeeAllListerner, ListEvents.OnTouchScrollListener {
    private static final String TAG = "AllEventsFragment";

    private ListEvents lsvw_new_00;
    private ListEvents  lsvw_free_00;
    private ListEvents  lsvw_holiday_00;

    public static Fragment newInstance() {
        Log.d(TAG,"newInstance");
        Fragment frag   = new AllEventsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_all_events_fragment, container, false);
        lsvw_new_00     = view.findViewById(R.id.lsvw_new_00);
        lsvw_holiday_00 = view.findViewById(R.id.lsvw_holiday_00);
        lsvw_free_00    = view.findViewById(R.id.lsvw_free_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG,"onViewCreated");

        lsvw_new_00.setOnSelectedListener(this);
        lsvw_holiday_00.setOnSelectedListener(this);
        lsvw_holiday_00.setOnSeeAllListerner(this);
        lsvw_new_00.setOnSeeAllListerner(this);
        lsvw_new_00.setOnTouch(this);
        lsvw_holiday_00.setOnTouch(this);

        // Event Terbaru
        lsvw_new_00.clear();
        lsvw_new_00.setTiltle(getResources().getString(R.string.latest),App.CATEGORY_ALL);
        EventsDB eventsDB = new EventsDB();
        ArrayList<EventsDB> events = eventsDB.getNews(getActivity(), App.CATEGORY_ALL);
        for (EventsDB event: events){
            lsvw_new_00.add(event.id,event.name,event.img_url, event.venue);
        }
        lsvw_new_00.notif();

        // Event Attraction
        lsvw_holiday_00.clear();
        lsvw_holiday_00.setTiltle(getResources().getString(R.string.activity), App.CATEGORY_ATTRACTION);
        ArrayList<EventsDB> attractions = eventsDB.getNews(getActivity(), App.CATEGORY_ATTRACTION);
        for (EventsDB event: attractions){
            lsvw_holiday_00.add(event.id,event.name,event.img_url, event.venue);
        }
        lsvw_holiday_00.notif();

        // Event Free
        lsvw_free_00.clear();
        lsvw_free_00.setTiltle(getResources().getString(R.string.free_event),"3");
        lsvw_free_00.notif();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onSelected(EventsList holder) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        BookingDB mybooking = new BookingDB();
        mybooking.event_id  = holder.id;
        mybooking.event_name= holder.name;
        mybooking.event_image = holder.img_url;
        mybooking.event_venue = holder.venue;
        mybooking.insert(getActivity());
        startActivity(intent);
    }

    @Override
    public void onSeeAll(String id, String title) {
        Intent intent = new Intent(getActivity(), SeeAllActivity.class);
        intent.putExtra("CATEGORY_DESC", title);
        intent.putExtra("CATEGORY", id);
        startActivity(intent);
    }

    @Override
    public void OnTouch() {
        if (pTouch != null){
            pTouch.OnTouch();
        }
    }


    private OnTouchScrollListener pTouch;
    public void setOnTouch(OnTouchScrollListener mTouch){
        pTouch = mTouch;
    }
    public interface OnTouchScrollListener{
        public void OnTouch();
    }

}
