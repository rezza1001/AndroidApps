package g.rezza.moch.mobilesales.Activity.Product;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.AbsListView;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.lib.Master.ActivityFrgWthHdr;
import g.rezza.moch.mobilesales.lib.pagger.PagerSlidingTabStrip;
import g.rezza.moch.mobilesales.lib.pagger.ScrollTabHolder;
import g.rezza.moch.mobilesales.lib.pagger.ScrollTabHolderFragment;
import g.rezza.moch.mobilesales.view.Fragment.product.EventsListFragment;
import g.rezza.moch.mobilesales.view.Fragment.product.FamilyListFragment;

public class TicketsActivity extends ActivityFrgWthHdr implements ScrollTabHolder, ViewPager.OnPageChangeListener{


    private PagerSlidingTabStrip    pstp_tabs_00;
    private ViewPager               mViewPager;
    private PagerAdapter            mPagerAdapter;



    private int mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        pstp_tabs_00    = (PagerSlidingTabStrip) findViewById(R.id.pstp_tabs_00);
        mViewPager      = (ViewPager) findViewById(R.id.vwpr_pager_00);
        mViewPager.setOffscreenPageLimit(4);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);

        mViewPager.setAdapter(mPagerAdapter);

        pstp_tabs_00.setViewPager(mViewPager);
        pstp_tabs_00.setOnPageChangeListener(this);
        mLastY=0;

        pstp_tabs_00.setDividerColor(getResources().getColor(R.color.color_orange_light));
        pstp_tabs_00.setIndicatorColor(getResources().getColor(R.color.color_orange_light));

    }

    @Override
    protected void onPostLayout() {
        hideBody(true);
        setTitleHeader(r.getString(R.string.ticket_events));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {

        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Events", "Family Attraction", "Movies"};
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment = null;
            if (position == 0){
                fragment  = (ScrollTabHolderFragment) EventsListFragment.newInstance(position);
            }
            else {
                fragment  = (ScrollTabHolderFragment) FamilyListFragment.newInstance(position);
            }
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }


    }

}
