package g.rezza.moch.kiospos.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

import g.rezza.moch.kiospos.adapter.GuestAdapter;
import g.rezza.moch.kiospos.component.ListViewNoScroll;
import g.rezza.moch.kiospos.lib.Converter;
import g.rezza.moch.kiospos.lib.LongRunningGetIO;
import g.rezza.moch.kiospos.lib.LongRunningGetIO2;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.lib.Parser;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.adapter.AttendenzAdapter;
import g.rezza.moch.kiospos.component.PopupAttdz;
import g.rezza.moch.kiospos.component.TextKeyValue;
import g.rezza.moch.kiospos.holder.AttendenzHolder;
import g.rezza.moch.kiospos.holder.KeyValueHolder;

/**
 * Created by Rezza on 10/13/17.
 */

public class Summary extends MasterView implements View.OnClickListener {

    private TextView        texvw_selected_31;
    private TextKeyValue    txkv_orderdtl_40;
    private TextKeyValue    txkv_price_41;
    private TextKeyValue    txkv_qty_42;
    private Button          bbtn_next_31;
    private Button          bbtn_back_30;
    private LinearLayout    lnly_atendz_40;
    private Boolean         isValidData = false;
    private int             mQty = 0;
    private ProgressDialog  mProgress ;
    private ArrayList<GuestAdapter> list = new ArrayList<>();

    public Summary(Context context) {
        super(context);
    }

    public Summary(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_summary, this, true);

        texvw_selected_31   = (TextView)        findViewById(R.id.texvw_selected_31);
        txkv_orderdtl_40    = (TextKeyValue)    findViewById(R.id.txkv_orderdtl_40);
        txkv_price_41       = (TextKeyValue)    findViewById(R.id.txkv_price_41);
        txkv_qty_42         = (TextKeyValue)    findViewById(R.id.txkv_qty_42);
        lnly_atendz_40      = (LinearLayout)   findViewById(R.id.lnly_atendz_40);
        bbtn_next_31        = (Button)          findViewById(R.id.bbtn_next_31);
        bbtn_back_30        = (Button)          findViewById(R.id.bbtn_back_30);

        bbtn_back_30.setOnClickListener(this);
        bbtn_next_31.setOnClickListener(this);
    }

    public void create(JSONObject jo){
        mData = jo;
        disableLogo();
        list.clear();
        try {
            JSONObject data = jo.getJSONObject("Category");
            texvw_selected_31.setText(data.getString("value"));
            txkv_orderdtl_40.create("ORDER DETAILS",data.getString("value"));
            txkv_price_41.create("PRICE PER TICKETS","IDR "+ Converter.toCurrnecy(data.getString("price")));

            JSONObject data2 = jo.getJSONObject("ChooseQty");
            mQty = Integer.parseInt(data2.getString("quantity"));
            txkv_qty_42.create("QUANTITY", mQty+" Tickets");

            mProgress = new ProgressDialog(getContext());
            mProgress.setMessage("Please Wait...");
            mProgress.show();
            mHandler.sendEmptyMessageDelayed(1, 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateData(AttendenzHolder holder, int index){
        for (GuestAdapter category: list){
            if (holder.id.equals(category.getData().id)){
                GuestAdapter guestAdapter = new GuestAdapter(getContext(), null);
                guestAdapter.create(holder,index);
                list.set(index, guestAdapter);
            }
        }
        notifyData();
        checkAllData();
    }

    private void checkAllData(){
        boolean isComplete = true;
        for (GuestAdapter data: list){
           if (!data.getData().complete){
               isComplete = false;
           }
        }

        if (isComplete){
            isValidData = true;
            bbtn_next_31.setBackgroundResource(R.drawable.button_blue06_round_right);
        }
        else {
            isValidData = false;
            bbtn_next_31.setBackgroundResource(R.drawable.button_blue06t30_round_right);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bbtn_next_31)){
            if (mFinishListener != null && isValidData){

                JSONObject jo = new JSONObject();
                try {
                    JSONArray ja = new JSONArray();
                    for (GuestAdapter att: list){
                        ja.put(att.getData().pack());
                    }
                    jo.put("guest",ja);
                    mData.put("Summary", jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                senDataToServer();
            }
        }
        else {
            if (mFinishListener != null){
                mFinishListener.onBack(Summary.this, mData);
            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    for (int i=0; i<mQty; i++){
                        AttendenzHolder attdz = new AttendenzHolder();
                        attdz.id = attdz.id + i;
                        if (i == 0){
                            try {
                                JSONObject data = mData.getJSONObject("GuestInformation");
                                attdz = attdz.unpack(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        GuestAdapter ga = new GuestAdapter(getContext(), null);
                        ga.create(attdz, i);
                        list.add(ga);
                        notifyData();
                    }

                    checkAllData();
                    mProgress.dismiss();
                    break;
            }
            return false;
        }
    });

    private void notifyData(){
        lnly_atendz_40.removeAllViews();
        for (GuestAdapter guestAdapter: list){
            lnly_atendz_40.addView(guestAdapter);

            guestAdapter.setOnSelectedItemListener(new GuestAdapter.OnSelectedItemListener() {
                @Override
                public void OnItemSelected(final int index, AttendenzHolder data) {
                    PopupAttdz popup = new PopupAttdz(getContext());
                    popup.show();
                    popup.setData(data);
                    if (index == 0){
                        popup.disable();
                    }
                    popup.setOnPositifClickListener(new PopupAttdz.OnPositifClickListener() {
                        @Override
                        public void onClick(AttendenzHolder holder) {
                            updateData(holder, index);
                        }
                    });
                }
            });
        }
    }

    private void senDataToServer(){
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setMessage("Please Wait...");
        loading.show();
        LongRunningGetIO2 client = new LongRunningGetIO2(getContext());
        client.setUrl("mobile/insertthtd/");
        JSONObject th   = new JSONObject();
        JSONArray tds   = new JSONArray();
        try {
            JSONArray data = mData.getJSONObject("Summary").getJSONArray("guest");
            th.put("customer_email",    data.getJSONObject(0).getString("email"));
            th.put("customer_name",     data.getJSONObject(0).getString("name"));
            th.put("customer_city",     data.getJSONObject(0).getString("city"));
            th.put("customer_gender",   data.getJSONObject(0).getString("gender"));
            th.put("customer_birthdate",Parser.getDateServer(data.getJSONObject(0).getString("birth")));

            for (int i=0; i< data.length(); i++){
                JSONObject jo = new JSONObject();
                jo.put("name", data.getJSONObject(i).getString("name"));
                jo.put("city", data.getJSONObject(i).getString("city"));
                jo.put("gender", data.getJSONObject(i).getString("gender"));
                jo.put("birthdate", Parser.getDateServer(data.getJSONObject(i).getString("birth")));
                tds.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject dataTosend = new JSONObject();
        try {
            dataTosend.put("th", th);
            dataTosend.put("tds", tds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setData(dataTosend);
        client.execute();
        client.setOnReceiveListener(new LongRunningGetIO2.onReceiveListener() {
            @Override
            public void onReceive(Object obj) {
                loading.dismiss();
                String      data        = (String) obj;
                JSONObject  jResponse   = null;
                try {
                    jResponse           = new JSONObject(data);
                    String code         = jResponse.get("code").toString() ;
                    String description  = jResponse.get("description").toString() ;
                    if (code.equals("00")){
                        mFinishListener.onNext(Summary.this, mData);
                    }
                    else {
                        Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
