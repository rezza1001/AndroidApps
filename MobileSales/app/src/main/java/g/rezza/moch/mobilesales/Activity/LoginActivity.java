package g.rezza.moch.mobilesales.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import g.rezza.moch.mobilesales.DataStatic.App;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.DataStatic.RoleUser;
import g.rezza.moch.mobilesales.Database.CompanyDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.lib.Parse;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView edtx_email_00;
    private EditText edtx_password_00;
    private Button bbtn_signin_00;
    private TelephonyManager mTelephone;
    private String imei = "";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtx_email_00       = (AutoCompleteTextView) findViewById(R.id.edtx_email_00);
        edtx_password_00    = (EditText) findViewById(R.id.edtx_password_00);
        bbtn_signin_00      = (Button) findViewById(R.id.bbtn_signin_00);


        loadExistingEmails();
        mTelephone = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = mTelephone.getImei();

        edtx_password_00.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        bbtn_signin_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        edtx_email_00.setError(null);
        edtx_password_00.setError(null);

        String email = edtx_email_00.getText().toString();
        String password = edtx_password_00.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtx_password_00.setError(getString(R.string.error_invalid_password));
            focusView = edtx_password_00;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtx_email_00.setError(getString(R.string.error_field_required));
            focusView = edtx_email_00;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edtx_email_00.setError(getString(R.string.error_invalid_email));
            focusView = edtx_email_00;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            login();
        }
    }

    private void login(){
        PostManager post = new PostManager(this);
        post.setApiUrl("login");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("email", edtx_email_00.getText().toString()));
        kvs.add(new KeyValueHolder("password",edtx_password_00.getText().toString()));
        kvs.add(new KeyValueHolder("app_type", App.TYPE + ""));
        kvs.add(new KeyValueHolder("imei", imei));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                try {
                    if (ErrorCode.OK == code){
                        onLogin(obj);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

                }


            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void loadExistingEmails(){
        ArrayList<String> emails = new ArrayList<>();
        emails.add("rezza_gumilang@yahoo.com");
        emails.add("merchant1@gmail.com");
        addEmailsToAutoComplete(emails);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        edtx_email_00.setAdapter(adapter);
    }


    private void onLogin(JSONObject obj){
        try {
            JSONObject JUSER    = obj.getJSONObject("USER");
            JSONObject ROLE     = obj.getJSONObject("ROLE");

            JSONObject ACCOUNT  = obj.getJSONObject("USER ACCOUNT");
            JSONObject INFO     = obj.getJSONObject("COMPANY INFO");

            UserDB user             = new UserDB();
            UserInfoDB userInfoDB   = new UserInfoDB();
            CompanyDB store         = new CompanyDB();

            user.clearData(this);
            userInfoDB.clearData(this);
            store.clearData(this);

            user.id         = JUSER.getInt("id");
            user.email      = JUSER.getString("email");
            user.token      = JUSER.getString("remember_token");
            user.password   = edtx_password_00.getText().toString();
            user.parent     = JUSER.getInt("parent");
            user.account    = ACCOUNT.getString("account_no");
            user.balance    = ACCOUNT.getLong("balance");
            user.type       = ACCOUNT.getInt("status");
            user.role_id    = ROLE.getInt("id");
            user.role_dec   = ROLE.getString("name");
            user.mine       = 1;
            user.createContentValues();
            user.insert(this);

            if (user.role_id  == RoleUser.MERCHANT ){
                userInfoDB.gender        = 0;
                userInfoDB.dob           = "00-00-0000";
                userInfoDB.user_id       = user.id;
                insertUserInfo(userInfoDB, INFO);
            }
            else if (user.role_id  == RoleUser.MERCHANT_PERSONAL){
                userInfoDB.gender        = INFO.getInt("gender");
                userInfoDB.dob           = Parse.getDateCustom(INFO.getString("dob"));
                userInfoDB.user_id       = user.id;
                insertUserInfo(userInfoDB, INFO);
            }
            else if (user.role_id  == RoleUser.STORE){
                store.user_id = user.id;
                insertCompany(store,INFO );
            }


            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }catch (JSONException ex){
            Log.e("TAGRZ",ex.getMessage());
            Toast.makeText(this,"Invalid data "+ ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void insertUserInfo(UserInfoDB userInfoDB, JSONObject jo){
        try {
            userInfoDB.name          = jo.getString("name");
            userInfoDB.mc_code       = jo.getString("merchant_code");
            userInfoDB.address       = jo.getString("address");
            userInfoDB.phone1        = jo.getString("phone1");
            userInfoDB.phone2        = jo.getString("phone2");
            userInfoDB.identity_type = jo.getInt("identity_type");
            userInfoDB.identity_no   = jo.getString("identity_no");
            userInfoDB.insert(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void insertCompany(CompanyDB store, JSONObject jo){
        try {
            store.name          = jo.getString("name");
            store.address       = jo.getString("address");
            store.phone1        = jo.getString("phone1");
            store.phone2        = jo.getString("phone2");
            store.identity_type = jo.getInt("identity_type");
            store.identity_no   = jo.getString("identity_no");
            store.createContentValues();
            store.insert(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
