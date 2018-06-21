package g.rezza.moch.mobilesales.Activity.Sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.SalesAdapter;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.SalesListHolder;
import g.rezza.moch.mobilesales.holder.SpinerHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class ListSalesMcActivity extends ActivityDtl {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sales_mc);
    }

    private SimpleSpinner               smpr_store_00;
    private ListView                    lsvw_sales_00;
    private SalesAdapter                mAdapter;
    private ArrayList<SalesListHolder>  mlist = new ArrayList<>();
    private SwipeRefreshLayout          srly_sales_00;
    private FloatingActionButton        fab_add_00;
    private TextView                    txvw_empty_00;

    private final int ADD_NEW_SALES = 1;
    private final int EDIT_SALES    = 2;
    private final int REFRESH       = 3;

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.sales));

        smpr_store_00 = (SimpleSpinner) findViewById(R.id.smpr_store_00);
        smpr_store_00.setTitle(r.getString(R.string.store));

        lsvw_sales_00 = (ListView)              findViewById(R.id.lsvw_sales_00);
        srly_sales_00 = (SwipeRefreshLayout)    findViewById(R.id.srly_sales_00);
        fab_add_00    = (FloatingActionButton)  findViewById(R.id.fab_add_00);
        txvw_empty_00 = (TextView)              findViewById(R.id.txvw_empty_00);
        mAdapter      = new SalesAdapter(this, mlist);

        lsvw_sales_00.setAdapter(mAdapter);
        txvw_empty_00.setVisibility(View.GONE);

        requestStore();
        registerListener();
    }

    private void requestStore(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-store");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray arr_store             = obj.getJSONArray("DATA");
                        ArrayList<SpinerHolder> stores  = new ArrayList<>();
                        for (int i=0; i<arr_store.length(); i++){
                            stores.add(new SpinerHolder(arr_store.getJSONObject(i).getInt("USER"),
                                    arr_store.getJSONObject(i).getString("COMPANY"),1));

                        }
                        smpr_store_00.setChoosers(stores);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void requestData(String storeID){
        txvw_empty_00.setVisibility(View.GONE);
        mlist.clear();
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-sales");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("store_id", storeID));
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");
        srly_sales_00.setRefreshing(true);
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                srly_sales_00.setRefreshing(false);
                if (code == ErrorCode.OK){

                    try {
                        JSONArray arrData = obj.getJSONArray("DATA");
                        for (int i=0; i<arrData.length(); i++){
                            JSONObject data     = arrData.getJSONObject(i);
                            SalesListHolder holder = new SalesListHolder();
                            holder.name         = data.getString("name");
                            holder.balance      = data.getLong("balance");
                            holder.total_sales  = data.getLong("total_sales");
                            holder.user_id      = data.getInt("user_id");
                            mlist.add(holder);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (code == ErrorCode.NO_DATA){
                    txvw_empty_00.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    protected void registerListener() {
        srly_sales_00.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(smpr_store_00.getValue().key);
            }
        });

        fab_add_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListSalesMcActivity.this, AddSalesActivity.class);
                intent.putExtra("STORE_ID", Integer.parseInt(smpr_store_00.getValue().key));
                startActivityForResult(intent, ADD_NEW_SALES);
            }
        });

        mAdapter.setOnSelectedItemListener(new SalesAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(SalesListHolder holder, int position) {
                Intent intent = new Intent(ListSalesMcActivity.this, SalesDetailActivity.class);
                intent.putExtra("ID", holder.user_id);
                startActivityForResult(intent, EDIT_SALES);
            }
        });

        smpr_store_00.setOnChangeListener(new SimpleSpinner.ChangeListener() {
            @Override
            public void after() {
                onActivityResult(REFRESH,ErrorCode.OK,new Intent());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG","onActivityResult requestCode : "+ requestCode+ " | Result "+ resultCode);
        switch (requestCode){
            case ADD_NEW_SALES:
                if (resultCode == ErrorCode.OK){
                    requestData(smpr_store_00.getValue().key);
                }

                break;
            case EDIT_SALES:
                if (resultCode == ErrorCode.OK){
                    requestData(smpr_store_00.getValue().key);
                }
                break;
            case REFRESH:
                requestData(smpr_store_00.getValue().key);
                break;
        }
    }
}
