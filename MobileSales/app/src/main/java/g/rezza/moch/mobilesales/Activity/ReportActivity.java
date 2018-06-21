package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import g.rezza.moch.mobilesales.Activity.Report.ParameterActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.ReportAdapter;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.ReportList;

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";

    private TextView txvw_hdr_00;
    private ImageView imvw_param_00;
    private ListView lsvw_report_00;
    private ReportAdapter mAdapter;
    private ArrayList<ReportList> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txvw_hdr_00     = (TextView)    findViewById(R.id.txvw_hdr_00);
        imvw_param_00   = (ImageView)   findViewById(R.id.imvw_param_00);
        lsvw_report_00  = (ListView)    findViewById(R.id.lsvw_report_00);
        mAdapter        = new ReportAdapter(this, mList);
        lsvw_report_00.setAdapter(mAdapter);

        txvw_hdr_00.setText(getResources().getString(R.string.report));

        imvw_param_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, ParameterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            String start_date   = data.getStringExtra("START_DATE");
            String end_date     = data.getStringExtra("END_DATE");
            String store        = data.getStringExtra("STORE");
            String status       = data.getStringExtra("STATUS");

            requestData(start_date,end_date,store,status);
        }
    }

    private void requestData(String start, String end, String store, String status){
        {
            ReportList list = new ReportList();
            list.order_no   = "TAPP-1519894154";
            list.amount     = "100000";
            list.status_desc= "Success";
            list.status     = "4";
            list.date       = "2018-03-01 15:49:14";
            mList.add(list);
        }
        {
            ReportList list = new ReportList();
            list.order_no   = "TAPP-1519894151";
            list.amount     = "100000";
            list.status_desc= "Success";
            list.status     = "4";
            list.date       = "2018-03-01 15:19:14";
            mList.add(list);
        }
//        2018-03-01 18:06:12
//        DateFormat format   = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
//        DateFormat format2  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
//        Calendar cal_start  = Calendar.getInstance();
//        Calendar cal_end    = Calendar.getInstance();
//        String start_date   = "-";
//        String end_date     = "-";
//        try {
//            cal_start.setTime(format.parse(start));
//            cal_end.setTime(format.parse(end));
//            start_date = format2.format(cal_start.getTime());
//            end_date = format2.format(cal_end.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG,"Start Date : "+ start_date);
//        Log.d(TAG,"Until Date : "+ end_date);
//        Log.d(TAG,"Store : "     + store);
//        Log.d(TAG,"Status : "    + status);
//
//        mList.clear();
//        PostManager post =  new PostManager(this);
//        post.setApiUrl("report-filter");
//        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
//        kvs.add(new KeyValueHolder("start_date", start_date));
//        kvs.add(new KeyValueHolder("end_date", end_date));
//        kvs.add(new KeyValueHolder("store_id", store));
//        kvs.add(new KeyValueHolder("status", status));
//        post.setData(kvs);
//        post.execute("POST");
//        post.setOnReceiveListener(new PostManager.onReceiveListener() {
//            @Override
//            public void onReceive(JSONObject obj, int code) {
//                try {
//                    JSONArray data = obj.getJSONArray("DATA");
//                    for (int i=0; i<data.length(); i++){
//                        JSONObject order    = data.getJSONObject(i);
//                        String order_no     = order.getString("order_no");
//                        String status       = order.getString("status");
//                        String created_at   = order.getString("created_at");
//                        String amount       = order.getString("amount");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        mAdapter.notifyDataSetChanged();
    }
}
