package g.rezza.moch.grupia;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.json.JSONObject;

import g.rezza.moch.grupia.connection.firebase.MyReceiver;
import g.rezza.moch.grupia.view.ChatFragment;
import g.rezza.moch.grupia.view.MapFragment;
import g.rezza.moch.grupia.view.MemberFragment;
import g.rezza.moch.grupia.view.NotificationFragment;
import g.rezza.moch.grupia.view.OrderFragment;
import g.rezza.moch.grupia.view.ProfileFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout container;
    private LinearLayout lnly_navbar_00;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        lnly_navbar_00 = headerLayout.findViewById(R.id.lnly_navbar_00);


        container = (FrameLayout)  findViewById(R.id.container);
        lnly_navbar_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfile();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        showProfile();
    }
    private void showProfile(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportActionBar().setTitle(getResources().getString(R.string.navigation_profile));
        Fragment fragment = ProfileFragment.newInstance(0);
        fragmentTransaction.replace(container.getId(), fragment,"Profile");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

        }
        else if (id == R.id.nav_member) {
            getSupportActionBar().setTitle(getResources().getString(R.string.navigation_member));
            fragment = MemberFragment.newInstance(0);
            fragmentTransaction.replace(container.getId(), fragment,"Member");
        }
        else if (id == R.id.nav_map) {
            getSupportActionBar().setTitle(getResources().getString(R.string.navigation_map));
            fragment = MapFragment.newInstance(0);
            fragmentTransaction.replace(container.getId(), fragment,"Map");
        }
        else if (id == R.id.nav_notification){
            getSupportActionBar().setTitle(getResources().getString(R.string.navigation_notif));
            fragment = NotificationFragment.newInstance(0);
            fragmentTransaction.replace(container.getId(), fragment,"Notification");
        }
        else if (id == R.id.nav_order){
            getSupportActionBar().setTitle(getResources().getString(R.string.navigation_order));
            fragment = OrderFragment.newInstance(0);
            fragmentTransaction.replace(container.getId(), fragment,"Order");
        }
        else if (id == R.id.nav_chat){
            getSupportActionBar().setTitle(getResources().getString(R.string.navigation_chat));
            fragment = ChatFragment.newInstance(0);
            fragmentTransaction.replace(container.getId(), fragment,"Chat");
        }
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
