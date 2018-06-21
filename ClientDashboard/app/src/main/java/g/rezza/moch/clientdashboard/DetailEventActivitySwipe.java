package g.rezza.moch.clientdashboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.clientdashboard.adapter.ViewPagerAdapter;
import g.rezza.moch.clientdashboard.component.PopupParameter;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.libs.Utils;
import g.rezza.moch.clientdashboard.view.TransactionDetailView;
import g.rezza.moch.clientdashboard.view.TransactionGrafikView;

public class DetailEventActivitySwipe extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "DetailEvent";

    private ViewPager               mViewPager;
    private ImageView               imvw_vliter_02;
    private TextView                txvw_parameter_02;
    private RelativeLayout          rvly_indict_left_00;
    private RelativeLayout          rvly_indict_right_00;
    private boolean                 mOpenDetail;
    TransactionGrafikView tdGrafik ;
    TransactionDetailView tdTable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event_swipe);

        mViewPager          = (ViewPager)   findViewById(R.id.container);
        imvw_vliter_02      = (ImageView)   findViewById(R.id.imvw_vliter_02);
        txvw_parameter_02   = (TextView)    findViewById(R.id.txvw_parameter_02);
        rvly_indict_left_00  = (RelativeLayout) findViewById(R.id.rvly_indict_left_00);
        rvly_indict_right_00 = (RelativeLayout) findViewById(R.id.rvly_indict_right_00);

        final PopupParameter parameter = new PopupParameter(DetailEventActivitySwipe.this);
        imvw_vliter_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameter.show();
                ParameterHolder param = new ParameterHolder();
                param.getData(DetailEventActivitySwipe.this);
                Integer position = Integer.parseInt(param.paramter);
                parameter.setValueChooset(position-1);
            }
        });
        parameter.setOnPositifClickListener(new PopupParameter.OnPositifClickListener() {
            @Override
            public void onClick(int type, String value) {
                txvw_parameter_02.setText(Utils.getParameterValue(value));
                ParameterHolder parameter = new ParameterHolder();
                parameter.getData(DetailEventActivitySwipe.this);
                replaceParameter(parameter.event_id, value);
                tdGrafik.create();
            }
        });

        mHandler.sendEmptyMessageDelayed(1,500);
        rvly_indict_left_00.setOnClickListener(this);
        rvly_indict_right_00.setOnClickListener(this);

        setupViewPager(mViewPager);
        mOpenDetail = false;
        imvw_vliter_02.setVisibility(View.GONE);
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ParameterHolder param = new ParameterHolder();
        param.getData(this);
        txvw_parameter_02.setText(Utils.getParameterValue(param.paramter));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        tdGrafik = new TransactionGrafikView();
        tdTable = new TransactionDetailView();

        adapter.addFragment(tdGrafik, "Chart");
        adapter.addFragment(tdTable, "Table");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    mHandler.sendEmptyMessageDelayed(1,100);
                }
                else {
                    if (!mOpenDetail){
                        mOpenDetail = true;
                        tdTable.create();
                    }

                    mHandler.sendEmptyMessageDelayed(2,100);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    rvly_indict_left_00.setVisibility(View.GONE);
                    rvly_indict_right_00.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    rvly_indict_left_00.setVisibility(View.VISIBLE);
                    rvly_indict_right_00.setVisibility(View.GONE);
                    break;
                case 3:
                    rvly_indict_left_00.setVisibility(View.GONE);
                    rvly_indict_right_00.setVisibility(View.GONE);
                    break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        if (v == rvly_indict_left_00){
            mViewPager.setCurrentItem(0, true);
        }
        else {
            mViewPager.setCurrentItem(1, true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void replaceParameter(String eventID, String param){
        ParameterHolder parameter = new ParameterHolder();
        parameter.clearData(DetailEventActivitySwipe.this);
        parameter.event_id = eventID;
        parameter.paramter = param;
        parameter.insert(DetailEventActivitySwipe.this);
    }
}
