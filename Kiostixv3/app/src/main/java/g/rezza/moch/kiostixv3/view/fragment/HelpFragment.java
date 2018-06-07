package g.rezza.moch.kiostixv3.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.view.fragment.help.ContactUsFragment;
import g.rezza.moch.kiostixv3.view.fragment.help.FaqFragment;

/**
 * Created by rezza on 09/02/18.
 */

public class HelpFragment extends Fragment {
    private static final String TAG = "HelpFragment";


    private TabLayout           tabLayout;
    private ViewPager           viewPager;
    private PagerAdapter        adapter;

    public static Fragment newInstance() {
        Fragment frag   = new HelpFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_help_fragment, container, false);
        tabLayout       = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager       = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView faq = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        faq.setText("FAQ");

        TextView contact = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        contact.setText("Hubungi Kami");

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setCustomView(faq);
        tabLayout.getTabAt(1).setCustomView(contact);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }



        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return FaqFragment.newInstance();
                default:
                    return ContactUsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
