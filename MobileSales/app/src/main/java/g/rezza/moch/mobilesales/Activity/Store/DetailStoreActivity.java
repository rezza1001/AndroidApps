package g.rezza.moch.mobilesales.Activity.Store;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class DetailStoreActivity extends ActivityDtl {

    private EditextStandardC edtx_email_00;
    private EditextStandardC edtx_password_00;
    private EditextStandardC edtx_password_01;
    private EditextStandardC edtx_mcid_00;
    private EditextStandardC edtx_name_00;
    private EditextStandardC edtx_address_00;
    private EditextStandardC edtx_phone1_00;
    private EditextStandardC edtx_phone2_00;
    private EditextStandardC edtx_id_00;
    private SimpleSpinner    spsn_idtype_00;

    private Button bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.new_store));
        hideRightMenu(true);

        edtx_email_00 = (EditextStandardC) findViewById(R.id.edtx_email_00);
        edtx_password_00 = (EditextStandardC) findViewById(R.id.edtx_password_00);
        edtx_password_01 = (EditextStandardC) findViewById(R.id.edtx_password_01);

        edtx_mcid_00 = (EditextStandardC) findViewById(R.id.edtx_mcid_00);
        edtx_name_00 = (EditextStandardC) findViewById(R.id.edtx_name_00);
        edtx_address_00 = (EditextStandardC) findViewById(R.id.edtx_address_00);
        edtx_phone1_00  = (EditextStandardC) findViewById(R.id.edtx_phone1_00);
        edtx_phone2_00  = (EditextStandardC) findViewById(R.id.edtx_phone2_00);
        edtx_id_00      = (EditextStandardC) findViewById(R.id.edtx_id_00);
        spsn_idtype_00  = (SimpleSpinner)    findViewById(R.id.spsn_idtype_00);


        bbtn_action_00 = (Button) findViewById(R.id.bbtn_action_00);

        edtx_email_00.setTitle(r.getString(R.string.email_address));
        edtx_email_00.setMandatory(true);
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtx_password_00.setTitle(r.getString(R.string.password));
        edtx_password_00.setMandatory(true);
        edtx_password_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_password_01.setTitle(r.getString(R.string.confirmation));
        edtx_password_01.setMandatory(true);
        edtx_password_01.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_password_00.setVisibility(View.GONE);
        edtx_password_01.setVisibility(View.GONE);


        edtx_mcid_00.setTitle(r.getString(R.string.merchant_code));
        edtx_mcid_00.setReadOnly(true);
        edtx_mcid_00.setVisibility(View.GONE);

        edtx_name_00.setTitle(r.getString(R.string.name));
        edtx_name_00.setMandatory(true);
        edtx_address_00.setTitle(r.getString(R.string.address));
        edtx_phone1_00.setTitle(r.getString(R.string.phone_number_1));
        edtx_phone1_00.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone2_00.setTitle(r.getString(R.string.phone_number_2));
        edtx_phone2_00.setInputType(InputType.TYPE_CLASS_PHONE);
        spsn_idtype_00.setTitle(r.getString(R.string.id_type));
        spsn_idtype_00.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_COMPANY,this));
        edtx_id_00.setTitle(r.getString(R.string.id_number));
        edtx_id_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

    }

    private void send(){
        if (!checkMandatory()){
            return;
        }
        CompanyDB company = new CompanyDB();
        company.getData(this, userDB.id +"");

        PostManager pos = new PostManager(this);
        pos.setApiUrl("add-store");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("email",edtx_email_00.getValue()));
        mHolders.add(new KeyValueHolder("description","Store of "+ company.name));
        mHolders.add(new KeyValueHolder("name",edtx_name_00.getValue()));
        mHolders.add(new KeyValueHolder("address",edtx_address_00.getValue()));
        mHolders.add(new KeyValueHolder("phone1",edtx_phone1_00.getValue()));
        mHolders.add(new KeyValueHolder("phone2",edtx_phone2_00.getValue()));
        mHolders.add(new KeyValueHolder("identity_no",edtx_id_00.getValue()));
        mHolders.add(new KeyValueHolder("identity_type",spsn_idtype_00.getValue().key));
        mHolders.add(new KeyValueHolder("city",""));
        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(DetailStoreActivity.this,
                            r.getString(R.string.store_success_created),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    DetailStoreActivity.this.finish();
                }
                else {
                    Toast.makeText(DetailStoreActivity.this,
                            r.getString(R.string.failed_to_save),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean checkMandatory(){
        boolean completed = true;
        if (edtx_email_00.getValue().isEmpty()){
            edtx_email_00.showNotif(r.getString(R.string.field_required));
            completed = false;
        }
        else if (edtx_name_00.getValue().isEmpty()){
            edtx_name_00.showNotif(r.getString(R.string.field_required));
            completed = false;
        }
        return completed;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }
}
