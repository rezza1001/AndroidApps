package g.rezza.moch.mobilesales.Activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.TabHost;

import g.rezza.moch.mobilesales.lib.Master.ActivityFrgWthHdr;
import g.rezza.moch.mobilesales.view.Fragment.Store.StoreListActivity;
import g.rezza.moch.mobilesales.view.Fragment.Store.StoreMapActivity;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;

public class StoreActivity extends ActivityFrgWthHdr {

    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
                {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(getResources().getColor(R.color.colorBlack_03));
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(getResources().getColor(R.color.color_orange_light));
            }
        });

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("List");
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_list));
        tabHost.addTab(spec, StoreListActivity.class, null);

        //Tab 2
        spec = tabHost.newTabSpec("Maps");
        spec.setIndicator("",  getResources().getDrawable(R.drawable.ic_maps));
        tabHost.addTab(spec, StoreMapActivity.class, null);

    }

    @Override
    protected void onPostLayout() {
        Log.d("StoreActivity","onPostLayout");
        setTitleHeader(r.getString(R.string.store));
        hideBody(true);
    }
}
