package g.rezza.moch.mobilesales.Activity.Store;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class ViewStoreActivity extends ActivityDtl {

    private static final String TAG = "ViewStoreActivity";

    private TextKeyValueView edtx_email_00;
    private TextKeyValueView edtx_mcid_00;
    private TextKeyValueView edtx_name_00;
    private TextKeyValueView edtx_address_00;
    private TextKeyValueView edtx_phone1_00;
    private TextKeyValueView edtx_phone2_00;
    private TextKeyValueView edtx_id_00;
    private SpinKeyValueView    spsn_idtype_00;

    private Button bbtn_action_00;

    private int store_id = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.store));
        setRightImage(R.mipmap.ic_delete);

        store_id = getIntent().getIntExtra("STORE_ID", 0);
        Log.d(TAG, "STORE ID >> "+ store_id);


        edtx_email_00    = (TextKeyValueView) findViewById(R.id.edtx_email_00);
        edtx_mcid_00    = (TextKeyValueView) findViewById(R.id.edtx_mcid_00);
        edtx_name_00    = (TextKeyValueView) findViewById(R.id.edtx_name_00);
        edtx_address_00 = (TextKeyValueView) findViewById(R.id.edtx_address_00);
        edtx_phone1_00  = (TextKeyValueView) findViewById(R.id.edtx_phone1_00);
        edtx_phone2_00  = (TextKeyValueView) findViewById(R.id.edtx_phone2_00);
        edtx_id_00      = (TextKeyValueView) findViewById(R.id.edtx_id_00);
        spsn_idtype_00  = (SpinKeyValueView)    findViewById(R.id.spsn_idtype_00);

        bbtn_action_00  = (Button)  findViewById(R.id.bbtn_action_00);

        edtx_email_00.setTitle(r.getString(R.string.email_address));
        edtx_email_00.setReadOnly(true);


        edtx_mcid_00.setTitle(r.getString(R.string.merchant_code));
        edtx_mcid_00.setReadOnly(true);
        edtx_mcid_00.setVisibility(View.GONE);

        edtx_name_00.setTitle(r.getString(R.string.name));
        edtx_address_00.setTitle(r.getString(R.string.address));
        edtx_phone1_00.setTitle(r.getString(R.string.phone_number_1));
        edtx_phone1_00.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone2_00.setTitle(r.getString(R.string.phone_number_2));
        edtx_phone2_00.setInputType(InputType.TYPE_CLASS_PHONE);
        spsn_idtype_00.setTitle(r.getString(R.string.id_type));
        spsn_idtype_00.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_COMPANY,this));
        edtx_id_00.setTitle(r.getString(R.string.id_number));
        edtx_id_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        requestData();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

    }



    private void requestData(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("detail-store");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("user_id_store", store_id+""));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray arr_store   = obj.getJSONArray("DATA");
                        JSONObject data       = arr_store.getJSONObject(0);
                        JSONObject user_store = data.getJSONObject("DETAIL STORE");

                        edtx_email_00.setValue(data.getString("EMAIL USER"));
                        edtx_name_00.setValue(user_store.getString("name"));
                        edtx_address_00.setValue(user_store.getString("address"));
                        edtx_phone1_00.setValue(user_store.getString("phone1"));
                        edtx_phone2_00.setValue(user_store.getString("phone2"));
                        spsn_idtype_00.setValue(user_store.getString("identity_type"));
                        edtx_id_00.setValue(user_store.getString("identity_no"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ViewStoreActivity.this,
                            r.getString(R.string.failed_to_load_store), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }

    private void send(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-store");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("email",edtx_email_00.getValue()));

        mHolders.add(new KeyValueHolder("name",edtx_name_00.getValue()));
        mHolders.add(new KeyValueHolder("address",edtx_address_00.getValue()));
        mHolders.add(new KeyValueHolder("phone1",edtx_phone1_00.getValue()));
        mHolders.add(new KeyValueHolder("phone2",edtx_phone2_00.getValue()));
        mHolders.add(new KeyValueHolder("identity_no",edtx_id_00.getValue()));
        mHolders.add(new KeyValueHolder("identity_type",spsn_idtype_00.getValue()));
        mHolders.add(new KeyValueHolder("city",""));
        mHolders.add(new KeyValueHolder("user_id_store",store_id+""));

        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(ViewStoreActivity.this,
                            r.getString(R.string.update_success),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    ViewStoreActivity.this.finish();
                }
                else {
                    Toast.makeText(ViewStoreActivity.this,
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
        pos.setApiUrl("nonaktif-store");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("user_id_store",store_id+""));
        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(ViewStoreActivity.this,
                            r.getString(R.string.update_success),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    ViewStoreActivity.this.finish();
                }
                else {
                    Toast.makeText(ViewStoreActivity.this,
                            r.getString(R.string.failed_to_delete),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }
}
