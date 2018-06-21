package g.rezza.moch.mobilesales.Activity.MyAccount;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.DataStatic.RoleUser;
import g.rezza.moch.mobilesales.Activity.MyAccountActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;

public class CompanyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditextStandardC text_mcname_00, text_mcaddress_00, text_mcphone1_00;
    private EditextStandardC text_mcphone2_00, text_mcidno_00;
    private SimpleSpinner text_mc_idtype_00;
    private TextView txvw_company_00;
    private Resources r;
    private UserDB mUser;
    private Button bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        mUser = new UserDB();
        mUser.getMine(this);

        r = getResources();

        txvw_company_00 = (TextView) findViewById(R.id.txvw_company_00);

        text_mcname_00 = (EditextStandardC) findViewById(R.id.text_mcname_00);
        text_mcname_00.setTitle(r.getString(R.string.name));

        text_mcaddress_00 = (EditextStandardC) findViewById(R.id.text_mcaddress_00);
        text_mcaddress_00.setTitle(r.getString(R.string.address));

        text_mcphone1_00 = (EditextStandardC) findViewById(R.id.text_mcphone1_00);
        text_mcphone1_00.setTitle(r.getString(R.string.phone_number_1));
        text_mcphone1_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_mcphone2_00 = (EditextStandardC) findViewById(R.id.text_mcphone2_00);
        text_mcphone2_00.setTitle(r.getString(R.string.phone_number_2));
        text_mcphone2_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_mc_idtype_00 = (SimpleSpinner) findViewById(R.id.text_mc_idtype_00);
        text_mc_idtype_00.setTitle(r.getString(R.string.id_type));
        text_mc_idtype_00.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_COMPANY,this));

        text_mcidno_00 = (EditextStandardC) findViewById(R.id.text_mcidno_00);
        text_mcidno_00.setTitle(r.getString(R.string.id_number));
        text_mcidno_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        bbtn_action_00 = (Button) findViewById(R.id.bbtn_action_00);
        bbtn_action_00.setOnClickListener(this);

        txvw_company_00.setText(r.getString(R.string.please_complete_merchantinfo));
        CompanyDB mMerchant = new CompanyDB();
        mMerchant.getData(this, mUser.id+"");
        text_mcidno_00.setValue(mMerchant.identity_no);
        text_mcphone2_00.setValue(mMerchant.phone2);
        text_mcphone1_00.setValue(mMerchant.phone1);
        text_mcaddress_00.setValue(mMerchant.address);
        text_mcname_00.setValue(mMerchant.name);
        text_mc_idtype_00.setValue(mMerchant.identity_type+"");
    }

    @Override
    public void onClick(View view) {
        send();
    }

    private void send(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-company");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("name",text_mcname_00.getValue()));
        kvs.add(new KeyValueHolder("description",""));
        kvs.add(new KeyValueHolder("address",text_mcaddress_00.getValue()));
        kvs.add(new KeyValueHolder("phone1",text_mcphone1_00.getValue()));
        kvs.add(new KeyValueHolder("phone2",text_mcphone2_00.getValue()));
        kvs.add(new KeyValueHolder("identity_type",text_mc_idtype_00.getValue().key+""));
        kvs.add(new KeyValueHolder("identity_no",text_mcidno_00.getValue()+""));
        kvs.add(new KeyValueHolder("city",""));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    saveToLocalDB();
                    Intent intent = new Intent(CompanyInfoActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(CompanyInfoActivity.this,
                            getResources().getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void saveToLocalDB(){
        CompanyDB merchant = new CompanyDB();
        merchant.name     = text_mcname_00.getValue();
        merchant.user_id  = mUser.id;
        merchant.address  = text_mcaddress_00.getValue();
        merchant.phone1   = text_mcphone1_00.getValue();
        merchant.phone2   = text_mcphone2_00.getValue();
        merchant.identity_type = Integer.parseInt(text_mc_idtype_00.getValue().key);
        merchant.identity_no   = text_mcidno_00.getValue();
        merchant.update(this, ""+ merchant.FIELD_USER+ "="+ mUser.id);


        mUser.update = 1;
        mUser.update(this, " " + mUser.FIELD_ID +"="+ mUser.id);
    }
}
