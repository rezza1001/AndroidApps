package g.rezza.moch.hrsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.hrsystem.connection.PostManager;
import g.rezza.moch.hrsystem.database.EmployeesDB;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import g.rezza.moch.hrsystem.lib.ErrorCode;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText edtx_email_00;
    private EditText edtx_password_00;
    private RelativeLayout bbtn_login_00;
    private String imei = "0";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_login);
        bbtn_login_00 = (RelativeLayout) findViewById(R.id.bbtn_login_00);
        edtx_email_00 = (EditText) findViewById(R.id.edtx_email_00);
        edtx_password_00 = (EditText) findViewById(R.id.edtx_password_00);

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        edtx_email_00.setText("rezza_gumilang@yahoo.com");
        edtx_password_00.setText("asdqwe");

        TelephonyManager mTelephone = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "PERMISSION IMEI NOT OK");
            return;
        }
        try {
            imei = mTelephone.getImei();
        }catch (Exception e){
            imei = getUniqueID();
        }
        catch(Error e2) {
            imei = getUniqueID();
        }
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

    private void login() {
        PostManager post = new PostManager(this);
        post.setApiUrl("login_mobile");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("username", edtx_email_00.getText().toString()));
        kvs.add(new KeyValueHolder("password", edtx_password_00.getText().toString()));
        kvs.add(new KeyValueHolder("imei", imei));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                try {
                    if (ErrorCode.OK == code) {
                        JSONObject USER = obj.getJSONObject("data");
                        UserDB userDB = new UserDB();
                        userDB.clearData(LoginActivity.this);
                        userDB.password = edtx_password_00.getText().toString();
                        userDB.email = USER.getString("email");
                        userDB.mine = 1;
                        userDB.id = Integer.parseInt(USER.getString("id"));
                        userDB.token = USER.getString("remember_token");
                        userDB.insert(LoginActivity.this);
                        onSuccessLogin(userDB.id);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

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

    private void onSuccessLogin(int userID) {
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

        PostManager post = new PostManager(this);
        post.setApiUrl("employee");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                try {
                    if (ErrorCode.OK == code) {
                        JSONObject data = obj.getJSONObject("data");
                        EmployeesDB employee = new EmployeesDB();
                        employee.clearData(LoginActivity.this);
                        employee.id = data.getInt("id");
                        employee.user_id = data.getInt("user");
                        employee.name = data.getString("name");
                        employee.gender = data.getInt("gender");
                        employee.phone = data.getString("phone");
                        employee.alt_phone = data.getString("alt_phone");
                        employee.dob = data.getString("dob");
                        employee.pob = data.getString("pob");
                        employee.npwp = data.getString("npwp");
                        employee.email = data.getString("email");
                        employee.address = data.getString("address");
                        JSONObject org = data.getJSONObject("organization");
                        employee.org = org.getInt("id");
                        employee.org_desc = org.getString("name");
                        employee.insert(LoginActivity.this);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

            }
        });
    }

    public String getUniqueID() {
        String myAndroidDeviceId = "";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "-1";
        }
        if (mTelephony.getDeviceId() != null) {
            myAndroidDeviceId = mTelephony.getDeviceId();
        } else {
            myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }
}
