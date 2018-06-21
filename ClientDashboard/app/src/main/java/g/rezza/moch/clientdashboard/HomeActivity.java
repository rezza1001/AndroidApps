package g.rezza.moch.clientdashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.holder.UserSHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;
import g.rezza.moch.clientdashboard.libs.Utils;
import g.rezza.moch.clientdashboard.view.DashboardGrafikView;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String TAG = "RZ HomeActivity";

    public static final int REQUEST_MORE_INFO       = 1;
    public static final int REQUEST_SETTING_EVENT   = 2;

    private RelativeLayout rvly_main_00;
    private ProgressDialog mProgressDlg;
    private TextView txvw_header_dsh_00;
    private TextView txvw_header_dsh_20;
    private TextView txvw_name_01;
    private RelativeLayout rvly_view_detail_00;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DashboardGrafikView dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView      = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout   = navigationView.getHeaderView(0);
        txvw_name_01        = (TextView) headerLayout.findViewById(R.id.txvw_name_01);
        rvly_main_00        = (RelativeLayout) findViewById(R.id.rvly_main_00);
        rvly_view_detail_00 = (RelativeLayout) findViewById(R.id.rvly_view_detail_00);
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.getMenu().getItem(0).setChecked(true);
        rvly_view_detail_00.setOnClickListener(this);
        create();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            create();
        }
        else if (id == 99) {
            mProgressDlg = new ProgressDialog(HomeActivity.this);
            mProgressDlg.setMessage(AlertMessage.getMessage(AlertMessage.PLEASE_WAIT,AlertMessage.ENGLISH));
            mProgressDlg.show();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
        else if (id == 100) {
            Intent i = item.getIntent();

            replaceParameter(i.getStringExtra("KEY"), "6" );
            registerViewHeader(i.getStringExtra("VALUE"));
            createDashBoardGrafik();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MORE_INFO){
            createDashBoardGrafik();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, DetailEventActivitySwipe.class);
        replaceParameter( dashboard.getEventID(), dashboard.getParamSelected());
        HomeActivity.this.startActivityForResult(intent, REQUEST_MORE_INFO);
    }

    public void create(){
        txvw_name_01.setText("Aldi Febrian");

        addEventMenu("1");
        addLogoutMenu();
    }

    private void addEventMenu(String uid) {
        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        final Menu submenu = menu.addSubMenu("Events");
        {
            Intent intent   = new Intent();
            String id      = "1";
            String name    = "SOUNDRENALINE";
            intent.putExtra("KEY",id);
            intent.putExtra("VALUE",name);
            submenu.add(0,100,1,name).setIcon(R.drawable.ic_calendar).setIntent(intent);
        }

        {
            Intent intent   = new Intent();
            String id      = "2";
            String name    = "SOUNDFEST";
            intent.putExtra("KEY",id);
            intent.putExtra("VALUE",name);
            submenu.add(0,100,2,name).setIcon(R.drawable.ic_calendar).setIntent(intent);
        }

        navView.invalidate();
        onNavigationItemSelected(submenu.getItem(0));

    }
    private void addLogoutMenu() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("My Account");
//        int groupId, int itemId, int order, CharSequence title
        submenu.add(0,99,1,"Logout").setIcon(R.drawable.if_logout_01);

        navView.invalidate();
    }

    private void registerViewHeader(String name){
        if (txvw_header_dsh_00 == null){
            txvw_header_dsh_00 = (TextView) toolbar.findViewById(R.id.txvw_header_dsh_00);
            txvw_header_dsh_20 = (TextView) toolbar.findViewById(R.id.txvw_header_dsh_20);
        }
        txvw_header_dsh_00.setText(name);
        ParameterHolder param = new ParameterHolder();
        param.getData(HomeActivity.this);
        txvw_header_dsh_20.setText(Utils.getParameterValue(param.paramter));
    }
    private void createDashBoardGrafik(){
        rvly_main_00.removeAllViews();
        dashboard = new DashboardGrafikView(HomeActivity.this, null);
        dashboard.create();
        rvly_main_00.addView(dashboard);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mProgressDlg.dismiss();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                    break;

            }
        }
    };

    private void replaceParameter(String eventID, String param){
        ParameterHolder parameter = new ParameterHolder();
        parameter.clearData(HomeActivity.this);
        parameter.event_id = eventID;
        parameter.paramter = param;
        parameter.insert(HomeActivity.this);
    }


}
