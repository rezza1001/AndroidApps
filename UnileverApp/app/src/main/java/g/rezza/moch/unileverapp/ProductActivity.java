package g.rezza.moch.unileverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.Static.App;
import g.rezza.moch.unileverapp.adapter.ProductGridAdapter;
import g.rezza.moch.unileverapp.adapter.ProductListAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.holder.ProductHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";

    private ImageView           imvw_back_00;
    private ListView            lsvw_product_00;
    private GridView            grvw_product_00;
    private ProductListAdapter  adapter ;
    private ProductGridAdapter  adapter_grid;
    private ArrayList<ProductHolder> list = new ArrayList<>();
    private LinearLayout        bbtn_list_00;
    private ImageView           imvw_option_00;
    private TextView            txvw_option_00;
    private TextView            txvw_title_00;
    private TextView    txvw_counter_00;
    private RelativeLayout rvly_cart_counter_00;
    private RelativeLayout rvly_cart_00;

    private int option = App.MENU_GRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initLayout();
        initListener();
        retrieveData();
        switchOption();
    }

    private void initLayout(){
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        lsvw_product_00 = (ListView) findViewById(R.id.lsvw_product_00);
        adapter         = new ProductListAdapter(this, list);
        adapter_grid    = new ProductGridAdapter(this, list);
        bbtn_list_00    = (LinearLayout) findViewById(R.id.bbtn_list_00);
        imvw_option_00  = (ImageView) findViewById(R.id.imvw_option_00);
        txvw_option_00  = (TextView) findViewById(R.id.txvw_option_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        grvw_product_00 = (GridView) findViewById(R.id.grvw_product_00);
        txvw_counter_00     = (TextView)    findViewById(R.id.txvw_counter_00);
        rvly_cart_counter_00= (RelativeLayout) findViewById(R.id.rvly_cart_counter_00);
        rvly_cart_00        = (RelativeLayout) findViewById(R.id.rvly_cart_00);

        lsvw_product_00.setAdapter(adapter);
        grvw_product_00.setAdapter(adapter_grid);
        rvly_cart_counter_00.bringToFront();
    }

    private void initListener(){
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn_list_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (option == App.MENU_LIST){
                    option = App.MENU_GRID;
                }
                else {
                    option = App.MENU_LIST;
                }
                switchOption();
            }
        });

        adapter_grid.setOnSelectedListener(new ProductGridAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
                intent.putExtra("PRODUCTID", event.product_id);
                startActivityForResult(intent, 1);
                ProductActivity.this.finish();
            }
        });

        adapter.setOnSelectedListener(new ProductListAdapter.OnSelectedListener() {
            @Override
            public void OnSelect(ProductHolder event, int position) {
                Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
                intent.putExtra("PRODUCTID", event.product_id);
                startActivityForResult(intent, 1);
                ProductActivity.this.finish();
            }
        });

        rvly_cart_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, ChartActivity.class);
                startActivity(intent);
                ProductActivity.this.finish();
            }
        });

    }

    private void switchOption(){
        if (option == App.MENU_LIST){
            grvw_product_00.setVisibility(View.GONE);
            lsvw_product_00.setVisibility(View.VISIBLE);
            txvw_option_00.setText("GRID");
            imvw_option_00.setImageResource(R.drawable.ic_grid);
        }
        else {
            grvw_product_00.setVisibility(View.VISIBLE);
            lsvw_product_00.setVisibility(View.GONE);
            txvw_option_00.setText("LIST");
            imvw_option_00.setImageResource(R.drawable.ic_list);
        }
    }

    private void retrieveData(){
        PostManager post = new PostManager(this);
        post.setApiUrl("product/inquiry");
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code, String message) {

                if (code == ErrorCode.OK){
                    try {
                        ProductDB DB = new ProductDB();
                        DB.clearData(ProductActivity.this);
                        JSONArray data = obj.getJSONArray("data");
                        for (int i=0; i<data.length(); i++){
                            JSONObject prod = data.getJSONObject(i);
                            ProductDB productDB = new ProductDB();
                            productDB.id            = prod.getString("product_id");
                            productDB.category_l1   = prod.getString("category_level_1");
                            productDB.category_l2   = prod.getString("category_level_2");
                            productDB.category_l3   = prod.getString("category_level_3");
                            productDB.brand         = prod.getString("brand_name");
                            productDB.brand_id      = prod.getString("brand_id");
                            productDB.sku           = prod.getString("product_sku");
                            productDB.name          = prod.getString("product_name");
                            productDB.pricelist     = prod.getLong("product_pricelist");
                            productDB.selling_price = prod.getLong("selling_price");
                            productDB.discount      = prod.getDouble("product_discount");
                            productDB.photo_link    = prod.getString("product_photo_link");
                            productDB.photo         = prod.getString("product_photo");
                            productDB.insert(ProductActivity.this);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initData();
            }
        });
    }

    private void initData(){
        ChartDB chartDBCounter = new ChartDB();
        ArrayList<ChartDB> carts = chartDBCounter.getProducts(this);
        txvw_counter_00.setText(""+carts.size());

        txvw_title_00.setText(getIntent().getStringExtra("BRAND"));

        ProductDB productDB = new ProductDB();
        ArrayList<ProductDB> products = productDB.getProdcutsByBrand(ProductActivity.this, getIntent().getStringExtra("BRAND_ID"));
        for(ProductDB prod: products){
            ProductHolder holder = new ProductHolder();
            holder.product_id    = prod.id;
            holder.description   = prod.name;
            holder.product       = prod.brand;
            holder.img_url       = prod.photo_link+"/"+prod.photo;
            holder.price         = prod.selling_price;

            ChartDB chartDB = new ChartDB();
            chartDB.getProduct(ProductActivity.this,holder.product_id);
            if (chartDB.product_id.isEmpty()){
                holder.inChart = 0;
            }
            else {
                holder.inChart = 1;
            }

            list.add(holder);
        }
        adapter.notifyDataSetChanged();
        adapter_grid.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
