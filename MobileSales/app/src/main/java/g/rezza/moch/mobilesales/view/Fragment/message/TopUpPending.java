package g.rezza.moch.mobilesales.view.Fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.Notification.TopUpReqDtlActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.NotificationDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.TopUpReqAdapter;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.NotificationHolder;

/**
 * Created by rezza on 02/02/18.
 */

public class TopUpPending extends Fragment {

    private ListView                        lsvw_notif_00;
    private TopUpReqAdapter                 mAdapter;
    private ArrayList<NotificationHolder>   holders;
    private SwipeRefreshLayout              srly_notif_00;
    private TextView                        txvw_empty_00;
    private UserDB  userDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_fragment_reqtopup, container, false);
        holders       = new ArrayList<>();
        srly_notif_00 = (SwipeRefreshLayout) view.findViewById(R.id.srly_notif_00);
        lsvw_notif_00 = (ListView) view.findViewById(R.id.lsvw_notif_00);
        mAdapter      = new TopUpReqAdapter(getActivity(), holders);
        txvw_empty_00 = (TextView) view.findViewById(R.id.txvw_empty_00);
        lsvw_notif_00.setAdapter(mAdapter);
        txvw_empty_00.setVisibility(View.GONE);

        userDB = new UserDB();
        userDB.getMine(getActivity());
        loadData();
        registerListener();
        return view;
    }

    private void loadData(){

        srly_notif_00.setRefreshing(true);
        PostManager pos = new PostManager(getActivity());
        pos.setApiUrl("request-topup-pending");
        pos.showloading(false);
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                srly_notif_00.setRefreshing(false);
                NotificationDB notifDB = new NotificationDB();
                notifDB.clearData(getActivity(), NotificationHolder.IN_PROGRESS+"");
                if (code == ErrorCode.OK){
                    try {
                        JSONArray jaData = obj.getJSONArray("DATA");
                        for (int i=0; i<jaData.length(); i++){
                            NotificationHolder notif = new NotificationHolder();
                            JSONObject jo   = jaData.getJSONObject(i);
                            notif.id        = jo.getString("ID");
                            notif.nominal   = jo.getString("BALANCE");
                            notif.when      = jo.getJSONObject("CREATED AT").getString("date").substring(0,19);
                            notif.who       = jo.getString("COMPANY REQUEST NAME");
                            notif.account_name  = jo.getString("USER REQUEST NAME");
                            notif.account_no    = jo.getString("ACCOUNT NO USER REQUEST");
                            notif.user_id_req   = jo.getString("USER ID REQUEST");
                            notif.status        = jo.getInt("STATUS ID");
                            notif.status_desc   = jo.getString("STATUS");
                            notif.what          = "Request Balance";

                            insertToLocalDB(notif);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (code == ErrorCode.NO_DATA){
                    String error_message = "Undifined";
                    try {
                        error_message = obj.getString("ERROR MESSAGE");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(getActivity(), error_message , Toast.LENGTH_SHORT ).show();
                }
                loadLocalDb();
            }
        });

    }

    private void loadLocalDb(){
        txvw_empty_00.setVisibility(View.VISIBLE);
        holders.clear();
        NotificationDB notificationDB = new NotificationDB();
        ArrayList<NotificationDB> notifs = notificationDB.getDatas(getActivity(),
                NotificationDB.FIELD_STATUS+"="+NotificationHolder.IN_PROGRESS);
        for (NotificationDB notif: notifs){
            NotificationHolder holder = new NotificationHolder();
            holder.id            = notif.id + "";
            holder.nominal       = notif.nominal;
            holder.when          = notif.when;
            holder.who           = notif.who;
            holder.account_name  = notif.account_name_req;
            holder.user_id_req   = notif.user_id_req;
            holder.status        = notif.status;
            holder.status_desc   = notif.status_desc;
            holder.what          = "Request Balance";


            holders.add(holder);
            txvw_empty_00.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void insertToLocalDB(NotificationHolder holder){
        NotificationDB notif = new NotificationDB();
        notif.id            = Integer.parseInt(holder.id);
        notif.nominal       = holder.nominal;
        notif.when          = holder.when;
        notif.what          = holder.what;
        notif.who           = holder.who;
        notif.message       = holder.message;
        notif.account_name_req  = holder.account_name;
        notif.account_no_req  = holder.account_no;
        notif.user_id_req   = holder.user_id_req;
        notif.isRead        = 1;
        notif.status        = holder.status;
        notif.status_desc    = holder.status_desc;

        notif.insert(getActivity());
    }

    protected void registerListener(){
        mAdapter.setOnSelectedItemListener(new TopUpReqAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(NotificationHolder holder, int position) {
                Intent intent = new Intent(getActivity(), TopUpReqDtlActivity.class);
                intent.putExtra("ID",holder.id );
                startActivity(intent);
            }
        });

        srly_notif_00.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }
}