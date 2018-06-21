package g.rezza.moch.mobilesales.Activity.Sales;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.SpinKeyValueView;
import g.rezza.moch.mobilesales.component.TextKeyValueView;
import g.rezza.moch.mobilesales.component.TopUpAllocationDialog;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class SalesDetailActivity extends ActivityDtl {

    private TextKeyValueView edtx_email_00;
    private TextKeyValueView edtx_account_00;
    private TextKeyValueView edtx_balance_00;
    private TextKeyValueView edtx_total_00;

    private TextKeyValueView edtx_name_01;
    private TextKeyValueView edtx_address_01;
    private TextKeyValueView edtx_phone1_01;
    private TextKeyValueView edtx_phone2_01;
    private TextKeyValueView edtx_id_01;
    private SpinKeyValueView spsn_idtype_01;
    private SpinKeyValueView spsn_gender_01;
    private TextKeyValueView smdp_dob_01;
    private ImageView imvw_topup_00;

    private Button bbtn_action_00;

    private int sales_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.sales));
        setRightImage(R.drawable.ic_delete);
        sales_id = getIntent().getIntExtra("ID", 0);

        edtx_email_00   = (TextKeyValueView) findViewById(R.id.edtx_email_00);
        edtx_account_00 = (TextKeyValueView) findViewById(R.id.edtx_account_00);
        edtx_balance_00 = (TextKeyValueView) findViewById(R.id.edtx_balance_00);
        edtx_total_00   = (TextKeyValueView) findViewById(R.id.edtx_total_00);
        edtx_name_01    = (TextKeyValueView) findViewById(R.id.edtx_name_01);
        edtx_address_01 = (TextKeyValueView) findViewById(R.id.edtx_address_01);
        edtx_phone1_01  = (TextKeyValueView) findViewById(R.id.edtx_phone1_01);
        edtx_phone2_01  = (TextKeyValueView) findViewById(R.id.edtx_phone2_01);
        edtx_id_01      = (TextKeyValueView) findViewById(R.id.edtx_id_01);
        spsn_idtype_01  = (SpinKeyValueView) findViewById(R.id.spsn_idtype_01);
        spsn_gender_01  = (SpinKeyValueView) findViewById(R.id.spsn_gender_01);
        smdp_dob_01     = (TextKeyValueView) findViewById(R.id.smdp_dob_01);
        bbtn_action_00  = (Button)           findViewById(R.id.bbtn_action_00);
        imvw_topup_00   = (ImageView) findViewById(R.id.imvw_topup_00);

        edtx_email_00.setTitle(r.getString(R.string.email_address));
        edtx_account_00.setTitle(r.getString(R.string.account_number));
        edtx_balance_00.setTitle(r.getString(R.string.balance));
        edtx_total_00.setTitle(r.getString(R.string.total_sales));

        edtx_name_01.setTitle(r.getString(R.string.name));
        edtx_address_01.setTitle(r.getString(R.string.address));
        edtx_phone1_01.setTitle(r.getString(R.string.phone_number_1));
        edtx_phone1_01.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone2_01.setTitle(r.getString(R.string.phone_number_2));
        edtx_phone2_01.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_id_01.setTitle(r.getString(R.string.id_number));
        edtx_id_01.setInputType(InputType.TYPE_CLASS_NUMBER);
        spsn_idtype_01.setTitle(r.getString(R.string.id_type));
        spsn_idtype_01.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_PERSON,this));
        spsn_gender_01.setTitle(r.getString(R.string.gender));
        spsn_gender_01.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_GENDER,this));
        smdp_dob_01.setTitle(r.getString(R.string.dob));
        smdp_dob_01.setInputType(InputType.TYPE_CLASS_DATETIME);

        accessLevel();
        requestData();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        imvw_topup_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopUpAllocationDialog topopDialog = new TopUpAllocationDialog(SalesDetailActivity.this);
                topopDialog.show();
                topopDialog.create(edtx_account_00.getValue());
                topopDialog.setOnSubmitListener(new TopUpAllocationDialog.OnSubmitListener() {
                    @Override
                    public void onSubmit() {
                        requestData();
                    }
                });
            }
        });
    }

    public void requestData(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("detail-sales");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("sales_id",sales_id));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    JSONArray arr_sales   = null;
                    try {
                        arr_sales               = obj.getJSONArray("DATA");
                        JSONObject data         = arr_sales.getJSONObject(0);
                        JSONObject userInfo     = data.getJSONObject("user_info");

                        edtx_email_00.setValue(data.getString("email"));
                        edtx_account_00.setValue(data.getString("account_no"));
                        edtx_balance_00.setValue(Parse.toCurrnecy(data.getLong("balance")));
                        edtx_total_00.setValue(Parse.toCurrnecy(data.getLong("total_sales")));

                        edtx_name_01.setValue(userInfo.getString("name"));
                        edtx_address_01.setValue(userInfo.getString("address"));
                        edtx_phone1_01.setValue(userInfo.getString("phone1"));
                        edtx_phone2_01.setValue(userInfo.getString("phone2"));
                        edtx_id_01.setValue(userInfo.getString("identity_no"));
                        spsn_idtype_01.setValue(userInfo.getString("identity_type"));
                        spsn_gender_01.setValue(userInfo.getString("gender"));
                        smdp_dob_01.setValue(Parse.getDateCustom(userInfo.getString("dob")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(SalesDetailActivity.this,
                            r.getString(R.string.failed_to_load_sales), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }

    private void send(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-sales");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("sales_id",sales_id));
        mHolders.add(new KeyValueHolder("email",edtx_email_00.getValue()));
        mHolders.add(new KeyValueHolder("name",edtx_name_01.getValue()));
        mHolders.add(new KeyValueHolder("address",edtx_address_01.getValue()));
        mHolders.add(new KeyValueHolder("phone1",edtx_phone1_01.getValue()));
        mHolders.add(new KeyValueHolder("phone2",edtx_phone2_01.getValue()));
        mHolders.add(new KeyValueHolder("gender",spsn_gender_01.getValue()));
        mHolders.add(new KeyValueHolder("identity_no",edtx_id_01.getValue()));
        mHolders.add(new KeyValueHolder("dob", Parse.getDateToServer(smdp_dob_01.getValue())));
        mHolders.add(new KeyValueHolder("identity_type",spsn_idtype_01.getValue()));
        mHolders.add(new KeyValueHolder("city",""));

        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(SalesDetailActivity.this,
                            r.getString(R.string.update_success),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    SalesDetailActivity.this.finish();
                }
                else {
                    Toast.makeText(SalesDetailActivity.this,
                            r.getString(R.string.failed_to_save),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onClickMenuRight() {
        new AlertDialog.Builder(this)
                .setTitle(r.getString(R.string.notification))
                .setMessage(r.getString(R.string.do_you_really_want_delete))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        sendDelete();
                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }

    private void sendDelete(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("nonaktif-sales");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("sales_id",sales_id+""));
        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(SalesDetailActivity.this,
                            r.getString(R.string.update_success),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    SalesDetailActivity.this.finish();
                }
                else {
                    Toast.makeText(SalesDetailActivity.this,
                            r.getString(R.string.failed_to_delete),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void accessLevel(){
        edtx_email_00.setReadOnly(true);
        edtx_account_00.setReadOnly(true);
        edtx_balance_00.setReadOnly(true);
        edtx_total_00.setReadOnly(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }
}
