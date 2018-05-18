package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.adapter.ProductOrderAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.fragment.ProductOrderFragment;
import g.rezza.moch.unileverapp.fragment.SalesOrderFragment;
import g.rezza.moch.unileverapp.holder.MyOrderHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.Parse;

public class ChartActivity extends AppCompatActivity {

    private ImageView imvw_back_00;
    private static final String TAG = "ChartActivity";
    private ListView                    lsvw_order_00;
    private ProductOrderAdapter         adapter;
    private ArrayList<MyOrderHolder>    list = new ArrayList<>();
    private Button                      bbtn_action_00;
    private TextView                    txvw_total_00;
    private TextView                    txvw_empty_00;
    private TextView                    txvw_counter_00;
    private RelativeLayout              rvly_bottom_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout(){
        imvw_back_00        = (ImageView) findViewById(R.id.imvw_back_00);
        lsvw_order_00       = (ListView) findViewById(R.id.lsvw_order_00);
        adapter             = new ProductOrderAdapter(this,list);
        bbtn_action_00      = (Button) findViewById(R.id.bbtn_action_00);
        txvw_total_00       = (TextView) findViewById(R.id.txvw_total_00);
        txvw_empty_00       = (TextView) findViewById(R.id.txvw_empty_00);
        txvw_counter_00     = (TextView) findViewById(R.id.txvw_counter_00);
        rvly_bottom_00      = (RelativeLayout) findViewById(R.id.rvly_bottom_00);

        lsvw_order_00.setAdapter(adapter);
        txvw_empty_00.setVisibility(View.GONE);
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        adapter.setOOnChangeQtyListener(new ProductOrderAdapter.OnChangeQtyListener() {
            @Override
            public void OnSelect(MyOrderHolder product, int position) {
                ChartDB chartDB = new ChartDB();
                chartDB.getProduct(ChartActivity.this, product.product);
                chartDB.qty = product.qty;
                chartDB.update(ChartActivity.this, " "+ ChartDB.FIELD_PRODUCT_ID+" = '"+product.product+"'");
                processTotal();
            }
        });

        adapter.setAfterDeleteListener(new ProductOrderAdapter.afterDeleteListener() {
            @Override
            public void afterChange(MyOrderHolder event, int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                processTotal();
            }
        });
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }

    private void initData(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            MyOrderHolder holder = new MyOrderHolder();
            holder.product      = chart.product_id;
            holder.description  = productDB.name;
            holder.img_url      = productDB.photo_link+""+productDB.photo;
            holder.qty          = chart.qty;
            holder.price        = productDB.selling_price;
            holder.max_qty      = 100;
            list.add(holder);

        }
        txvw_counter_00.setText("Bag ("+list.size()+")");
        adapter.notifyDataSetChanged();
        processTotal();
    }

    private void processTotal(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        if (charts.size() == 0){
            txvw_empty_00.setVisibility(View.VISIBLE);
            rvly_bottom_00.setVisibility(View.GONE);
        }
        else {
            txvw_empty_00.setVisibility(View.GONE);
        }
        long total = 0;
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            long price = chart.qty * productDB.selling_price;
            total = total + price;
        }
        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total+""));
        txvw_counter_00.setText("Bag ("+list.size()+")");
    }

    private void sendData(){
        PostManager post = new PostManager(ChartActivity.this);
        post.setApiUrl("order/save");
        JSONObject send = new JSONObject();
        try {
            OutletDB outletDB = new OutletDB();
            outletDB.getMine(ChartActivity.this);

            send.put("request_type","7");
            JSONObject data = new JSONObject();

            JSONObject order_info = new JSONObject();
            order_info.put("username", outletDB.username);
            order_info.put("outlet_id", outletDB.outlet_id);
            order_info.put("order_disc", "0");
            order_info.put("order_pay_type", "Bank Transfer");
            order_info.put("order_notes", "");
            data.put("order_info",order_info);

            JSONArray product_info = new JSONArray(getDetialOrder());
            data.put("order_detail", product_info);

            send.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(send);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {
                if (code == ErrorCode.OK){
                    OrderDB orderDB = new OrderDB();
                    orderDB.order_pay_type = "Bank Transfer";
                    orderDB.order_detail = getDetialOrder();
                    try {
                        orderDB.id          = obj.getString("order_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    orderDB.insert(ChartActivity.this);

                    ChartDB chartDB = new ChartDB();
                    chartDB.clearData(ChartActivity.this);

                    Intent intent = new Intent(ChartActivity.this, SummaryOrderActivity.class);
                    intent.putExtra("ORDERID",orderDB.id);
                    startActivityForResult(intent, 1);
                    ChartActivity.this.finish();

//                    FragmentTransaction fragmentTransaction = ChartActivity.this.getSupportFragmentManager().beginTransaction();
//                    Fragment fragment = SalesOrderFragment.newInstance(orderDB.id);
//                    fragmentTransaction.replace(ProductOrderFragment.this.getId(), fragment,"sales_order");
//                    fragmentTransaction.detach(fragment);
//                    fragmentTransaction.attach(fragment);
//                    fragmentTransaction.commit();
                    Toast.makeText(ChartActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ChartActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getDetialOrder(){
        ChartDB chartDB = new ChartDB();
        ArrayList<ChartDB> charts = chartDB.getProducts(ChartActivity.this);
        JSONArray product_info = new JSONArray();
        for (ChartDB chart: charts){
            ProductDB productDB = new ProductDB();
            productDB.getProduct(ChartActivity.this,chart.product_id );
            JSONObject product_item  = new JSONObject();
            try {
                product_item.put("product_id", productDB.id);
                product_item.put("product_qty", chart.qty);
                product_item.put("product_pricelist", productDB.pricelist);
                product_item.put("product_discount", productDB.discount);
                product_item.put("product_selling_price", productDB.selling_price);
                product_info.put(product_item);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return product_info.toString();
    }


}
