package g.rezza.moch.mobilesales.Activity.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Activity.OrderCheck.OrderCheckActivity;
import g.rezza.moch.mobilesales.Activity.OrderCheck.OrderCheckListActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.EventSumaryAdapter;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.holder.EventCategoryHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class EventSummaryActivity extends ActivityDtl {

    private FieldTransDtl ftdl_date_00;
    private FieldTransDtl ftdl_subtotal_00;
    private FieldTransDtl ftdl_fee_00;
    private TextView      txvw_name_00;
    private TextView      txvw_amount_00;
    private LinearLayout    lnly_detail_00;
    private Button          bbtn_action_00;
    private BookingDB       myBooking = new BookingDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_summary);
        myBooking.getData(this);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(getResources().getString(R.string.header_order_summary));
        ftdl_date_00        = (FieldTransDtl)   findViewById(R.id.ftdl_date_00);
        ftdl_fee_00         = (FieldTransDtl)   findViewById(R.id.ftdl_fee_00);
        ftdl_subtotal_00    = (FieldTransDtl)   findViewById(R.id.ftdl_subtotal_00);
        txvw_amount_00      = (TextView)        findViewById(R.id.txvw_amount_00);
        txvw_name_00        = (TextView)        findViewById(R.id.txvw_name_00);
        lnly_detail_00      = (LinearLayout)    findViewById(R.id.lnly_detail_00) ;
        bbtn_action_00      = (Button)              findViewById(R.id.bbtn_action_00);

        ftdl_date_00.setTitle(r.getString(R.string.selected_date));
        ftdl_fee_00.setTitle(r.getString(R.string.fee));
        ftdl_subtotal_00.setTitle(r.getString(R.string.sub_total));

        bulidData();
        registerListener();
    }

    private void booking(){
        PostManager posman = new PostManager(this);
        posman.setApiUrl("order");
        posman.setData(buildDdataToServer());
        posman.execute("POST");
        posman.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){

                    myBooking.clearData(EventSummaryActivity.this);
                    Intent intent = new Intent(EventSummaryActivity.this, OrderCheckActivity.class);
                    try {
                        intent.putExtra("CODE",obj.getString("ORDER NO"));
                    } catch (JSONException e) {
                        intent.putExtra("CODE","0");
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    EventSummaryActivity.this.finish();
                }
                else {

                }
            }
        });
    }

    private void bulidData(){
        txvw_name_00.setText(myBooking.event_name);

        float subtotal  =0 ;
        lnly_detail_00.removeAllViews();
        ArrayList<EventCategoryHolder> mList = new ArrayList<>();
        try {
            JSONArray jACategories = new JSONArray(myBooking.event_category);
            for (int i=0; i<jACategories.length(); i++){
                JSONObject data = jACategories.getJSONObject(i);
                EventCategoryHolder category = new EventCategoryHolder();
                category.name                = data.getString("NAME");
                category.quantity            = data.getInt("QTY");
                category.price               = data.getLong("PRICE");
                mList.add(category);
            }
            Log.d(TAG,"jACategories "+ jACategories.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (EventCategoryHolder holder : mList){
            EventSumaryAdapter adapter = new EventSumaryAdapter(this, null);
            adapter.create(holder);
            lnly_detail_00.addView(adapter);
            float total         = holder.price * holder.quantity;
            subtotal            = subtotal+ total;
        }
        ftdl_date_00.setValue("22 Januari 2018");
        ftdl_subtotal_00.setValue("IDR "+ Parse.toCurrnecy(subtotal));
        ftdl_fee_00.setValue("IDR 0");
        txvw_amount_00.setText("IDR "+ Parse.toCurrnecy(subtotal));

    }

    private void registerListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booking();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventSummaryActivity.this, EventGuestActivity.class);
        startActivity(intent);
        EventSummaryActivity.this.finish();
    }

    private JSONObject buildDdataToServer(){
        JSONObject dataSend = new JSONObject();
        try {
            JSONObject guest = new JSONObject(myBooking.event_guest);
            JSONArray jSections = new JSONArray();
            dataSend.put("user_id",""+ userDB.id);
            dataSend.put("token",""+ userDB.token);
            dataSend.put("created_by",userDB.id);
            dataSend.put("product",myBooking.event_id);
            dataSend.put("email",  guest.getString("EMAIL"));
            dataSend.put("name",     guest.getString("NAME"));
            dataSend.put("phone", guest.getString("PHONE"));
            dataSend.put("city", guest.getString("CITY"));
            dataSend.put("dob", Parse.getDateToServer( guest.getString("DOB")));

            try {
                JSONArray jACategories = new JSONArray(myBooking.event_category);
                for (int i=0; i<jACategories.length(); i++){
                    JSONObject data = jACategories.getJSONObject(i);

                    JSONObject jo = new JSONObject();
                    jo.put("qty",     data.getInt("QTY"));
                    jo.put("amount",  data.getLong("PRICE"));
                    jo.put("section", data.getString("ID"));
                    jSections.put(jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataSend.put("data_detail",jSections);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  dataSend;
    }
}
