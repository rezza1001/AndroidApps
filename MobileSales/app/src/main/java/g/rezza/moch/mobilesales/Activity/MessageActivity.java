package g.rezza.moch.mobilesales.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.view.Fragment.message.TopUpPending;
import g.rezza.moch.mobilesales.view.Fragment.message.TopUpApproved;

public class MessageActivity extends AppCompatActivity  {

    private TabLayout   tabLayout;
    private ViewPager   viewPager;
    private PagerAdapter adapter;
    private RelativeLayout rvly_right_10;
    private TextView       txvw_hdr_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tabLayout       = (TabLayout)       findViewById(R.id.tab_layout);
        viewPager       = (ViewPager)       findViewById(R.id.pager);
        rvly_right_10   = (RelativeLayout)  findViewById(R.id.rvly_right_10);
        txvw_hdr_00     = (TextView)        findViewById(R.id.txvw_hdr_00);

        rvly_right_10.setVisibility(View.GONE);
        txvw_hdr_00.setText(getResources().getString(R.string.notification));

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.inbox)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.outbox)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
