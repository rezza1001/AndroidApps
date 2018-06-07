package g.rezza.moch.kiostixv3.activity;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.EventsDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.view.fragment.HelpFragment;
import g.rezza.moch.kiostixv3.view.fragment.HomeFragment;
import g.rezza.moch.kiostixv3.view.fragment.MyAccountFragment;
import g.rezza.moch.kiostixv3.view.fragment.MyTixFragment;
import g.rezza.moch.kiostixv3.view.fragment.home.AllEventsFragment;
import io.fabric.sdk.android.Fabric;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import g.rezza.moch.kiostixv3.R;

public class HomeActivity extends AppCompatActivity implements BottomNavigation.OnMenuItemSelectionListener {

    private BottomNavigation navigation;
    private FrameLayout container;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_home);

        navigation = (BottomNavigation) findViewById(R.id.navigation);
        container  = (FrameLayout)      findViewById(R.id.container);
        navigation.setOnMenuItemClickListener(this);

        getEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMenuItemSelect(int i, int i1, boolean b) {
        Fragment fragment = null;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (i){
            case R.id.menu_beranda:
                clearFrg();
                fragment = HomeFragment.newInstance(0);
                fragmentTransaction.replace(container.getId(), fragment,"home");
                break;
            case R.id.menu_tix:
                fragment = MyTixFragment.newInstance(0);
                fragmentTransaction.replace(container.getId(), fragment,"mytix");
                break;
            case R.id.menu_help:
                fragment = HelpFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"help");
                break;
            case R.id.menu_account:
                fragment = MyAccountFragment.newInstance();
                fragmentTransaction.replace(container.getId(), fragment,"myaccount");
        }
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }


    private void clearFrg(){
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    @Override
    public void onMenuItemReselect(int i, int i1, boolean b) {
//        onMenuItemSelect(R.id.menu_beranda, i1, false);
    }

    private void getEvents(){
        PostManager post = new PostManager(this);
        post.setApiUrl("event");
        post.setData(new JSONObject());
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                EventsDB eventsDB = new EventsDB();

                try {
                    JSONArray data = obj.getJSONArray("data");
                    if (code == ErrorCode.SUCCSESS){
                        eventsDB.clearData(HomeActivity.this);
                    }
                    int sq_no = 1;
                    for (int i=0; i<data.length(); i++){
                        JSONObject event = data.getJSONObject(i);
                        eventsDB.id         = event.getString("id");
                        eventsDB.name       = event.getString("title");
                        eventsDB.category   = event.getString("category");
                        eventsDB.venue      = event.getString("venue");
                        eventsDB.img_url    = event.getString("image");
                        eventsDB.sqno       = sq_no++;
                        eventsDB.insert(HomeActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                onMenuItemSelect(R.id.menu_beranda, 0, false);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
