package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.Notification.TopUpReqDtlActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.NotificationDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.NotificationAdapter;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.NotificationHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;

public class NotificationActivity extends ActivityWthHdr {

    private ListView lsvw_notif_00;
    private NotificationAdapter mAdapter;
    private ArrayList<NotificationHolder> holders;
    private SwipeRefreshLayout srly_notif_00;
    private TextView    txvw_empty_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_req);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.notification));


        holders       = new ArrayList<>();
        srly_notif_00 = (SwipeRefreshLayout) findViewById(R.id.srly_notif_00);
        lsvw_notif_00 = (ListView) findViewById(R.id.lsvw_notif_00);
        mAdapter      = new NotificationAdapter(this, holders);
        txvw_empty_00 = (TextView) findViewById(R.id.txvw_empty_00);
        lsvw_notif_00.setAdapter(mAdapter);
        txvw_empty_00.setVisibility(View.GONE);

        loadData();
    }

    private void loadLocalDb(){
        txvw_empty_00.setVisibility(View.VISIBLE);
        holders.clear();
        NotificationDB notificationDB = new NotificationDB();
        ArrayList<NotificationDB> notifs = notificationDB.getData(this);
        for (NotificationDB notif: notifs){
            NotificationHolder holder = new NotificationHolder();
            holder.id            = notif.id + "";
            holder.nominal       = notif.nominal;
            holder.when          = notif.when;
            holder.who           = notif.who;
            holder.account_name  = notif.account_name_req;
            holder.user_id_req   = notif.user_id_req;

            if (Integer.parseInt(notif.user_id_req) ==  userDB.id){
                holder.status    = holder.OUTBOX;
                holder.what      = "Top Up";
            }
            else {
                holder.status    = holder.INBOX;
                holder.what      = "Top Up";
            }

            holders.add(holder);
            txvw_empty_00.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void loadData(){

        srly_notif_00.setRefreshing(true);
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-request-topup");
        pos.showloading(false);
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                srly_notif_00.setRefreshing(false);
                NotificationDB notifDB = new NotificationDB();
                notifDB.clearData(NotificationActivity.this);
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
                            notif.account_name = jo.getString("USER REQUEST NAME");
                            notif.account_no = jo.getString("ACCOUNT NO USER REQUEST");
                            notif.user_id_req = jo.getString("USER ID REQUEST");

                            if (notif.user_id_req.equalsIgnoreCase(userDB.id+"")){
                                notif.status = notif.OUTBOX;
                                notif.what      = "Top Up";
                            }
                            else {
                                notif.status = notif.INBOX;
                                notif.what      = "Top Up";
                            }

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
                        Toast.makeText(NotificationActivity.this, error_message , Toast.LENGTH_SHORT ).show();
                }
                loadLocalDb();
            }
        });

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

        notif.insert(this);
    }

    protected void registerListener(){
        mAdapter.setOnSelectedItemListener(new NotificationAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(NotificationHolder holder, int position) {
                Intent intent = new Intent(NotificationActivity.this, TopUpReqDtlActivity.class);
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

//    private void refreshContent(){
//
//
//        new AsyncTask<NotificationHolder, Void, NotificationHolder>() {
//
//            @Override
//            protected NotificationHolder doInBackground(NotificationHolder... params) {
//                NotificationHolder notif = new NotificationHolder();
//                try {
//                    Thread.sleep(2000); // 5 seconds
//                    notif.what      = "Top Up Request";
//                    notif.nominal   = "IDR 250.000";
//                    notif.when      = "01 Jan 2018 13:00";
//                    notif.who       = "Toko Kaka";
//                    notif.account_name = "Kaka Zay";
//                    notif.status    = notif.OUTBOX;
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return notif;
//            }
//
//            @Override
//            protected void onPostExecute(NotificationHolder result) {
//                super.onPostExecute(result);
//                mAdapter.insert(result, 0);
//                mAdapter.notifyDataSetChanged();
//                srly_notif_00.setRefreshing(false);
//
//            }
//        }.execute();
//
//    }


}
