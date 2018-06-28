package g.rezza.moch.chatid;

import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.chatid.fragment.ChatFragment;
import g.rezza.moch.chatid.fragment.ContactFragment;

public class HomeActivity extends AppCompatActivity {

    private TabLayout   tabLayout;
    private ViewPager   viewPager;
    private PagerAdapter    adapter;
    private CircleImageView imvw_profile_00;
    private TextView    txvw_name_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout          = (TabLayout)        findViewById(R.id.tab_layout);
        viewPager          = (ViewPager)        findViewById(R.id.pager);
        imvw_profile_00    = (CircleImageView)  findViewById(R.id.imvw_profile_00);
        txvw_name_00       = (TextView)         findViewById(R.id.txvw_name_00);

        createTab();
        synchData();
    }

    private void synchData(){
        Glide.with(this).
                load("https://scontent.fcgk12-1.fna.fbcdn.net/v/t1.0-9/32191820_1355661067868783_2252564820375109632_n.jpg?_nc_cat=0&oh=94056342ddb962794cee6e968afae8d5&oe=5BA632F7")
                .into(imvw_profile_00);
        txvw_name_00.setText("Rezza Gumilang");
    }

    private void createTab(){
        tabLayout.addTab(tabLayout.newTab().setText("Contact").setIcon(R.drawable.ic_contact_selected));
        tabLayout.addTab(tabLayout.newTab().setText("Chat").setIcon(R.drawable.ic_chat_unselected));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getText().toString().equals("Contact")){
                    tab.setIcon(R.drawable.ic_contact_selected);
                }
                else {
                    tab.setIcon(R.drawable.ic_chat_selected);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getText().toString().equals("Contact")){
                    tab.setIcon(R.drawable.ic_contact_unselected);
                }
                else {
                    tab.setIcon(R.drawable.ic_chat_unselected);
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        ContactFragment fragment1;
        ChatFragment fragment2;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    fragment1 = (ContactFragment) ContactFragment.newInstance();
                    return fragment1;
                case 1:
                    fragment2 = (ChatFragment) ChatFragment.newInstance();
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

