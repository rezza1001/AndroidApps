package g.rezza.moch.kiostixv3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.DetailOrderView;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;

public class TransDetailActivity extends AppCompatActivity {

    private String TAG = "SummaryActivity";

    private DetailOrderView dovw_detail_00;
    private TextView        txvw_title_00;
    private TextView        txvw_datetime_00;
    private TextView        txvw_location_00;
    private TextView        txvw_payment_00;
    private TextView        txvw_status_00;
    private TextView        txvw_transid_00;
    private TextView        txvw_virtual_00;
    private LinearLayout    lnly_virtual_00;
    private ImageView       imvw_back_00;

    private CustomerDB customerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_detail);

        dovw_detail_00      = (DetailOrderView) findViewById(R.id.dovw_detail_00);
        txvw_title_00       = (TextView)        findViewById(R.id.txvw_title_00);
        txvw_datetime_00    = (TextView)        findViewById(R.id.txvw_datetime_00);
        txvw_location_00    = (TextView)        findViewById(R.id.txvw_location_00);
        txvw_payment_00     = (TextView)        findViewById(R.id.txvw_payment_00);
        txvw_status_00      = (TextView)        findViewById(R.id.txvw_status_00);
        txvw_transid_00     = (TextView)        findViewById(R.id.txvw_transid_00);
        txvw_virtual_00     = (TextView)        findViewById(R.id.txvw_virtual_00);
        lnly_virtual_00     = (LinearLayout)    findViewById(R.id.lnly_virtual_00);
        imvw_back_00        = (ImageView)       findViewById(R.id.imvw_back_00);

        lnly_virtual_00.setVisibility(View.GONE);
        initListener();
        initData();
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData(){
        String orderID = getIntent().getStringExtra("ORDER_NO");
        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(this);
        if (customerDB.token.isEmpty()){
            return;
        }

        txvw_transid_00.setText(orderID);
        PostManager post = new PostManager(this);
        post.setApiUrl("transaction/history/detail");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("token",customerDB.token));
        kvs.add(new KeyValueHolder("order_no",orderID));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONObject data = obj.getJSONArray("data").getJSONObject(0);
                        txvw_status_00.setText(data.getString("status"));
                        txvw_title_00.setText(data.getString("event_name"));
                        txvw_payment_00.setText(data.getString("payment_method"));
                        txvw_payment_00.setText(data.getString("payment_method"));
                        DateFormat f_out    = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                        DateFormat f_in     = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Calendar calendar   = Calendar.getInstance();
                        try {
                            calendar.setTime(f_in.parse(data.getString("schedule")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        txvw_datetime_00.setText(f_out.format(calendar.getTime()) +" "+data.getString("visit_time") );

                        JSONObject locationObj = data.getJSONObject("venue_details");
                        txvw_location_00.setText(locationObj.getString("name")+" : "+locationObj.getString("address") );

                        JSONArray item_details = data.getJSONArray("item_details");
                        dovw_detail_00.setFee(data.getString("fee"));
                        dovw_detail_00.setDiscount("",data.getString("discount"));

                        JSONArray jAdata = new JSONArray();
                        for (int i=0; i<item_details.length(); i++){
                            JSONObject jsonCategory = new JSONObject();
                            jsonCategory.put("ID", i);
                            jsonCategory.put("NAME", item_details.getJSONObject(i).getString("name"));
                            jsonCategory.put("EVENT_NAME", data.getString("event_name"));
                            jsonCategory.put("VENUE",locationObj.getString("name"));
                            jsonCategory.put("QTY", item_details.getJSONObject(i).getString("quantity"));
                            jsonCategory.put("PRICE", item_details.getJSONObject(i).getString("price"));
                            jsonCategory.put("TOTAL", item_details.getJSONObject(i).getString("total"));
                            jAdata.put(jsonCategory);
                        }
                        dovw_detail_00.create(jAdata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        String message = obj.getString("message");
                        Toast.makeText(TransDetailActivity.this, message,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(TransDetailActivity.this, "Internal Error",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
