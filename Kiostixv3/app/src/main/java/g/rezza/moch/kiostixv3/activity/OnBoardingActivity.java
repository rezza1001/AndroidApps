package g.rezza.moch.kiostixv3.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.lib.MyViewPagerHome;
import g.rezza.moch.kiostixv3.view.fragment.slider.FirstFragment;
import g.rezza.moch.kiostixv3.view.fragment.slider.SecondFragment;
import g.rezza.moch.kiostixv3.view.fragment.slider.thirdFragment;
import io.fabric.sdk.android.Fabric;

public class OnBoardingActivity extends AppCompatActivity {

    private MyPagerAdapter          adapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_on_boarding);
        pager           = (ViewPager)    findViewById(R.id.pager);
        adapter= new MyPagerAdapter(this.getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FirstFragment.newInstance();
                case 1:
                    return SecondFragment.newInstance();
                case 2:
                    return thirdFragment.newInstance();
                default:
                    return FirstFragment.newInstance();
            }

        }

    }
}
