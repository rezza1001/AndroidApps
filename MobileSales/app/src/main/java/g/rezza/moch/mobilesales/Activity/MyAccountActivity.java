package g.rezza.moch.mobilesales.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.DataStatic.RoleUser;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.TextKeyValueView;
import g.rezza.moch.mobilesales.component.SpinKeyValueView;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.LoadingScreen;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;
import g.rezza.moch.mobilesales.lib.Parse;

public class MyAccountActivity extends ActivityWthHdr implements View.OnClickListener {

    private TextKeyValueView text_mccode_01, edtx_email_00,edtx_password_00,edtx_account_00, text_name_00,text_address_00;
    private TextKeyValueView text_phone1_00, text_phone2_00;
    private SpinKeyValueView smpr_gender_00, smpr_idtype_00;
    private TextKeyValueView smdp_dob_00;
    private TextKeyValueView text_idvalue_00;
    private LinearLayout     lnly_userinfo_00;
    private TextView         txvw_userinfo_00;
    private TextKeyValueView text_mccode_00, text_mcname_00, text_mcaddress_00, text_mcphone1_00;
    private TextKeyValueView text_mcphone2_00, text_mcidno_00;
    private SpinKeyValueView text_mc_idtype_00;
    private TextView txvw_company_00;
    private LinearLayout lnly_company_00;
    private Button bbtn_action_00;

    private UserDB mUser;
    private UserInfoDB mUserInfo;
    private LoadingScreen mLoading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mLoading = new LoadingScreen(this);
        mLoading.setMessage(getResources().getString(R.string.please_wait));
        mUser = new UserDB();
        mUser.getMine(this);

        mUserInfo = new UserInfoDB();
        mUserInfo.getData(this, mUser.id);

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.my_account));
        hideMenuRight(true);
        bbtn_action_00 = (Button) findViewById(R.id.bbtn_action_00);
        bbtn_action_00.setOnClickListener(this);

        createAccountInfo();
        createUserInfo();
        createStoreInfo();
        accessLevelControl();
    }

    private void accessLevelControl(){
        if (userDB.role_id == RoleUser.STORE){
            lnly_userinfo_00.setVisibility(View.GONE);
            txvw_userinfo_00.setVisibility(View.GONE);
            text_mccode_00.setVisibility(View.GONE);
            text_mccode_01.setVisibility(View.GONE);
        }
        else {
            lnly_company_00.setVisibility(View.GONE);
            txvw_company_00.setVisibility(View.GONE);
        }
    }

    @Override
    protected void registerListener() {

    }

    @SuppressLint("ResourceType")
    private void createAccountInfo(){
        lnly_userinfo_00 = (LinearLayout) findViewById(R.id.lnly_userinfo_00);
        txvw_userinfo_00 = (TextView)     findViewById(R.id.txvw_userinfo_00);

        edtx_email_00 = (TextKeyValueView) findViewById(R.id.edtx_email_00);
        edtx_email_00.setTitle(r.getString(R.string.email_address));
        edtx_email_00.setValue(mUser.email);
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtx_password_00 = (TextKeyValueView) findViewById(R.id.edtx_password_00);
        edtx_password_00.setTitle(r.getString(R.string.password));
        edtx_password_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_password_00.setValue(mUser.password);

        edtx_account_00 = (TextKeyValueView) findViewById(R.id.edtx_account_00);
        edtx_account_00.setTitle(r.getString(R.string.account_number));
        edtx_account_00.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        edtx_account_00.setValue(mUser.account);
        edtx_account_00.setReadOnly(true);
    }

    @SuppressLint("ResourceType")
    private void createUserInfo(){

        text_mccode_01 = (TextKeyValueView) findViewById(R.id.text_mccode_01);
        text_mccode_01.setTitle(r.getString(R.string.merchant_code));
        text_mccode_01.setReadOnly(true);

        text_name_00 = (TextKeyValueView) findViewById(R.id.text_name_00);
        text_name_00.setTitle(r.getString(R.string.name));

        text_address_00 =(TextKeyValueView) findViewById(R.id.text_address_00);
        text_address_00.setTitle(r.getString(R.string.address));

        text_phone1_00 =(TextKeyValueView) findViewById(R.id.text_phone1_00);
        text_phone1_00.setTitle(r.getString(R.string.phone_number_1));
        text_phone1_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_phone2_00 = (TextKeyValueView) findViewById(R.id.text_phone2_00);
        text_phone2_00.setTitle(r.getString(R.string.phone_number_2));
        text_phone2_00.setInputType(InputType.TYPE_CLASS_PHONE);

        smpr_gender_00 = (SpinKeyValueView) findViewById(R.id.smpr_gender_00);
        smpr_gender_00.setTitle(r.getString(R.string.gender));
        smpr_gender_00.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_GENDER,this));

        smdp_dob_00 =  (TextKeyValueView) findViewById(R.id.smdp_dob_00);
        smdp_dob_00.setTitle(r.getString(R.string.dob));
        smdp_dob_00.setInputType(InputType.TYPE_CLASS_DATETIME);

        smpr_idtype_00 = (SpinKeyValueView) findViewById(R.id.smpr_idtype_00);
        smpr_idtype_00.setTitle(r.getString(R.string.id_type));
        smpr_idtype_00.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_PERSON,this));

        text_idvalue_00 = (TextKeyValueView) findViewById(R.id.text_idvalue_00);
        text_idvalue_00.setTitle(r.getString(R.string.id_number));

        if (mUserInfo.user_id != 0){
            text_name_00.setValue(mUserInfo.name);
            text_address_00.setValue(mUserInfo.address);
            text_phone1_00.setValue(mUserInfo.phone1);
            text_phone2_00.setValue(mUserInfo.phone2);
            smpr_idtype_00.setValue(mUserInfo.identity_type+"");
            text_idvalue_00.setValue(mUserInfo.identity_no);
            smdp_dob_00.setValue(mUserInfo.dob);
            smpr_gender_00.setValue(mUserInfo.gender+"");
            text_mccode_01.setValue(mUserInfo.mc_code);
        }

        if (userDB.role_id == RoleUser.MERCHANT) {
            smpr_gender_00.setVisibility(View.GONE);
            smdp_dob_00.setVisibility(View.GONE);
        }

    }

    @SuppressLint("ResourceType")
    private void createStoreInfo(){
        txvw_company_00 = (TextView) findViewById(R.id.txvw_company_00);
        lnly_company_00 = (LinearLayout) findViewById(R.id.lnly_company_00);

        text_mccode_00 = (TextKeyValueView) findViewById(R.id.text_mccode_00);
        text_mccode_00.setTitle(r.getString(R.string.merchant_code));
        text_mccode_00.setReadOnly(true);


        text_mcname_00 = (TextKeyValueView) findViewById(R.id.text_mcname_00);
        text_mcname_00.setTitle(r.getString(R.string.full_name));

        text_mcaddress_00 = (TextKeyValueView) findViewById(R.id.text_mcaddress_00);
        text_mcaddress_00.setTitle(r.getString(R.string.address));

        text_mcphone1_00 = (TextKeyValueView) findViewById(R.id.text_mcphone1_00);
        text_mcphone1_00.setTitle(r.getString(R.string.phone_number_1));
        text_mcphone1_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_mcphone2_00 = (TextKeyValueView) findViewById(R.id.text_mcphone2_00);
        text_mcphone2_00.setTitle(r.getString(R.string.phone_number_2));
        text_mcphone2_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_mc_idtype_00 = (SpinKeyValueView) findViewById(R.id.text_mc_idtype_00);
        text_mc_idtype_00.setTitle(r.getString(R.string.id_type));
        text_mc_idtype_00.setChooser(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_COMPANY,this));

        text_mcidno_00 = (TextKeyValueView) findViewById(R.id.text_mcidno_00);
        text_mcidno_00.setTitle(r.getString(R.string.id_number));
        text_mcidno_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        text_mccode_00.setReadOnly(true);
        txvw_company_00.setText(r.getString(R.string.merchant_information));
        CompanyDB mMerchant = new CompanyDB();
        mMerchant.getData(this, mUser.id+"");
        text_mcidno_00.setValue(mMerchant.identity_no);
        text_mcphone2_00.setValue(mMerchant.phone2);
        text_mcphone1_00.setValue(mMerchant.phone1);
        text_mcaddress_00.setValue(mMerchant.address);
        text_mcname_00.setValue(mMerchant.name);
        text_mccode_00.setValue(mMerchant.code);
        text_mc_idtype_00.setValue(mMerchant.identity_type+"");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View view) {
        sendUser();
    }

    private void saveToLocalCompanyDB(){
        mUser.password  = edtx_password_00.getValue();
        mUser.email     = edtx_email_00.getValue();
        mUser.update(this, " " + mUser.FIELD_ID +"="+ mUser.id);

        CompanyDB merchant = new CompanyDB();
        merchant.name     = text_mcname_00.getValue();
        merchant.user_id  = mUser.id;
        merchant.address  = text_mcaddress_00.getValue();
        merchant.phone1   = text_mcphone1_00.getValue();
        merchant.phone2   = text_mcphone2_00.getValue();
        merchant.identity_type = Integer.parseInt(text_mc_idtype_00.getValue());
        merchant.identity_no   = text_mcidno_00.getValue();
        merchant.update(this, ""+ merchant.FIELD_USER+ "="+ mUser.id);
        Toast.makeText(this,r.getString(R.string.update_success), Toast.LENGTH_LONG).show();
    }

    private void saveTolocalUserInfo(){
        mUser.password  = edtx_password_00.getValue();
        mUser.email     = edtx_email_00.getValue();
        mUser.update(this, " " + mUser.FIELD_ID +"="+ mUser.id);

        UserInfoDB userInfo = new UserInfoDB();
        userInfo.mc_code  = text_mccode_01.getValue();
        userInfo.name     = text_name_00.getValue();
        userInfo.user_id  = mUser.id;
        userInfo.address  = text_address_00.getValue();
        userInfo.phone1   = text_phone1_00.getValue();
        userInfo.phone2   = text_phone2_00.getValue();
        userInfo.identity_type = Integer.parseInt(smpr_idtype_00.getValue());
        userInfo.identity_no   = text_idvalue_00.getValue();
        userInfo.gender   =  Integer.parseInt(smpr_gender_00.getValue());
        userInfo.dob      = smdp_dob_00.getValue();
        userInfo.update(this, ""+ userInfo.FIELD_USER+ "="+ mUser.id);
        Toast.makeText(this,r.getString(R.string.update_success), Toast.LENGTH_LONG).show();
    }

    public void sendUserInfo() {
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-user");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("name", text_name_00.getValue()));
        kvs.add(new KeyValueHolder("address", text_address_00.getValue()));
        kvs.add(new KeyValueHolder("phone1", text_phone1_00.getValue()));
        kvs.add(new KeyValueHolder("phone2", text_phone2_00.getValue()));
        kvs.add(new KeyValueHolder("identity_type", smpr_idtype_00.getValue() + ""));
        kvs.add(new KeyValueHolder("identity_no", text_idvalue_00.getValue() + ""));
        kvs.add(new KeyValueHolder("city", ""));
        if (userDB.role_id == RoleUser.MERCHANT_PERSONAL){
            kvs.add(new KeyValueHolder("gender", smpr_gender_00.getValue() + ""));
            kvs.add(new KeyValueHolder("dob", Parse.getDateToServer(smdp_dob_00.getValue()) ));
        }
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                mLoading.dimiss();
                if (code == ErrorCode.OK){
                    saveTolocalUserInfo();
                }
                else {
                    Toast.makeText(MyAccountActivity.this,
                            getResources().getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendCompany(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-company");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("name",text_mcname_00.getValue()));
        kvs.add(new KeyValueHolder("description",""));
        kvs.add(new KeyValueHolder("address",text_mcaddress_00.getValue()));
        kvs.add(new KeyValueHolder("phone1",text_mcphone1_00.getValue()));
        kvs.add(new KeyValueHolder("phone2",text_mcphone2_00.getValue()));
        kvs.add(new KeyValueHolder("identity_type",text_mc_idtype_00.getValue()+""));
        kvs.add(new KeyValueHolder("identity_no",text_mcidno_00.getValue()+""));
        kvs.add(new KeyValueHolder("city",""));
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                mLoading.dimiss();
                if (code == ErrorCode.OK){
                   saveToLocalCompanyDB();
                }
                else {
                    Toast.makeText(MyAccountActivity.this,
                            getResources().getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendUser(){
        mLoading.show();
        PostManager pos = new PostManager(this);
        pos.setApiUrl("change-password");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("password", edtx_password_00.getValue()));
        pos.setData(kvs);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    if (userDB.role_id == RoleUser.STORE){
                        sendCompany();
                    }
                    else {
                        sendUserInfo();
                    }

                }
                else {
                    mLoading.dimiss();
                    Toast.makeText(MyAccountActivity.this,
                            getResources().getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
