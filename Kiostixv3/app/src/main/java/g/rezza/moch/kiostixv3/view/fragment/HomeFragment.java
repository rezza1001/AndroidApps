package g.rezza.moch.kiostixv3.view.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.EventDetailActivity;
import g.rezza.moch.kiostixv3.activity.SearchActivity;
import g.rezza.moch.kiostixv3.adapter.ListEventAdapter;
import g.rezza.moch.kiostixv3.component.ListEvents;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.holder.EventsList;
import g.rezza.moch.kiostixv3.lib.MyViewPager;
import g.rezza.moch.kiostixv3.lib.MyViewPagerHome;
import g.rezza.moch.kiostixv3.lib.Utils;
import g.rezza.moch.kiostixv3.view.fragment.detail_event.EventDetailFragment;
import g.rezza.moch.kiostixv3.view.fragment.detail_event.TermConditionFragment;
import g.rezza.moch.kiostixv3.view.fragment.home.AllEventsFragment;
import g.rezza.moch.kiostixv3.view.fragment.home.EventsFragment;
import g.rezza.moch.kiostixv3.view.fragment.home.EventsFreeFragment;
import g.rezza.moch.kiostixv3.view.fragment.home.EventsHolidayFragment;

/**
 * Created by rezza on 09/02/18.
 */

public class HomeFragment extends Fragment {
    private static final String ARG_COLOR = "arg_color";
    private static final String TAG = "HomeFragment";


    private View        mContent;
    private int         currentColor = 0;

    private TextView                edtx_search_00;
    private PagerSlidingTabStrip    tabs;
    private MyViewPagerHome         pager;
    private MyPagerAdapter          adapter;
    private RelativeLayout          rvly_search_00;
    private AllEventsFragment       all_event_frg;

    public static Fragment newInstance(int color) {
        Fragment frag   = new HomeFragment();
        Bundle args     = new Bundle();
        args.putInt(ARG_COLOR, color);
        frag.setArguments(args);
        return frag;
    }


    public static Fragment newInstance() {
        Fragment frag   = new HomeFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_fragment_home, container, false);
        edtx_search_00  = view.findViewById(R.id.edtx_search_00);

        tabs            = (PagerSlidingTabStrip)     view.findViewById(R.id.tabs);
        pager           = (MyViewPagerHome)          view.findViewById(R.id.pager);
        adapter         = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        rvly_search_00  = (RelativeLayout)           view.findViewById(R.id.rvly_search_00);

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setAllCaps(false);
        pager.disableSwipe();


        tabs.setTextColor(createColorStateList(
                getResources().getColor(R.color.selected_tab),
                getResources().getColor(R.color.selected_tab),
                getResources().getColor(R.color.colorPrimaryDark)));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated : "+ savedInstanceState);
    }

    private ColorStateList createColorStateList(int color_state_pressed, int color_state_selected, int color_state_default) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed}, //pressed
                        new int[]{android.R.attr.state_selected}, // enabled
                        new int[]{} //default
                },
                new int[]{
                        color_state_pressed,
                        color_state_selected,
                        color_state_default
                }
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");

        for (int i = 0; i < tabs.getTabCount(); i++) {
            //noinspection ConstantConditions

        }

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        handler.sendEmptyMessageDelayed(1, 100);

        rvly_search_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        edtx_search_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            adapter.notifyDataSetChanged();
            return false;
        }
    });

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Beranda", "  Event  ","Aktivitas & Hiburan", "Gratis"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
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
            switch (position){
                case 0:
                    all_event_frg = new AllEventsFragment();
                    all_event_frg.setOnTouch(new AllEventsFragment.OnTouchScrollListener() {
                        @Override
                        public void OnTouch() {
                            pager.requestDisallowInterceptTouchEvent(true);
                        }
                    });
                    return all_event_frg;
                case 1:
                    return EventsFragment.newInstance();
                case 2:
                    return EventsHolidayFragment.newInstance();
                case 3:
                    return EventsFreeFragment.newInstance();
                default:
                    return EventsFragment.newInstance();
            }

        }

    }

}
