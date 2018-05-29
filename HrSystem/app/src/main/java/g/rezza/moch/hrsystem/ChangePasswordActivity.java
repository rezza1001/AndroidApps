package g.rezza.moch.hrsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.hrsystem.component.EditextStandardC;
import g.rezza.moch.hrsystem.connection.PostManager;
import g.rezza.moch.hrsystem.database.UserDB;
import g.rezza.moch.hrsystem.holder.KeyValueHolder;
import io.fabric.sdk.android.Fabric;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditextStandardC edtx_old_00;
    private EditextStandardC edtx_new_00;
    private EditextStandardC edtx_conf_00;
    private Button           bbtn_login_00;
    private ImageView        imvw_back_00;

    private UserDB customer = new UserDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_edit_password);

        edtx_old_00       = (EditextStandardC) findViewById(R.id.edtx_old_00);
        edtx_new_00       = (EditextStandardC) findViewById(R.id.edtx_new_00);
        edtx_conf_00      = (EditextStandardC) findViewById(R.id.edtx_conf_00);
        bbtn_login_00     = (Button)           findViewById(R.id.bbtn_login_00);
        imvw_back_00      = (ImageView)        findViewById(R.id.imvw_back_00);


        customer.getMine(this);

        setupComponent();
    }

    private void setupComponent(){
        edtx_old_00.setTitle("Password");
        edtx_old_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtx_new_00.setTitle("Password Baru");
        edtx_new_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtx_conf_00.setTitle("Confirmasi");
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
            edtx_old_00.showNotif(" Harus diisi ");
            return;
        }
        else if (!old_password.equals(customer.password)){
            edtx_old_00.showNotif("Password tidak sesuai ");
            return;
        }
        else if (new_password.isEmpty()){
            edtx_new_00.showNotif(" tidak sesuai ");
            return;
        }
        else if (!new_password.equals(conf_password)){
            edtx_conf_00.showNotif(" tidak sesuai ");
            return;
        }

        customer.password = new_password;

        PostManager post = new PostManager(this);
        post.setApiUrl("change_pasword");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("user_id",customer.id));
        kvs.add(new KeyValueHolder("password",new_password));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == 200){
                    try {
                        customer.deleteData(ChangePasswordActivity.this, customer.id +"");
                        customer.insert(ChangePasswordActivity.this);
                        Toast.makeText(ChangePasswordActivity.this, "Perubahan berhasil",Toast.LENGTH_SHORT).show();
                        ChangePasswordActivity.this.setResult(1001);
                        onBackPressed();

                    } catch (Exception e) {
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
