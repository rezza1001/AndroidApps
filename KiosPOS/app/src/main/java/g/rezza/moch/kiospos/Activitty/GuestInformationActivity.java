package g.rezza.moch.kiospos.Activitty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.AutoComplete;
import g.rezza.moch.kiospos.component.GenderOption;
import g.rezza.moch.kiospos.component.MyDatePicker;
import g.rezza.moch.kiospos.component.MyEditext;
import g.rezza.moch.kiospos.component.MyEditextMail;
import g.rezza.moch.kiospos.holder.AttendenzHolder;
import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.lib.MasterTransActivity;
import g.rezza.moch.kiospos.lib.Parser;
import g.rezza.moch.kiospos.view.DataCheck;

/**
 * Created by rezza on 17/11/17.
 */

public class GuestInformationActivity extends MasterTransActivity implements MasterComponent.ChangeListener{

    private MyEditextMail   myedtx_mail_30;
    private MyEditext       myedtx_name_31;
    private MyDatePicker    mydtpk_birth_32;
    private GenderOption    gopt_gender_33;
    private AutoComplete    acpl_city_34;
    private Button          bbtn_next_31;
    private Button          bbtn_back_30;

    private boolean         isValidData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_information);
        showStep(6,1);

        myedtx_mail_30  =   (MyEditextMail)     findViewById(R.id.myedtx_mail_30);
        myedtx_name_31  =   (MyEditext)         findViewById(R.id.myedtx_name_31);
        mydtpk_birth_32 =   (MyDatePicker)      findViewById(R.id.mydtpk_birth_32);
        gopt_gender_33  =   (GenderOption)      findViewById(R.id.gopt_gender_33);
        acpl_city_34    =   (AutoComplete)      findViewById(R.id.acpl_city_34);
        bbtn_next_31    =   (Button)            findViewById(R.id.bbtn_next_31);
        bbtn_back_30    =   (Button)            findViewById(R.id.bbtn_back_30);


        create();
        registerListener();
        hideSoftKeyboard();
    }

    @Override
    protected void registerListener() {

        myedtx_mail_30.setOnChangeListener(this);
        myedtx_name_31.setOnChangeListener(this);
        mydtpk_birth_32.setOnChangeListener(this);
        gopt_gender_33.setOnChangeListener(this);
        acpl_city_34.setOnChangeListener(this);

        bbtn_back_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changeActivity(DataCheckActivity.class);
            }
        });

        bbtn_next_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAGRZ","onClick ");
            }
        });

    }

    @Override
    public void after(String s) {
        if(onCekData()){
            isValidData = true;
            bbtn_next_31.setBackgroundResource(R.drawable.button_blue06_round_right);
        }
        else {
            isValidData = false;
            bbtn_next_31.setBackgroundResource(R.drawable.button_blue06t30_round_right);
        }
    }



    private void create(){

        myedtx_mail_30.setTitle("Email Address");
        myedtx_name_31.setTitle("Full Name");
        mydtpk_birth_32.setTitle("Birth Date");
        gopt_gender_33.setTitle("Gender");
        acpl_city_34.setTitle("City (In accordance with valid ID)");

        try {
            JSONObject mData           = mObjectData.getJSONObject(this.getClass().getSimpleName());
            AttendenzHolder att = new AttendenzHolder().unpack(mData);
            myedtx_mail_30.setValue(att.email);
            myedtx_name_31.setValue(att.nama);
            gopt_gender_33.setValue(att.gender);
            acpl_city_34.setValue(att.city);
            mydtpk_birth_32.setValue(Parser.getDate(att.birth));

            myedtx_mail_30.disable();
            myedtx_name_31.disable();
            gopt_gender_33.disable();
            acpl_city_34.disable();
            mydtpk_birth_32.disable();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean onCekData(){
        P:{
            if (myedtx_name_31.getValue().equals("")){
                return false;
            }
            else if (mydtpk_birth_32.getValue().equals("")){
                return false;
            }
            else if (acpl_city_34.getValue().equals("")){
                return false;
            }
            else if (myedtx_mail_30.getValue().equals("")){
                return false;
            }
            else if (!MyEditextMail.emailValidation(myedtx_mail_30.getValue())){
                return false;
            }
            else if (gopt_gender_33.getValue().equals("")){
                return false;
            }
            else {
                AttendenzHolder att = new AttendenzHolder();
                att.nama        = myedtx_name_31.getValue();
                att.birth       = mydtpk_birth_32.getValue();
                att.city        = acpl_city_34.getValue();
                att.email       = myedtx_mail_30.getValue();
                att.gender      = gopt_gender_33.getValue();
                att.complete    = true;
                addObject(att.pack());
                return true;
            }
        }
    }
}
