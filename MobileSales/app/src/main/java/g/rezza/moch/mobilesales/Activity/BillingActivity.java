package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import g.rezza.moch.mobilesales.Activity.Billing.BillingDtlActivity;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.firebase.FirebaseMessageService;
import g.rezza.moch.mobilesales.Connection.firebase.MyReceiver;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.BillingAdapter;
import g.rezza.moch.mobilesales.holder.BillingListHolder;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;

public class BillingActivity extends AppCompatActivity {

    private RelativeLayout  rvly_right_10;
    private TextView        txvw_hdr_00;
    private TextView        txvw_empty_00;
    private ListView        lsvw_notif_00;
    private SwipeRefreshLayout
                            srly_notif_00;
    private BillingAdapter  mAdapter;
    private ArrayList<BillingListHolder> holders;
    private Resources r;
    private MyReceiver myReceiver;

    private ImageView imvw_back_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        r = getResources();
        holders         = new ArrayList<>();
        imvw_back_00    = (ImageView)       findViewById(R.id.imvw_back_00);
        rvly_right_10   = (RelativeLayout)  findViewById(R.id.rvly_right_10);
        txvw_hdr_00     = (TextView)        findViewById(R.id.txvw_hdr_00);
        txvw_empty_00   = (TextView)        findViewById(R.id.txvw_empty_00);
        lsvw_notif_00   = (ListView)        findViewById(R.id.lsvw_notif_00);
        srly_notif_00   = (SwipeRefreshLayout)
                                            findViewById(R.id.srly_notif_00);
        mAdapter        = new BillingAdapter(this, holders);
        lsvw_notif_00.setAdapter(mAdapter);

        rvly_right_10.setVisibility(View.GONE);

        txvw_hdr_00.setText(r.getString(R.string.billing));
        requestData(true);
        registerListener();
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FirebaseMessageService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        myReceiver.setOnReceiveListener(new MyReceiver.OnReceiveListener() {
            @Override
            public void onReceive(String body, JSONObject data, String title) {
                requestData(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    private void registerListener(){
        srly_notif_00.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(true);
            }
        });

        mAdapter.setOnSelectedItemListener(new BillingAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(BillingListHolder holder, int position) {
                Intent intent = new Intent(BillingActivity.this, BillingDtlActivity.class);
                intent.putExtra("CODE",         holder.code);
                intent.putExtra("DATE",         holder.date);
                intent.putExtra("STATUS",       holder.status);
                intent.putExtra("STATUS_DESC",  holder.status_desc);
                intent.putExtra("DESCRIPTION",  holder.note);
                intent.putExtra("AMOUNT",       holder.amount);
                intent.putExtra("FROM",         "0");
                intent.putExtra("TO",           "0");
                startActivity(intent);
                BillingActivity.this.finish();
            }
        });
    }

    private void requestData(boolean loading){
        txvw_empty_00.setVisibility(View.GONE);
        srly_notif_00.setRefreshing(loading);
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-invoice");
        pos.showloading(false);
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                srly_notif_00.setRefreshing(false);
                if (code == ErrorCode.OK){
                    holders.clear();

                    try {
                        JSONArray objs = obj.getJSONArray("DATA");
                        for (int i=0; i<objs.length(); i++){
                            JSONObject data = objs.getJSONObject(i);
                            BillingListHolder bill      = new BillingListHolder();
                            DateFormat formatDate       = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            DateFormat newformatDate    = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss");
                            Calendar craetd             = Calendar.getInstance();
                            craetd.setTime(formatDate.parse(data.getString("created_at")));
                            bill.code   = data.getString("code");
                            bill.status = data.getInt("status");
                            bill.date   = newformatDate.format(craetd.getTime());
                            bill.status_desc = data.getString("status_desc");
                            bill.title  = "Invoice";
                            bill.note   = data.getString("description");
                            bill.amount   = data.getString("amount");
                            holders.add(bill);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {

                }

                mAdapter.notifyDataSetChanged();
                if (holders.size() == 0){
                    txvw_empty_00.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
