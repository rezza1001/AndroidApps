package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.Sales.AddSalesActivity;
import g.rezza.moch.mobilesales.Activity.Sales.SalesDetailActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.ListSalesDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.SalesAdapter;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.SalesListHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;

public class SalesActivity extends ActivityWthHdr {

    private ListView lsvw_sales_00;
    private SalesAdapter mAdapter;
    private ArrayList<SalesListHolder> mlist = new ArrayList<>();
    private SwipeRefreshLayout srly_sales_00;
    private FloatingActionButton fab_add_00;

    private final int ADD_NEW_SALES = 1;
    private final int EDIT_SALES    = 2;
    private final int REFRESH       = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
    }

    @Override
    protected void onPostLayout() {
        Log.d("TAG","onPostLayout");
        setTitleHeader(r.getString(R.string.sales));
        lsvw_sales_00 = (ListView)              findViewById(R.id.lsvw_sales_00);
        srly_sales_00 = (SwipeRefreshLayout)    findViewById(R.id.srly_sales_00);
        fab_add_00    = (FloatingActionButton)  findViewById(R.id.fab_add_00);
        mAdapter      = new SalesAdapter(this, mlist);

        lsvw_sales_00.setAdapter(mAdapter);
        onActivityResult(REFRESH,ErrorCode.OK,new Intent());
    }


    @Override
    protected void registerListener() {
        srly_sales_00.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        fab_add_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalesActivity.this, AddSalesActivity.class);
                intent.putExtra("STORE_ID", userDB.id);
                startActivityForResult(intent, ADD_NEW_SALES);
            }
        });

        mAdapter.setOnSelectedItemListener(new SalesAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(SalesListHolder holder, int position) {
                Intent intent = new Intent(SalesActivity.this, SalesDetailActivity.class);
                intent.putExtra("ID", holder.user_id);
                startActivityForResult(intent, EDIT_SALES);
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
                    requestData();
                }

                break;
            case EDIT_SALES:
                if (resultCode == ErrorCode.OK){
                    requestData();
                }
                break;
            case REFRESH:
                requestData();
                break;
        }
    }

    private void refreshContent(){
        requestData();
    }

    private void requestData(){
        srly_sales_00.setRefreshing(true);
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-sales");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("store_id", userDB.id));
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    ListSalesDB sales   = new ListSalesDB();
                    sales.clearData(SalesActivity.this);
                    try {
                        JSONArray arrData = obj.getJSONArray("DATA");
                        for (int i=0; i<arrData.length(); i++){
                            JSONObject data     = arrData.getJSONObject(i);
                            sales.user_id       = data.getInt("user_id");
                            sales.name          = data.getString("name");
                            sales.total_sales   = data.getLong("total_sales");
                            sales.balance       = data.getLong("balance");
                            sales.insert(SalesActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                srly_sales_00.setRefreshing(false);
                loadData();
            }
        });

    }

    public void loadData(){
        mlist.clear();
        ListSalesDB sales   = new ListSalesDB();
        ArrayList<ListSalesDB> list = sales.getData(this);
        for (ListSalesDB salesDB: list){
            SalesListHolder holder = new SalesListHolder();
            holder.name         = salesDB.name;
            holder.balance      = salesDB.balance;
            holder.total_sales  = salesDB.total_sales;
            holder.user_id      = salesDB.user_id;
            mlist.add(holder);
        }
        mAdapter.notifyDataSetChanged();
    }


}
