package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextStandardC;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import io.fabric.sdk.android.Fabric;

public class SignInActivity extends AppCompatActivity {

    private EditextStandardC edtx_email_00;
    private EditextStandardC edtx_password_00;
    private Button          bbtn_login_00;
    private TextView        txvw_forgot_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_sign_in);

        edtx_email_00       = (EditextStandardC) findViewById(R.id.edtx_email_00);
        edtx_password_00    = (EditextStandardC) findViewById(R.id.edtx_password_00);
        bbtn_login_00       = (Button) findViewById(R.id.bbtn_login_00);
        txvw_forgot_00      = (TextView)    findViewById(R.id.txvw_forgot_00);
        setupComponent();
    }

    private void setupComponent(){
        edtx_email_00.setTitle(getResources().getString(R.string.email));
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtx_password_00.setTitle(getResources().getString(R.string.password));
        edtx_password_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtx_email_00.getValue().isEmpty()){
                    edtx_email_00.showNotif(getResources().getString(R.string.email)+" Required ");
                    return;
                }
                else if (edtx_password_00.getValue().isEmpty()){
                    edtx_password_00.showNotif(getResources().getString(R.string.password)+" Required ");
                    return;
                }
                sendLogin(edtx_email_00.getValue(), edtx_password_00.getValue());
            }
        });

        txvw_forgot_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
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
                            customer.clearData(SignInActivity.this);
                            customer.id     = data.getString("user_id");
                            customer.email  = data.getString("user_email");
                            customer.name   = data.getString("user_name");
                            customer.dob    = data.getString("user_dob");
                            customer.phone    = data.getString("user_phone");
                            customer.identity    = data.getString("id_number");
                            customer.token      = data.getString("token");
                            customer.password      = password;
                            customer.insert(SignInActivity.this);

                            if (getIntent().getStringExtra("FROM").equals("3")){
                                setResult(0);
                                SignInActivity.this.finish();
                            }
                            else {
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                SignInActivity.this.finish();
                            }


                        }
                        else {
                            Toast.makeText(SignInActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Toast.makeText(SignInActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(SignInActivity.this, "Login gagal",Toast.LENGTH_SHORT).show();
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
