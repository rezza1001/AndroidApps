package g.rezza.moch.mobilesales.Activity.Sales;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import g.rezza.moch.mobilesales.component.SimpleDatePicker;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;

public class AddSalesActivity extends ActivityDtl {

    private EditextStandardC edtx_email_00;
    private EditextStandardC edtx_password_00;
    private EditextStandardC edtx_password_01;

    private EditextStandardC edtx_name_01;
    private EditextStandardC edtx_address_01;
    private EditextStandardC edtx_phone1_01;
    private EditextStandardC edtx_phone2_01;
    private EditextStandardC edtx_id_01;
    private SimpleSpinner    spsn_idtype_01;
    private SimpleSpinner    spsn_gender_01;
    private SimpleDatePicker smdp_dob_01;

    private Button bbtn_action_00;

    private int store_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.new_sales));
        hideRightMenu(true);
        store_id = getIntent().getIntExtra("STORE_ID", 0);
        Log.d(TAG, "onPostLayout: "+ store_id);

        edtx_email_00    = (EditextStandardC) findViewById(R.id.edtx_email_00);
        edtx_password_00 = (EditextStandardC) findViewById(R.id.edtx_password_00);
        edtx_password_01 = (EditextStandardC) findViewById(R.id.edtx_password_01);

        edtx_name_01    = (EditextStandardC) findViewById(R.id.edtx_name_01);
        edtx_address_01 = (EditextStandardC) findViewById(R.id.edtx_address_01);
        edtx_phone1_01  = (EditextStandardC) findViewById(R.id.edtx_phone1_01);
        edtx_phone2_01  = (EditextStandardC) findViewById(R.id.edtx_phone2_01);
        edtx_id_01      = (EditextStandardC) findViewById(R.id.edtx_id_01);
        spsn_idtype_01  = (SimpleSpinner)    findViewById(R.id.spsn_idtype_01);
        spsn_gender_01  = (SimpleSpinner)    findViewById(R.id.spsn_gender_01);
        smdp_dob_01     = (SimpleDatePicker) findViewById(R.id.smdp_dob_01);
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

        edtx_name_01.setTitle(r.getString(R.string.full_name));
        edtx_name_01.setMandatory(true);
        edtx_address_01.setTitle(r.getString(R.string.address));
        edtx_phone1_01.setTitle(r.getString(R.string.phone_number_1));
        edtx_phone1_01.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone2_01.setTitle(r.getString(R.string.phone_number_2));
        edtx_phone2_01.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_id_01.setTitle(r.getString(R.string.id_number));
        edtx_id_01.setInputType(InputType.TYPE_CLASS_NUMBER);
        spsn_idtype_01.setTitle(r.getString(R.string.id_type));
        spsn_idtype_01.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_PERSON,this));
        spsn_gender_01.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_GENDER,this));
        spsn_gender_01.setTitle(r.getString(R.string.gender));
        smdp_dob_01.setTitle(r.getString(R.string.dob));

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
        pos.setApiUrl("create-sales");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        mHolders.add(new KeyValueHolder("email",edtx_email_00.getValue()));
        mHolders.add(new KeyValueHolder("name",edtx_name_01.getValue()));
        mHolders.add(new KeyValueHolder("address",edtx_address_01.getValue()));
        mHolders.add(new KeyValueHolder("phone1",edtx_phone1_01.getValue()));
        mHolders.add(new KeyValueHolder("phone2",edtx_phone2_01.getValue()));
        mHolders.add(new KeyValueHolder("gender",spsn_gender_01.getValue().key));
        mHolders.add(new KeyValueHolder("identity_no",edtx_id_01.getValue()));
        mHolders.add(new KeyValueHolder("dob",smdp_dob_01.getValueToServer()));
        mHolders.add(new KeyValueHolder("identity_type",spsn_idtype_01.getValue().key));
        mHolders.add(new KeyValueHolder("city",""));
        mHolders.add(new KeyValueHolder("store_id",store_id+""));

        pos.setData(mHolders);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(AddSalesActivity.this,
                            r.getString(R.string.sales_success_created),Toast.LENGTH_SHORT).show();
                    setResult(ErrorCode.OK);
                    AddSalesActivity.this.finish();
                }
                else {
                    Toast.makeText(AddSalesActivity.this,
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
        else if (edtx_name_01.getValue().isEmpty()){
            edtx_name_01.showNotif(r.getString(R.string.field_required));
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
