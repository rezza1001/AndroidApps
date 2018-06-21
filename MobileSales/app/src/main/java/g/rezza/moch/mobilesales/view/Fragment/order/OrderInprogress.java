package g.rezza.moch.mobilesales.view.Fragment.order;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.App;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.OrderCheck.OrderCheckActivity;
import g.rezza.moch.mobilesales.Connection.firebase.FirebaseMessageService;
import g.rezza.moch.mobilesales.Connection.firebase.MyReceiver;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.Orderdapter;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.OrderListHolder;

/**
 * Created by rezza on 02/02/18.
 */

public class OrderInprogress extends Fragment {

    private ListView                        lsvw_notif_00;
    private Orderdapter                     mAdapter;
    private ArrayList<OrderListHolder>      holders;
    private TextView                        txvw_empty_00;
    private UserDB                          userDB;
    private AVLoadingIndicatorView          avl_loading_00;

    private MyReceiver myReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_fragment_order, container, false);
        holders         = new ArrayList<>();
        lsvw_notif_00   = (ListView) view.findViewById(R.id.lsvw_notif_00);
        mAdapter        = new Orderdapter(getActivity(), holders);
        txvw_empty_00   = (TextView) view.findViewById(R.id.txvw_empty_00);
        avl_loading_00  = (AVLoadingIndicatorView) view.findViewById(R.id.avl_loading_00);
        lsvw_notif_00.setAdapter(mAdapter);

        userDB = new UserDB();
        userDB.getMine(getActivity());
        requestData();
        registerListener();

        avl_loading_00.bringToFront();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        holders.clear();
        mAdapter.notifyDataSetChanged();
        requestData();
    }

    @Override
    public void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FirebaseMessageService.MY_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);

        myReceiver.setOnReceiveListener(new MyReceiver.OnReceiveListener() {
            @Override
            public void onReceive(String body, JSONObject data, String title) {
                Log.d("TAG", "ORDER HERE !!!!!!!"+ title);
                if (title.equalsIgnoreCase(App.FCM_TITLE_ORDER)){
                    requestData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }

    public void requestData(){
        avl_loading_00.bringToFront();
        avl_loading_00.smoothToShow();
        txvw_empty_00.bringToFront();
        txvw_empty_00.setVisibility(View.GONE);
        PostManager pos = new PostManager(getActivity());
        pos.setApiUrl("list-order-new");
        pos.showloading(false);
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                avl_loading_00.smoothToHide();
                if (code == ErrorCode.OK){
                    holders.clear();
                    try {
                        JSONArray jaData = obj.getJSONArray("DATA");
                        for (int i=0; i<jaData.length(); i++){
                            JSONObject jo   = jaData.getJSONObject(i);
                            OrderListHolder holder = new OrderListHolder();
                            holder.code = jo.getString("order_no");
                            holder.date = jo.getString("created_at");
                            holder.status = jo.getInt("status");
                            holder.status_desc = jo.getString("status_description");
                            holder.title    = "Order ("+holder.code+ ")";
                            holders.add(holder);
                        }
                        if (jaData.length() == 0){
                            txvw_empty_00.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (code == ErrorCode.NO_DATA){
                    String error_message = "Undifined";
                    txvw_empty_00.setVisibility(View.VISIBLE);
                    try {
                        error_message = obj.getString("ERROR MESSAGE");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    protected void registerListener(){
        mAdapter.setOnSelectedItemListener(new Orderdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(OrderListHolder holder, int position) {
                Intent intent = new Intent(getActivity(), OrderCheckActivity.class);
                intent.putExtra("CODE",holder.code );
                startActivity(intent);
            }
        });

    }
}