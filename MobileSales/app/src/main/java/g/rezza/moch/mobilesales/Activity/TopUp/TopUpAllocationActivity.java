package g.rezza.moch.mobilesales.Activity.TopUp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.Database.NotificationDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.SpinerHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class TopUpAllocationActivity extends ActivityDtl {

    private EditextStandardC    edtx_mybalance_00;
    private EditextStandardC    edtx_type_00;
    private SimpleSpinner       spsn_to_00;
    private EditextStandardC    edtx_name_00;
    private EditextStandardC    edtx_account_00;
    private EditextStandardC    edtx_nominal_00;
    private Button bbtn_action_00;
    NotificationDB notificationDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_allocation);

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.top_up_allocation));
        hideRightMenu(true);

        BalanceDB balanceDB = new BalanceDB();
        balanceDB.getData(this);

        String id = getIntent().getStringExtra("ID");
        notificationDB = new NotificationDB();
        notificationDB.getData(this,NotificationDB.FIELD_ID +"="+ id );


        edtx_mybalance_00 = (EditextStandardC)   findViewById(R.id.edtx_mybalance_00);
        edtx_type_00 = (EditextStandardC)   findViewById(R.id.edtx_type_00);
        spsn_to_00   = (SimpleSpinner)      findViewById(R.id.spsn_to_00);
        edtx_name_00 = (EditextStandardC)findViewById(R.id.edtx_name_00);
        edtx_account_00 = (EditextStandardC)findViewById(R.id.edtx_account_00);
        edtx_nominal_00 = (EditextStandardC)findViewById(R.id.edtx_nominal_00);
        bbtn_action_00 = (Button) findViewById(R.id.bbtn_action_00);

        edtx_mybalance_00.setTitle(r.getString(R.string.my_balance));
        edtx_mybalance_00.setValue("IDR. "+ Parse.toCurrnecy(balanceDB.balance));
        edtx_nominal_00.setValue(("IDR. "+ Parse.toCurrnecy(notificationDB.nominal)));
        edtx_nominal_00.setReadOnly(true);
        edtx_nominal_00.setTitle(r.getString(R.string.nominal));
        Log.d("TAGRZ", " NOMINAL "+ notificationDB.nominal);
        edtx_mybalance_00.setReadOnly(true);
        edtx_mybalance_00.setTextStyle(R.style.TextView_Black_Montserrat_Bold);


        edtx_type_00.setTitle(r.getString(R.string.to));
        edtx_type_00.setValue(r.getString(R.string.store));
        edtx_type_00.setReadOnly(true);
        spsn_to_00.setTitle(r.getString(R.string.name));
        ArrayList<SpinerHolder> mTypes1 = new ArrayList<>();
        mTypes1.add(new SpinerHolder("1","Toko Wafa 2",100));
        mTypes1.add(new SpinerHolder("2","Toko Wafa 1",100));
        mTypes1.add(new SpinerHolder("3","Toko Dian",100));
        mTypes1.add(new SpinerHolder("4","Toko Kuya",100));
        spsn_to_00.setChoosers(mTypes1);
        edtx_name_00.setTitle(r.getString(R.string.user));
        edtx_name_00.setValue(notificationDB.account_name_req);
        edtx_name_00.setReadOnly(true);
        edtx_account_00.setTitle(r.getString(R.string.account_number));
        edtx_account_00.setValue(notificationDB.account_no_req);
        edtx_account_00.setReadOnly(true);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

    }

    public void send(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("approve-balance");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("account_to",edtx_account_00.getValue()));
        kvs.add(new KeyValueHolder("amount",notificationDB.nominal));
        kvs.add(new KeyValueHolder("topup_id",notificationDB.id+""));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    BalanceDB balance = new BalanceDB();
                    balance.Syncronize(TopUpAllocationActivity.this);
                    Toast.makeText(TopUpAllocationActivity.this, r.getString(R.string.allocation_success), Toast.LENGTH_SHORT ).show();
                    TopUpAllocationActivity.this.finish();
                }
                else if (code == ErrorCode.INSUFFICIENT_BALANCE){
                    String error_message = "Undifined";
                    try {
                        error_message = obj.getString("ERROR MESSAGE");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(TopUpAllocationActivity.this,error_message, Toast.LENGTH_SHORT ).show();
                }
                else {
                    Toast.makeText(TopUpAllocationActivity.this, r.getString(R.string.failed_to_allocation), Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }

}
