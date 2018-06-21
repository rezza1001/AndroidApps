package g.rezza.moch.mobilesales.view.Fragment.Store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Activity.Store.DetailStoreActivity;
import g.rezza.moch.mobilesales.Activity.Store.ViewStoreActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.ListStoreDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.StoreAdapter;
import g.rezza.moch.mobilesales.holder.CompanyInfoHolder;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;

public class StoreListActivity extends Fragment {

    public static final int REQ_ADD = 1;
    public static final int REQ_VIEW = 2;

    private ListView        lsvw_store_00;
    private StoreAdapter    mAdapter;
    private ArrayList<CompanyInfoHolder>
                            mHolders;
    private FloatingActionButton
                            fab_add_00;
    private SwipeRefreshLayout
                            srly_notif_00;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_store_list, container, false);
        registerView(view);
        registerListener();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD){
            if (resultCode == ErrorCode.OK){
//                srly_notif_00.setRefreshing(true);
                loadData(false);
            }
        }
        else if (requestCode == REQ_VIEW){
            if (resultCode == ErrorCode.OK){
                loadData(false);
            }

        }
    }

    private void registerView(View view){
        mHolders        = new ArrayList<>();
        lsvw_store_00   = (ListView) view.findViewById(R.id.lsvw_store_00);
        mAdapter        = new StoreAdapter(getActivity(), mHolders);
        fab_add_00      = (FloatingActionButton) view.findViewById(R.id.fab_add_00);
        srly_notif_00   = (SwipeRefreshLayout) view.findViewById(R.id.srly_notif_00);
        lsvw_store_00.setAdapter(mAdapter);
        loadData(false);
    }

    private void registerListener(){
        fab_add_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailStoreActivity.class);
                startActivityForResult(intent, REQ_ADD);
            }
        });

        mAdapter.setOnSelectedItemListener(new StoreAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(CompanyInfoHolder holder, int position) {
                Intent intent = new Intent(getActivity(), ViewStoreActivity.class);
                intent.putExtra("STORE_ID", holder.user_id);
                startActivityForResult(intent, REQ_VIEW);
            }
        });

        srly_notif_00.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
    }

    private void loadData(boolean refresh){
        final ListStoreDB store = new ListStoreDB();
        store.clearData(getActivity());
        mHolders.clear();
        PostManager pos = new PostManager(getActivity());
        pos.setApiUrl("list-store");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        if (refresh){
            pos.showloading(false);
        }
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray arr_store = obj.getJSONArray("DATA");
                        for (int i=0; i<arr_store.length(); i++){
                            store.user_id   = arr_store.getJSONObject(i).getInt("USER");
                            store.balance   = arr_store.getJSONObject(i).getLong("BALANCE");
                            store.name      = arr_store.getJSONObject(i).getString("COMPANY");
                            store.latitude  = arr_store.getJSONObject(i).getString("LATITUDE");
                            store.longitude = arr_store.getJSONObject(i).getString("LONGITUDE");
                            store.total_sales = arr_store.getJSONObject(i).getLong("TotalAmount");
                            store.total_trans = arr_store.getJSONObject(i).getInt("TotalTransaction");
                            store.insert(getActivity());

                            CompanyInfoHolder company = new CompanyInfoHolder();
                            company.user_id = store.user_id;
                            company.balance = store.balance;
                            company.name    = store.name;
                            company.latitude= store.latitude;
                            company.longitude= store.longitude;
                            company.total_transaction = store.total_trans;
                            company.total_sales = store.total_sales;
                            mHolders.add(company);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyDataSetChanged();
                    srly_notif_00.setRefreshing(false);
                }
            }
        });
    }


}
