package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextStandardC;
import g.rezza.moch.kiostixv3.component.MyDatePickerRegister;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import io.fabric.sdk.android.Fabric;

public class RegisterActivity extends AppCompatActivity {

    private EditextStandardC edtx_name_00;
    private EditextStandardC edtx_email_00;
    private EditextStandardC edtx_phone_00;
    private EditextStandardC edtx_id_00;
    private EditextStandardC edtx_password_00;
    private EditextStandardC edtx_confirm_00;
    private MyDatePickerRegister
                             edtx_dob_00;
    private Button           bbtn_login_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_register);

        edtx_name_00        = (EditextStandardC) findViewById(R.id.edtx_name_00);
        edtx_email_00       = (EditextStandardC) findViewById(R.id.edtx_email_00);
        edtx_phone_00       = (EditextStandardC) findViewById(R.id.edtx_phone_00);
        edtx_id_00          = (EditextStandardC) findViewById(R.id.edtx_id_00);
        edtx_password_00    = (EditextStandardC) findViewById(R.id.edtx_password_00);
        edtx_confirm_00     = (EditextStandardC) findViewById(R.id.edtx_confirm_00);
        bbtn_login_00       = (Button)           findViewById(R.id.bbtn_login_00);
        edtx_dob_00         = (MyDatePickerRegister)
                                                 findViewById(R.id.edtx_dob_00);

        setupComponent();
    }

    private void setupComponent(){
        edtx_email_00.setTitle(getResources().getString(R.string.email));
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtx_password_00.setTitle(getResources().getString(R.string.password));
        edtx_password_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_confirm_00.setTitle(getResources().getString(R.string.confirm_password));
        edtx_confirm_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtx_name_00.setTitle(getResources().getString(R.string.full_name));

        edtx_phone_00.setTitle(getResources().getString(R.string.handphone));
        edtx_phone_00.setInputType(InputType.TYPE_CLASS_PHONE);

        edtx_id_00.setTitle(getResources().getString(R.string.no_identity));
        edtx_id_00.setInputType(InputType.TYPE_CLASS_NUMBER);

        edtx_dob_00.setTitle(getResources().getString(R.string.dob));

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendApi();

            }
        });
    }

    private void sendApi(){
        final String email    = edtx_email_00.getValue();
        final String password = edtx_password_00.getValue();
        String phone        = edtx_phone_00.getValue();
        String name         = edtx_name_00.getValue();
        String identity     = edtx_id_00.getValue();
        String confirm      = edtx_confirm_00.getValue();
        String dob          = edtx_dob_00.getValue();


        Log.d("TAGREGIST","DOB : "+ dob);


        if (name.isEmpty()){
            edtx_name_00.showNotif(getResources().getString(R.string.full_name)+" Required ");
            return;
        }
        else if (email.isEmpty()){
            edtx_email_00.showNotif(getResources().getString(R.string.email)+" Required ");
            return;
        }
        else if (phone.isEmpty()){
            edtx_phone_00.showNotif(getResources().getString(R.string.handphone)+" Required ");
            return;
        }
        else if (dob.isEmpty()){
            edtx_id_00.showNotif(getResources().getString(R.string.dob)+" Required ");
            return;
        }
        else if (password.isEmpty()){
            edtx_password_00.showNotif(getResources().getString(R.string.password)+" Required ");
            return;
        }
        else if (!password.equals(confirm)){
            edtx_confirm_00.showNotif(getResources().getString(R.string.password)+" Not match ");
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("user/register");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("email",email));
        kvs.add(new KeyValueHolder("password",password));
        kvs.add(new KeyValueHolder("fullname",name));
        kvs.add(new KeyValueHolder("phone",phone));
        kvs.add(new KeyValueHolder("dob",dob));
        kvs.add(new KeyValueHolder("id_type","ktp"));
        kvs.add(new KeyValueHolder("id_number","123"));
        kvs.add(new KeyValueHolder("password_confirmation",confirm));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        if (obj.getString("code").equals("1001")){
                           sendLogin(email, password);
                            Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        }  else {
                            Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivity.this, "Login gagal",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void sendLogin(String email, final String password){
        PostManager post = new PostManager(this);
        post.setApiUrl("login");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("email",email));
        kvs.add(new KeyValueHolder("password",password));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){

                    try {
                        if (obj.getString("code").equals("1001")){
                            JSONObject data = obj.getJSONArray("data").getJSONObject(0);
                            CustomerDB customer = new CustomerDB();
                            customer.clearData(RegisterActivity.this);
                            customer.id     = data.getString("user_id");
                            customer.email  = data.getString("user_email");
                            customer.name   = data.getString("user_name");
                            customer.phone      = data.getString("user_phone");
                            customer.identity   = data.getString("id_number");
                            customer.token      = data.getString("token");
                            customer.password      = password;

                            DateFormat sdf_in   = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            DateFormat sdf_out  = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                            Calendar cal_dob    = Calendar.getInstance();
                            try {
                                cal_dob.setTime(sdf_in.parse(data.getString("user_dob")));
                                customer.dob   = sdf_out.format(cal_dob.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            customer.insert(RegisterActivity.this);
                            if (getIntent().getStringExtra("FROM").equals("3")){
                                setResult(0);
                                RegisterActivity.this.finish();
                            }
                            else {
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();

                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Toast.makeText(RegisterActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(RegisterActivity.this, "Login gagal",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,OnBoardingActivity.class);
        if (getIntent().getStringExtra("FROM").equals("2")){
            intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else if (getIntent().getStringExtra("FROM").equals("3")){
            setResult(-1);
        }
        else {
            startActivity(intent);

        }
        this.finish();

    }
}
