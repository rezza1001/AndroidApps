package g.rezza.moch.mobilesales.Activity.TopUp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextCurrency;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.view.Fragment.message.TopUpPending;
import g.rezza.moch.mobilesales.view.Fragment.message.TopUpApproved;

public class RequestBalanceActivity extends ActivityDtl{

    private EditextCurrency edtx_nominal_00;
    private EditextStandardC edtx_account_00;
    private EditextCurrency edtx_maxlimit_00;
    private EditextCurrency edtx_bill_00;
    private Button          bbtn_action_00;


    private TabLayout       tabLayout;
    private ViewPager       viewPager;
    private PagerAdapter    adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_balance);
    }

    @Override
    protected void onReceive(String body, JSONObject data, String title) {

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader("TOP UP");
        hideRightMenu(true);

        edtx_nominal_00     =(EditextCurrency)  findViewById(R.id.edtx_nominal_00);
        edtx_account_00     =(EditextStandardC) findViewById(R.id.edtx_account_00);
        edtx_maxlimit_00    =(EditextCurrency)  findViewById(R.id.edtx_maxlimit_00);
        edtx_bill_00        =(EditextCurrency)  findViewById(R.id.edtx_bill_00);
        bbtn_action_00      =(Button)           findViewById(R.id.bbtn_action_00);

        tabLayout           = (TabLayout)       findViewById(R.id.tab_layout);
        viewPager           = (ViewPager)       findViewById(R.id.pager);
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.in_progress)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.complete)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        edtx_account_00.setTitle(r.getString(R.string.account_number));
        edtx_account_00.setReadOnly(true);
        edtx_account_00.setValue(userDB.account);

        edtx_maxlimit_00.setTitle(r.getString(R.string.maximum_request));
        edtx_maxlimit_00.setReadOnly(true);

        edtx_bill_00.setTitle(r.getString(R.string.current_bill));
        edtx_bill_00.setReadOnly(true);

        edtx_nominal_00.setTitle(r.getString(R.string.nominal));


        edtx_nominal_00.setValue("0");

        checkLimitAndBill();
        createHistory();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nominal  = edtx_nominal_00.getValue();
                Double limit    = Double.parseDouble(edtx_maxlimit_00.getValue());
                Double bill     = Double.parseDouble(edtx_bill_00.getValue());
                Log.d(TAG,"Before ");
                if (nominal.isEmpty() || nominal.equals("0")){
                    edtx_nominal_00.showNotif(r.getString(R.string.field_required));
                    return;
                }
                if ((bill+Double.parseDouble(nominal)) > limit){
                    edtx_nominal_00.showNotif( r.getString(R.string.yor_maximum_limit));
                    return;
                }
                Log.d(TAG,"Send ");
                send();
            }
        });
    }

    private void checkLimitAndBill(){
        PostManager post = new PostManager(RequestBalanceActivity.this);
        post.setApiUrl("bill-check");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject data = obj.getJSONArray("DATA").getJSONObject(0);
                        Log.d(TAG,data.toString());
                        edtx_maxlimit_00.setValue(data.getString("Limit"));
                        edtx_bill_00.setValue(data.getString("Amount"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    RequestBalanceActivity.this.finish();
                }

            }
        });
    }

    private void send(){
        PostManager pos = new PostManager(RequestBalanceActivity.this);
        pos.setApiUrl("request-merchant-topup");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("balance_request", edtx_nominal_00.getValue()));
        kvs.add(new KeyValueHolder("user_request", userDB.id +""));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    showNotif();
                }
                else {
                    Toast.makeText(RequestBalanceActivity.this,  r.getString(R.string.failed_to_send_request), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showNotif(){
        new AlertDialog.Builder(this)
                .setMessage(r.getString(R.string.your_request_balance_sent))
                .setIcon(android.R.drawable.ic_dialog_info).setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        RequestBalanceActivity.this.finish();
                    }}).show();
    }


    private void createHistory(){

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
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment =  new TopUpPending();
                    return fragment;
                case 1:
                    fragment = new TopUpApproved();
                    return fragment;
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
