package g.rezza.moch.kiostixv3.view.fragment.MyTix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.FInishActivity;
import g.rezza.moch.kiostixv3.activity.TransDetailActivity;
import g.rezza.moch.kiostixv3.adapter.MyTixAdapter;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.TransHystoryDB;
import g.rezza.moch.kiostixv3.holder.MyTixList;

/**
 * Created by rezza on 23/03/18.
 */

public class TicketFragment  extends Fragment {
    private static final String TAG = "MyTixFragment";


    public static Fragment newInstance(String status) {
        Fragment frag = new TicketFragment();
        Bundle args = new Bundle();
        args.putString("STATUS", status);
        frag.setArguments(args);
        return frag;
    }

    private ListView lsvw_mytix_00;
    private MyTixAdapter mAdapter;
    private ArrayList<MyTixList> mHolders = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_mytix_mytix_fragment , container, false);
        lsvw_mytix_00 = view.findViewById(R.id.lsvw_mytix_00);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MyTixAdapter(getActivity(), mHolders);
        lsvw_mytix_00.setAdapter(mAdapter);
        Bundle bundle = getArguments();
        String status = bundle.getString("STATUS");

        requestData(status);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void requestData(String status) {
        TransHystoryDB transHystories = new TransHystoryDB();
        ArrayList<TransHystoryDB> histories = transHystories.getDatas(getActivity(), status);
        if (status.equals("Failed")){
            histories = transHystories.getDataOthers(getActivity());
        }


        for (TransHystoryDB history : histories) {
            MyTixList tix = new MyTixList();
            tix.event_name  = history.event_name;
            tix.order_no    = history.order_no;

            try {
                JSONObject venueObj = new JSONObject(history.venue_details);
                tix.event_venue = venueObj.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tix.event_price = history.order_total;
            tix.event_status = history.status;
            tix.event_status_desc = history.status;
            tix.event_date = history.schedule;
            tix.event_time = history.visit_time;
            tix.event_expired_date = history.expired_date.substring(0,10);
            tix.event_expired_time = history.expired_date.substring(11,16);
            mHolders.add(tix);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnselectedListener(new MyTixAdapter.OnSelectedListener() {
            @Override
            public void onSelected(MyTixList myTixDB) {
                Intent intent = new Intent(getActivity(),TransDetailActivity.class);
                intent.putExtra("ORDER_NO",myTixDB.order_no);
                startActivity(intent);
            }

            @Override
            public void onSeeDetail(MyTixList myTixDB) {
//                Intent intent = new Intent(getActivity(), FInishActivity.class);
//                intent.putExtra("ID", myTixDB.event_id);
//                startActivityForResult(intent, 1);
            }
        });
    }
}