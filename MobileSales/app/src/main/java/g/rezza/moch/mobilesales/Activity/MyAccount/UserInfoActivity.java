package g.rezza.moch.mobilesales.Activity.MyAccount;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.DataStatic.RoleUser;
import g.rezza.moch.mobilesales.Activity.MyAccountActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.EditextStandardC;
import g.rezza.moch.mobilesales.component.SimpleDatePicker;
import g.rezza.moch.mobilesales.component.SimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditextStandardC text_name_00,text_address_00,text_mcid_00;
    private EditextStandardC text_phone1_00, text_phone2_00;
    private SimpleSpinner    smpr_gender_00, smpr_idtype_00;
    private SimpleDatePicker smdp_dob_00;
    private EditextStandardC text_idvalue_00;
    private Resources r;
    private Button bbtn_action_00;
    private UserDB user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);
        r = getResources();
        user = new UserDB();
        user.getMine(this);

        text_mcid_00 = (EditextStandardC) findViewById(R.id.text_mcid_00);
        text_mcid_00.setTitle(r.getString(R.string.merchant_code));
        text_mcid_00.setReadOnly(true);

        text_name_00 = (EditextStandardC) findViewById(R.id.text_name_00);
        text_name_00.setTitle(r.getString(R.string.name));

        text_address_00 =(EditextStandardC) findViewById(R.id.text_address_00);
        text_address_00.setTitle(r.getString(R.string.address));

        text_phone1_00 =(EditextStandardC) findViewById(R.id.text_phone1_00);
        text_phone1_00.setTitle(r.getString(R.string.phone_number_1));
        text_phone1_00.setInputType(InputType.TYPE_CLASS_PHONE);

        text_phone2_00 = (EditextStandardC) findViewById(R.id.text_phone2_00);
        text_phone2_00.setTitle(r.getString(R.string.phone_number_2));
        text_phone2_00.setInputType(InputType.TYPE_CLASS_PHONE);

        smpr_gender_00 = (SimpleSpinner) findViewById(R.id.smpr_gender_00);
        smpr_gender_00.setTitle(r.getString(R.string.gender));
        smpr_gender_00.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_GENDER,this));

        smdp_dob_00 =  (SimpleDatePicker) findViewById(R.id.smdp_dob_00);
        smdp_dob_00.setTitle(r.getString(R.string.dob));

        smpr_idtype_00 = (SimpleSpinner) findViewById(R.id.smpr_idtype_00);
        smpr_idtype_00.setTitle(r.getString(R.string.id_type));
        smpr_idtype_00.setChoosers(DataSpiner.getData(DataSpiner.CATEGORY_IDTYPE_PERSON,this));

        text_idvalue_00 = (EditextStandardC) findViewById(R.id.text_idvalue_00);
        text_idvalue_00.setTitle(r.getString(R.string.id_number));
        text_idvalue_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        bbtn_action_00 = (Button) findViewById(R.id.bbtn_action_00);
        bbtn_action_00.setOnClickListener(this);

        if (user.role_id == RoleUser.MERCHANT){
            smpr_gender_00.setVisibility(View.GONE);
            smdp_dob_00.setVisibility(View.GONE);
        }


        UserInfoDB userInfo = new UserInfoDB();
        userInfo.getData(this, user.id);
        text_mcid_00.setValue(userInfo.mc_code);
        text_name_00.setValue(userInfo.name);
        text_address_00.setValue(userInfo.address);
        text_phone1_00.setValue(userInfo.phone1);
        text_phone2_00.setValue(userInfo.phone2);
        smpr_idtype_00.setValue(userInfo.identity_type+"");
        text_idvalue_00.setValue(userInfo.identity_no);
        smdp_dob_00.setValue(userInfo.dob);
        smpr_gender_00.setValue(userInfo.gender+"");


    }

    @Override
    public void onClick(View view) {
       send();

    }

    public void send(){
        PostManager pos = new PostManager(this);
        pos.setApiUrl("edit-user");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("name",text_name_00.getValue()));
        kvs.add(new KeyValueHolder("address",text_address_00.getValue()));
        kvs.add(new KeyValueHolder("phone1",text_phone1_00.getValue()));
        kvs.add(new KeyValueHolder("phone2",text_phone2_00.getValue()));
        kvs.add(new KeyValueHolder("identity_type",smpr_idtype_00.getValue().key+""));
        kvs.add(new KeyValueHolder("identity_no",text_idvalue_00.getValue()+""));
        kvs.add(new KeyValueHolder("city",""));
        if (user.role_id == RoleUser.MERCHANT_PERSONAL){
            kvs.add(new KeyValueHolder("gender",smpr_gender_00.getValue().key+""));
            kvs.add(new KeyValueHolder("dob",smdp_dob_00.getValueToServer()));
        }
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    saveToLocalDB();
                    Intent intent = new Intent(UserInfoActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(UserInfoActivity.this,
                            getResources().getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveToLocalDB(){
        UserInfoDB userInfo = new UserInfoDB();
        userInfo.mc_code  = text_mcid_00.getValue();
        userInfo.name     = text_name_00.getValue();
        userInfo.user_id  = user.id;
        userInfo.address  = text_address_00.getValue();
        userInfo.phone1   = text_phone1_00.getValue();
        userInfo.phone2   = text_phone2_00.getValue();
        userInfo.identity_type = Integer.parseInt(smpr_idtype_00.getValue().key);
        userInfo.identity_no   = text_idvalue_00.getValue();
        userInfo.gender   =  Integer.parseInt(smpr_gender_00.getValue().key);
        userInfo.dob      = smdp_dob_00.getValue();
        userInfo.createContentValues();
        userInfo.update(this, ""+ userInfo.FIELD_USER+ "="+ user.id);

        user.update = 1;
        user.update(this, " " + user.FIELD_ID +"="+ user.id);
    }
}
