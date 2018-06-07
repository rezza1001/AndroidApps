package g.rezza.moch.kiostixv3.view.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.EventDetailActivity;
import g.rezza.moch.kiostixv3.adapter.SeeAllAdapter;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.holder.EventsList;

/**
 * Created by rezza on 09/02/18.
 */

public class EventsFreeFragment extends Fragment {
    private static final String TAG = "EventsFreeFragment";

    private ListView lsvw_see_all_00;
    private ArrayList<EventsList> list = new ArrayList<>();
    private SeeAllAdapter mAdapter ;


    public static Fragment newInstance() {
        Fragment frag   = new EventsFreeFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_events_fragment, container, false);
        lsvw_see_all_00 = (ListView) view.findViewById(R.id.lsvw_see_all_00);
        mAdapter        = new SeeAllAdapter(getActivity(), list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsvw_see_all_00.setAdapter(mAdapter);
        mAdapter.setOnSelectedListener(new SeeAllAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(EventsList event) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                BookingDB mybooking = new BookingDB();
                mybooking.event_id  = event.id;
                mybooking.event_name= event.name;
                mybooking.event_image = event.img_url;
                mybooking.event_venue = event.venue;
                mybooking.insert(getActivity());
                startActivity(intent);
            }
        });
        requestData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    private void requestData(){
        list.clear();
        EventsDB eventsDB = new EventsDB();
        ArrayList<EventsDB> events = eventsDB.getDatas(getActivity(), App.CATEGORY_FREE);
        for (EventsDB event: events){
            list.add(new EventsList(event.id,event.name,event.img_url, event.venue));
        }
        mAdapter.notifyDataSetChanged();
    }
}
