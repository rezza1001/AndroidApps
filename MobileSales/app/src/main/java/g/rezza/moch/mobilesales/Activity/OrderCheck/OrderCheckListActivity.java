package g.rezza.moch.mobilesales.Activity.OrderCheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import g.rezza.moch.mobilesales.Connection.firebase.MyReceiver;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.view.Fragment.order.OrderComplete;
import g.rezza.moch.mobilesales.view.Fragment.order.OrderInprogress;

public class OrderCheckListActivity extends AppCompatActivity {

    private static final String TAG = "OrderCheckListActivity";
    private static final int REQUEST_BARCODE = 1;

    private EditText         edtx_transcode_00;
    private RelativeLayout   rvly_check_00;
    private RelativeLayout   rvly_find_00;
    private TabLayout        tabLayout;
    private ViewPager        viewPager;
    private PagerAdapter     adapter;
    private TextView         txvw_hdr_00;
    private MyReceiver      myReceiver;
    private ImageView       imvw_back_00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check_list);
        onPostLayout();
        registerListener();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void onPostLayout() {

        txvw_hdr_00   = (TextView)              findViewById(R.id.txvw_hdr_00);
        edtx_transcode_00  = (EditText)         findViewById(R.id.edtx_transcode_00);
        rvly_check_00      = (RelativeLayout)   findViewById(R.id.rvly_check_00);
        rvly_find_00       = (RelativeLayout)   findViewById(R.id.rvly_find_00);
        tabLayout          = (TabLayout)        findViewById(R.id.tab_layout);
        viewPager          = (ViewPager)        findViewById(R.id.pager);
        imvw_back_00       = (ImageView)        findViewById(R.id.imvw_back_00);
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.in_progress)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.complete)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        txvw_hdr_00.setText(getResources().getString(R.string.order_check));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"adapter.notifyDataSetChanged(); ");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_BARCODE:
                try {
                    String code = data.getStringExtra("CODE");
                    if (!code.equals("-1")){
                        edtx_transcode_00.setText(code);
                        if (!code.isEmpty()){
                            Intent intent = new Intent(OrderCheckListActivity.this, OrderCheckActivity.class);
                            intent.putExtra("CODE",code);
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){
                }
                break;
        }
    }

    protected void registerListener(){
        rvly_check_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCheckListActivity.this, BarcodeActivity.class);
                startActivityForResult(intent, REQUEST_BARCODE);
            }
        });

        rvly_find_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edtx_transcode_00.getText().toString();
                if (!code.isEmpty()){
                    Intent intent = new Intent(OrderCheckListActivity.this, OrderCheckActivity.class);
                    intent.putExtra("CODE",code);
                    startActivity(intent);
                }
            }
        });
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        OrderInprogress fragment1;
        OrderComplete fragment2;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }



        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    fragment1 =  new OrderInprogress();
                    return fragment1;
                case 1:
                    fragment2 = new OrderComplete();
                    return fragment2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
