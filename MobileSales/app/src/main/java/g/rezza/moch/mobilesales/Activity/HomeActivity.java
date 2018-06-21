package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.App;
import g.rezza.moch.mobilesales.DataStatic.RoleUser;
import g.rezza.moch.mobilesales.Activity.MyAccount.CompanyInfoActivity;
import g.rezza.moch.mobilesales.Activity.MyAccount.UserInfoActivity;
import g.rezza.moch.mobilesales.Activity.OrderCheck.OrderCheckListActivity;
import g.rezza.moch.mobilesales.Activity.TopUp.RequestBalanceActivity;
import g.rezza.moch.mobilesales.Connection.firebase.FirebaseMessageService;
import g.rezza.moch.mobilesales.Connection.firebase.MyReceiver;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.BalanceView;
import g.rezza.moch.mobilesales.component.MenuGridView;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.GPSTracker;
import g.rezza.moch.mobilesales.view.Dialog.FcmNotif;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private static final int MENU_PRODUCT   = 1;
    private static final int MENU_ORDER     = 2;
    private static final int MENU_REPORT    = 3;
    private static final int MENU_STORE     = 4;
    private static final int MENU_MYACCOUNT = 5;
    private static final int MENU_REQ_TOPUP = 6;
    private static final int MENU_BILLING   = 7;
    private static final int MENU_TOPUP     = 8;

    private RelativeLayout rvly_body_00;
    private RelativeLayout rvly_header_00;
    private LinearLayout   lnly_balance_00;
    private TextView       txvw_name_00;
    private TextView       txvw_owner_00;
    private TextView       txvw_balance_00;
    private MenuGridView menus;
    private UserDB user;
    private Resources r;

    private MyReceiver myReceiver;
    private BalanceView balanceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = new UserDB();
        user.getMine(this);

        r = getResources();
        onPostLayout();
        registerListener();
        checkBalance();
        registerFCMToken();

    }

    @Override
    protected void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FirebaseMessageService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        myReceiver.setOnReceiveListener(new MyReceiver.OnReceiveListener() {
            @Override
            public void onReceive(String body, JSONObject data, String title) {
                FcmNotif fcmNotif = new FcmNotif(HomeActivity.this);
                if (title.equalsIgnoreCase(App.FCM_TITLE_TOPUP)){
                    balanceView.notifRequest();
                    fcmNotif.create(title,body);
                }

            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user.getMine(this);

        CompanyDB company = new CompanyDB();
        company.getData(this, user.id+"");
        UserInfoDB userinfo = new UserInfoDB();
        userinfo.getData(this, user.id);

        if (user.role_id == RoleUser.STORE){
            txvw_name_00.setText(company.name);
        }
        else {
            txvw_name_00.setText(userinfo.name);
        }

        if (balanceView != null){
            balanceView.notifRequest();
        }

    }

    private void checkBalance(){
        balanceView = new BalanceView(this, null);
        balanceView.notifRequest();
        balanceView.setAfterRequestListener(new BalanceView.AfterRequestListener() {
            @Override
            public void after() {
                txvw_balance_00.setText(balanceView.getText());
            }
        });
        txvw_balance_00.setText(balanceView.getText());
    }

    protected void onPostLayout() {
        rvly_body_00       = (RelativeLayout)   findViewById(R.id.rvly_body_00);
        rvly_header_00     = (RelativeLayout)   findViewById(R.id.rvly_header_00);
        lnly_balance_00    = (LinearLayout)     findViewById(R.id.lnly_balance_00);
        txvw_name_00       = (TextView)         findViewById(R.id.txvw_name_00);
        txvw_owner_00      = (TextView)         findViewById(R.id.txvw_owner_00);
        txvw_balance_00    = (TextView)         findViewById(R.id.txvw_balance_00);
                     menus = new MenuGridView(this, null);


        if (user.role_id == RoleUser.MERCHANT || user.role_id == RoleUser.MERCHANT_PERSONAL){
            menus.addMenu(MENU_PRODUCT,r.getString(R.string.product),r.getDrawable(R.mipmap.ic_menu_product));
            menus.addMenu(MENU_ORDER,r.getString(R.string.order_check),r.getDrawable(R.mipmap.ic_menu_check));
            menus.addMenu(MENU_REPORT,r.getString(R.string.report),r.getDrawable(R.mipmap.ic_menu_report));
            menus.addMenu(MENU_STORE,r.getString(R.string.store),r.getDrawable(R.mipmap.ic_menu_store));
            menus.addMenu(MENU_MYACCOUNT,r.getString(R.string.my_account),r.getDrawable(R.mipmap.ic_menu_account));

            if (user.type == UserDB.TYPE_PASCABAYAR){
//                menus.addMenu(MENU_REQ_TOPUP,r.getString(R.string.top_up_request),r.getDrawable(R.mipmap.ic_topup_debt));
                menus.addMenu(MENU_BILLING,r.getString(R.string.billing),r.getDrawable(R.mipmap.ic_menu_billing));
//                menus.addMenu(-99,"",null);
            }
            else if (user.type == UserDB.TYPE_PRABAYAR){
                menus.addMenu(MENU_TOPUP,r.getString(R.string.top_up),r.getDrawable(R.mipmap.ic_menu_topup));
            }

        }
        else if (user.role_id == RoleUser.STORE){
            lnly_balance_00.setVisibility(View.GONE);
            menus.addMenu(MENU_PRODUCT,r.getString(R.string.product),   r.getDrawable(R.mipmap.ic_menu_product));
            menus.addMenu(MENU_ORDER,r.getString(R.string.order_check), r.getDrawable(R.mipmap.ic_menu_check));
            menus.addMenu(MENU_REPORT,r.getString(R.string.report),     r.getDrawable(R.mipmap.ic_menu_report));
            menus.addMenu(MENU_MYACCOUNT,r.getString(R.string.my_account),r.getDrawable(R.mipmap.ic_menu_account));
        }

        getLocation();
        rvly_body_00.addView(menus);
    }

    protected void registerListener() {
        menus.setOnSelectedListener(new MenuGridView.OnSelectedListener() {
            @Override
            public void onSelected(int menuID) {
                switch (menuID){
                    case MENU_TOPUP:
                        Intent intent = new Intent(HomeActivity.this, TopUpActivity.class);
                        startActivity(intent);
                        break;
                    case MENU_BILLING:
                        intent= new Intent(HomeActivity.this, BillingActivity.class);
                        startActivity(intent);
                        break;
                    case MENU_REQ_TOPUP:
                         intent= new Intent(HomeActivity.this, RequestBalanceActivity.class);
                        startActivity(intent);
                        break;
                    case MENU_MYACCOUNT:
                        UserInfoDB userInfo = new UserInfoDB();
                        userInfo.getData(HomeActivity.this, user.id);
                        if (user.update != 0){
                            intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                        }
                        else {
                            if (user.role_id == RoleUser.STORE){
                                intent = new Intent(HomeActivity.this, CompanyInfoActivity.class);
                            }
                            else {
                                intent = new Intent(HomeActivity.this, UserInfoActivity.class);
                            }
                        }

                        startActivity(intent);
                        break;
                    case MENU_STORE:
                        intent = new Intent(HomeActivity.this, StoreActivity.class);
                        startActivity(intent);
                        break;
                    case MENU_REPORT:
                        // REPORT
                        intent = new Intent(HomeActivity.this, ReportActivity.class);
                        startActivity(intent);
//                        Toast.makeText(HomeActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;
                    case MENU_ORDER:
                        intent = new Intent(HomeActivity.this, OrderCheckListActivity.class);
                        startActivity(intent);
                        break;
                    case MENU_PRODUCT:
                        intent = new Intent(HomeActivity.this, ProductActivity.class);
                        startActivity(intent);
//                        Toast.makeText(HomeActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void registerFCMToken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (!refreshedToken.equals(user.fcm_token)){
            Log.d(TAG,"NEW FCM TOKEN >> "+ refreshedToken);
            user.updateFcmToken(HomeActivity.this,refreshedToken);
        }
        else {
            Log.d(TAG,"FCM TOKEN >> "+ user.fcm_token);
        }

        PostManager pos = new PostManager(this);
        pos.setApiUrl("update-fcmToken");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("fcm_token",user.fcm_token));
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");

    }
    private void getLocation(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (user.role_id != RoleUser.STORE){
            if (gpsTracker.getLocation() != null){
                PostManager pos = new PostManager(this);
                pos.setApiUrl("update-longlat");
//                pos.setData();
//                pos.execute("POST");
            }
        }

    }


}
