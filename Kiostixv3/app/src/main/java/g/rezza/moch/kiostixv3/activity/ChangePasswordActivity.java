package g.rezza.moch.kiostixv3.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextStandardBlack;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import io.fabric.sdk.android.Fabric;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditextStandardBlack edtx_old_00;
    private EditextStandardBlack edtx_new_00;
    private EditextStandardBlack edtx_conf_00;
    private Button           bbtn_login_00;
    private ImageView        imvw_back_00;

    private CustomerDB customer = new CustomerDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_edit_password);

        edtx_old_00       = (EditextStandardBlack) findViewById(R.id.edtx_old_00);
        edtx_new_00       = (EditextStandardBlack) findViewById(R.id.edtx_new_00);
        edtx_conf_00      = (EditextStandardBlack) findViewById(R.id.edtx_conf_00);
        bbtn_login_00     = (Button)           findViewById(R.id.bbtn_login_00);
        imvw_back_00      = (ImageView)        findViewById(R.id.imvw_back_00);

        customer.getData(this);

        setupComponent();
    }

    private void setupComponent(){
        edtx_old_00.setTitle(getResources().getString(R.string.old_password));
        edtx_old_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtx_new_00.setTitle(getResources().getString(R.string.new_password));
        edtx_new_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtx_conf_00.setTitle(getResources().getString(R.string.confirm_password));
        edtx_conf_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        bbtn_login_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendApi();

            }
        });

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void sendApi(){
        String old_password    = edtx_old_00.getValue();
        String new_password    = edtx_new_00.getValue();
        String conf_password    = edtx_conf_00.getValue();

        if (old_password.isEmpty()){
            edtx_old_00.showNotif(getResources().getString(R.string.old_password)+" Required ");
            return;
        }
        else if (!old_password.equals(customer.password)){
            edtx_old_00.showNotif(getResources().getString(R.string.old_password)+" tidak sesuai ");
            return;
        }
        else if (new_password.isEmpty()){
            edtx_new_00.showNotif(getResources().getString(R.string.new_password)+" tidak sesuai ");
            return;
        }
        else if (!new_password.equals(conf_password)){
            edtx_conf_00.showNotif(getResources().getString(R.string.confirm_password)+" tidak sesuai ");
            return;
        }

        customer.password = new_password;

        PostManager post = new PostManager(this);
        post.setApiUrl("user/update");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("token",customer.token));
        kvs.add(new KeyValueHolder("password_confirmation",new_password));
        kvs.add(new KeyValueHolder("password",conf_password));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == 200){
                    try {
                        if (obj.getString("code").equals("1001")){
                            customer.clearData(ChangePasswordActivity.this);
                            customer.insert(ChangePasswordActivity.this);
                            Toast.makeText(ChangePasswordActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                            ChangePasswordActivity.this.setResult(1001);
                            onBackPressed();
                        }  else {
                            Toast.makeText(ChangePasswordActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            Toast.makeText(ChangePasswordActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(ChangePasswordActivity.this, "Perubahan data gagal",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
