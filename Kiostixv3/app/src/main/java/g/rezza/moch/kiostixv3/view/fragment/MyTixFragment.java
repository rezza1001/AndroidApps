package g.rezza.moch.kiostixv3.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.EventDetailActivity;
import g.rezza.moch.kiostixv3.activity.PaymentActivity;
import g.rezza.moch.kiostixv3.activity.RegisterActivity;
import g.rezza.moch.kiostixv3.activity.SignInActivity;
import g.rezza.moch.kiostixv3.adapter.MyTixAdapter;
import g.rezza.moch.kiostixv3.component.ListEvents;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.database.TransHystoryDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.EventsList;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.holder.MyTixList;
import g.rezza.moch.kiostixv3.view.fragment.MyTix.TicketFragment;

/**
 * Created by rezza on 09/02/18.
 */

public class MyTixFragment extends Fragment {
    private static final String TAG = "MyTixFragment";

    private RelativeLayout  rvly_nonactive_00;
    private Button          bbtn_register_00;
    private Button          bbtn_login_00;

    public static Fragment newInstance(int color) {
        Fragment frag   = new MyTixFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter     adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_mytix_fragment, container, false);
        tabLayout          = (TabLayout)        view.findViewById(R.id.tab_layout);
        viewPager          = (ViewPager)        view.findViewById(R.id.pager);

        rvly_nonactive_00   = view.findViewById(R.id.rvly_nonactive_00);
        bbtn_login_00       = (Button) view.findViewById(R.id.bbtn_login_00);
        bbtn_register_00    = (Button) view.findViewById(R.id.bbtn_register_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void createTab(){
        tabLayout.addTab(tabLayout.newTab().setText("Tiket"));
        tabLayout.addTab(tabLayout.newTab().setText("Menunggu"));
        tabLayout.addTab(tabLayout.newTab().setText("Batal"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv=(TextView)LayoutInflater.from(getActivity()).inflate(R.layout.custom_title_tab,null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(getActivity());
        if (customerDB.id.isEmpty()){
            rvly_nonactive_00.bringToFront();
            rvly_nonactive_00.setVisibility(View.VISIBLE);
        }
        else {
            rvly_nonactive_00.setVisibility(View.GONE);
        }

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.putExtra("FROM","2");
                startActivity(intent);
                getActivity().finish();
            }
        });

        bbtn_register_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("FROM","2");
                startActivity(intent);
                getActivity().finish();
            }
        });

        getDataFromServer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        TicketFragment fragment1;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    fragment1 = (TicketFragment) TicketFragment.newInstance("Paid");
                    return fragment1;
                case 1:
                    fragment1 = (TicketFragment) TicketFragment.newInstance("Pending");
                    return fragment1;
                case 2:
                    fragment1 = (TicketFragment) TicketFragment.newInstance("Failed");
                    return fragment1;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    private void getDataFromServer(){
        Log.d(TAG,"getDataFromServer");
        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(getActivity());
        if (customerDB.token.isEmpty()){
            return;
        }

        PostManager post = new PostManager(getActivity());
        post.setApiUrl("transaction/history");
            ArrayList<KeyValueHolder> kvs = new ArrayList<>();
            kvs.add(new KeyValueHolder("token",customerDB.token ));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    TransHystoryDB transHystoryDB = new TransHystoryDB();
                    transHystoryDB.clearData(getActivity());
                    try {
                        JSONArray data = obj.getJSONArray("data");
                        for (int i=0; i<data.length(); i++){
                            transHystoryDB.order_no     = data.getJSONObject(i).getString("order_no");
                            transHystoryDB.status       = data.getJSONObject(i).getString("status");
                            transHystoryDB.event_name   = data.getJSONObject(i).getString("event_name");
                            transHystoryDB.payment      = data.getJSONObject(i).getString("payment_method");
                            transHystoryDB.quantity     = data.getJSONObject(i).getInt("quantity");
                            transHystoryDB.schedule     = data.getJSONObject(i).getString("schedule");
                            transHystoryDB.visit_time   = data.getJSONObject(i).getString("visit_time");
                            transHystoryDB.order_total  = data.getJSONObject(i).getString("order_total");
                            transHystoryDB.expired_date = data.getJSONObject(i).getString("expired_date");
                            transHystoryDB.venue_details = data.getJSONObject(i).getString("venue_details");
                            transHystoryDB.insert(getActivity());

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        String message = obj.getString("message");
                        Toast.makeText(getActivity(), message,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Internal Error",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                createTab();
            }
        });
    }
}
