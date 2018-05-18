package g.rezza.moch.unileverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import g.rezza.moch.unileverapp.component.ListOrder;
import g.rezza.moch.unileverapp.component.TextKeyValue;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.OrderPayment;

public class SummaryOrderActivity extends AppCompatActivity {
    private static final String TAG = "SummaryOrderActivity";

    private TextKeyValue txkv_order_00;
    private TextKeyValue txkv_date_order_00;
    private TextKeyValue txkv_outlet_00;
    private TextKeyValue txkv_date_order_01;
    private TextKeyValue txkv_term_00;
    private TextKeyValue txkv_limit_00;
    private TextKeyValue txkv_payment_00;
    private TextKeyValue txkv_note_00;
    private ListOrder    lsvw_order_00;
    private Button       bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_order);
        initLayout();
        initData();
        initListener();
    }
    
    private void initLayout(){
        txkv_order_00           = (TextKeyValue) findViewById(R.id.txkv_order_00);
        txkv_date_order_00      = (TextKeyValue) findViewById(R.id.txkv_date_order_00);
        txkv_outlet_00          = (TextKeyValue) findViewById(R.id.txkv_outlet_00);
        lsvw_order_00           = (ListOrder) findViewById(R.id.lsvw_order_00);
        txkv_date_order_01      = (TextKeyValue) findViewById(R.id.txkv_date_order_01);
        txkv_term_00            = (TextKeyValue) findViewById(R.id.txkv_term_00);
        txkv_limit_00           = (TextKeyValue) findViewById(R.id.txkv_limit_00);
        txkv_payment_00         = (TextKeyValue) findViewById(R.id.txkv_payment_00);
        txkv_note_00            = (TextKeyValue) findViewById(R.id.txkv_note_00);
        bbtn_action_00          = (Button)       findViewById(R.id.bbtn_action_00);

        txkv_order_00.setKey("No. Order");
        txkv_date_order_00.setKey("Tgl. Order");
        txkv_date_order_01.setKey("Tgl. Order");
        txkv_outlet_00.setKey("Outlet");
        txkv_term_00.setKey("Term of Payment");
        txkv_limit_00.setKey("Credit Limit");
        txkv_payment_00.setKey("Cara Pembayaran");
        txkv_note_00.setKey("Keterangan");

    }

    private void initData() {
        OutletDB outletDB = new OutletDB();
        outletDB.getMine(this);

        OrderDB orderDB = new OrderDB();
        String order_id = "0";
        try {
            order_id = getIntent().getStringExtra("ORDERID");
        }catch (Exception e){
            onBackPressed();
            Toast.makeText(this, "Order ID is not valid !", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "ORDER ID "+order_id);
        orderDB.getOrder(this, order_id);

        txkv_order_00.setValue(order_id);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM YYYY");
        try {
            calendar.setTime(format1.parse(orderDB.created_at));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txkv_date_order_00.setValue(format2.format(calendar.getTime()));
        txkv_date_order_01.setValue(format2.format(calendar.getTime()));
        txkv_outlet_00.setValue(outletDB.outlet_name);
        txkv_term_00.setValue("7 Hari");
        txkv_limit_00.setValue("Rp. 2.000.000");
        txkv_payment_00.setValue(orderDB.order_pay_type);
        txkv_note_00.setValue(orderDB.order_notes);
        Log.d(TAG, orderDB.order_detail);

        try {
            ArrayList<OrderPayment> orders = new ArrayList<>();
            JSONArray data_detail = new JSONArray(orderDB.order_detail);
            for (int i=0; i<data_detail.length(); i++){
                JSONObject data     = data_detail.getJSONObject(i);
                ProductDB productDB = new ProductDB();
                productDB.getProduct(this,data.getString("product_id"));

                OrderPayment order  = new OrderPayment();
                order.product       = productDB.brand;
                order.description   = productDB.name;
                order.qty           = data.getInt("product_qty");
                order.price         = data.getLong("product_selling_price");
                orders.add(order);
            }
            lsvw_order_00.create(orders);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListener(){
        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
