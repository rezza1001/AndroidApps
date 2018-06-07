package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.DetailOrderAdapter;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 28/02/18.
 */

public class DetailOrderView extends RelativeLayout {
    private static final String TAG = "DetailOrderView";

    private TextView txvw_subtotal_00;
    private TextView txvw_fee_00;
    private TextView txvw_total_00;
    private TextView txvw_discount_00;
    private TextView txvw_title_discount_00;
    private LinearLayout lnly_list_00;
    private String total = "0";
    private HashMap<String, DetailOrderAdapter> MAP_DETAIL = new HashMap<>();
    ArrayList<KeyValueHolder> kvs_data = new ArrayList<>();

    private double fee = 5000;
    private double  discount = 0;
    private boolean  readOnly = false;

    public DetailOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_detail_order, this, true);

        txvw_subtotal_00    = (TextView)   findViewById(R.id.txvw_subtotal_00);
        txvw_fee_00         = (TextView)   findViewById(R.id.txvw_fee_00);
        txvw_total_00       = (TextView)   findViewById(R.id.txvw_total_00);
        txvw_discount_00    = (TextView)   findViewById(R.id.txvw_discount_00);
        txvw_title_discount_00    = (TextView)   findViewById(R.id.txvw_title_discount_00);
        lnly_list_00        = (LinearLayout) findViewById(R.id.lnly_list_00);

    }

    private void setSubTotal(String total){
        txvw_subtotal_00.setText("IDR "+ Parse.toCurrnecy(total));
    }

    private void setTotal(String total){
        this.total = total;
        txvw_total_00.setText("IDR "+ Parse.toCurrnecy(total));
    }

    public void setDiscount(String percent, String data){
        txvw_title_discount_00.setText(getResources().getString(R.string.discount));
        if (!percent.isEmpty()){
            txvw_title_discount_00.setText(txvw_title_discount_00.getText().toString()+" ( "+ percent+" )");
        }
        discount = Double.parseDouble(data);
        txvw_discount_00.setText("- IDR "+ Parse.toCurrnecy(data));
        handler.sendEmptyMessageDelayed(1,500);
    }

    public void setFee(String total){
        fee = Double.parseDouble(total);
        txvw_fee_00.setText("IDR "+ Parse.toCurrnecy(total));
    }

    public int getSize(){
        return MAP_DETAIL.size();
    }

    public void create(String jsonData){
        lnly_list_00.removeAllViews();
        kvs_data.clear();
        try {
            JSONArray arr = new JSONArray(jsonData);

            for (int i=0; i<arr.length(); i++){
                DetailOrderAdapter adapter = new DetailOrderAdapter(getContext(), null);
                JSONObject data = arr.getJSONObject(i);
                String id           = data.getString("ID");
                String name         = data.getString("NAME");
                String event_name   = data.getString("EVENT_NAME");
                String venue        = data.getString("VENUE");
                String price        = data.getString("PRICE");
                int qty             = data.getInt("QTY");

                kvs_data.add(new KeyValueHolder(id, qty+""));

                adapter.create(id,event_name,venue,price,name,qty);
                if (readOnly){
                    adapter.setReadOnly();
                }
                lnly_list_00.addView(adapter);
                MAP_DETAIL.put(id,adapter);

                adapter.setOnCancelListener(new DetailOrderAdapter.OnCancelListener() {
                    @Override
                    public void onCancel(String id, View v) {
                        lnly_list_00.removeView(v);

                        Message message = new Message();
                        message.what = 2;
                        Bundle b = new Bundle();
                        b.putString("ID", id);
                        message.setData(b);
                        handler.sendMessage(message);
                    }
                });
            }

            setDiscount("","0");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void calCulate(){
        Log.d(TAG,"MAP_DETAIL "+ MAP_DETAIL.size());
        long sub_total  = 0;

        Iterator it = MAP_DETAIL.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            DetailOrderAdapter adapter = (DetailOrderAdapter) pair.getValue();
            sub_total = sub_total + (Long.parseLong(adapter.getPrice()) * Long.parseLong(adapter.getQty()));
        }

        setSubTotal(sub_total+"");
        setTotal(((fee + sub_total) - discount) + "");
        if (mCancel != null){
            mCancel.onCancel(sub_total);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    Log.d(TAG,"ARRAY LENGTH : "+ MAP_DETAIL.size());
                    calCulate();
                    break;
                case 2:
                    Bundle b = message.getData();
                    MAP_DETAIL.remove(b.getString("ID"));
                    handler.sendEmptyMessageDelayed(1,500);
                    break;
            }
            return false;
        }
    });

    public ArrayList<KeyValueHolder> getData(){
        return kvs_data;
    }

    public String getTotal(){
        return this.total;
    }

    public void setReadOnly(){
        readOnly = true;
    }

    private onCancelListener mCancel;
    public void setOnCancelListener (onCancelListener pListener){
        mCancel = pListener;
    }
    public interface onCancelListener{
        public void onCancel(Long subtotal);
    }
}
