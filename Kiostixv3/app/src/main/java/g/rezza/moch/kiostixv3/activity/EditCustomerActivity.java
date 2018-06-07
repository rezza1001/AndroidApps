package g.rezza.moch.kiostixv3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextAutoComplateBlack;
import g.rezza.moch.kiostixv3.component.EditextStandardBlack;
import g.rezza.moch.kiostixv3.component.MyDatePicker;
import g.rezza.moch.kiostixv3.component.MyDatePickerWthBorder;
import g.rezza.moch.kiostixv3.component.SimpleSpinner;
import g.rezza.moch.kiostixv3.component.SimpleSpinnerWthBorder;
import g.rezza.moch.kiostixv3.component.TextAreaStandardBlack;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.CustomerDB;
import g.rezza.moch.kiostixv3.datastatic.Countries;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.holder.SpinerHolder;
import io.fabric.sdk.android.Fabric;

public class EditCustomerActivity extends AppCompatActivity {

    public static final String TAG = "EditCustomerActivity";

    private EditextStandardBlack edtx_name_00;
    private EditextStandardBlack edtx_email_00;
    private EditextStandardBlack edtx_phone_00;
    private EditextStandardBlack edtx_id_00;
    private EditextStandardBlack edtx_zipcode_00;
    private SimpleSpinnerWthBorder smpr_gender_00;
    private SimpleSpinnerWthBorder smpr_idtype_00;
    private EditextStandardBlack edtx_pob_00;
    private MyDatePickerWthBorder dtpk_dob_00;
    private EditextAutoComplateBlack     edtx_country_00;
    private EditextAutoComplateBlack     edtx_province_00;
    private EditextAutoComplateBlack     edtx_city_00;
    private TextAreaStandardBlack        edtx_address_00;
    private Button               bbtn_login_00;
    private ImageView            imvw_back_00;

    private CustomerDB customer = new CustomerDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_edit_customer);

        edtx_name_00        = (EditextStandardBlack) findViewById(R.id.edtx_name_00);
        edtx_email_00       = (EditextStandardBlack) findViewById(R.id.edtx_email_00);
        edtx_phone_00       = (EditextStandardBlack) findViewById(R.id.edtx_phone_00);
        edtx_id_00          = (EditextStandardBlack) findViewById(R.id.edtx_id_00);
        edtx_pob_00         = (EditextStandardBlack) findViewById(R.id.edtx_pob_00);
        edtx_zipcode_00     = (EditextStandardBlack) findViewById(R.id.edtx_zipcode_00);
        bbtn_login_00       = (Button)           findViewById(R.id.bbtn_login_00);
        imvw_back_00        = (ImageView)        findViewById(R.id.imvw_back_00);
        smpr_gender_00      = (SimpleSpinnerWthBorder) findViewById(R.id.smpr_gender_00);
        smpr_idtype_00      = (SimpleSpinnerWthBorder) findViewById(R.id.smpr_idtype_00);
        dtpk_dob_00         = (MyDatePickerWthBorder)         findViewById(R.id.dtpk_dob_00);
        edtx_country_00     = (EditextAutoComplateBlack)findViewById(R.id.edtx_country_00);
        edtx_province_00    = (EditextAutoComplateBlack)findViewById(R.id.edtx_province_00);
        edtx_city_00        = (EditextAutoComplateBlack)findViewById(R.id.edtx_city_00);
        edtx_address_00     = (TextAreaStandardBlack)   findViewById(R.id.edtx_address_00);

        customer.getData(this);

        setupComponent();
    }

    private void setupComponent(){

        edtx_email_00.setTitle(getResources().getString(R.string.email));
        edtx_email_00.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtx_email_00.setValue(customer.email);
        edtx_email_00.setReadOnly(true);
        edtx_email_00.setVisibility(View.GONE);

        edtx_name_00.setTitle(getResources().getString(R.string.full_name));
        edtx_name_00.setValue(customer.name);

        edtx_phone_00.setTitle(getResources().getString(R.string.handphone));
        edtx_phone_00.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_phone_00.setValue(customer.phone);

        edtx_id_00.setTitle(getResources().getString(R.string.no_identity));
        edtx_id_00.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtx_id_00.setValue(customer.identity);

        smpr_gender_00.setTitle(getResources().getString(R.string.gender));
        ArrayList<SpinerHolder> gender = new ArrayList<>();
        gender.add(new SpinerHolder("1", "Laki-Laki", 0));
        gender.add(new SpinerHolder("2", "Perempuan", 0));
        smpr_gender_00.setChoosers(gender);
        smpr_gender_00.setValue(customer.gender);

        edtx_pob_00.setTitle(getResources().getString(R.string.pob));
        edtx_pob_00.setValue(customer.pob);

        dtpk_dob_00.setTitle(getResources().getString(R.string.dob));
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            dtpk_dob_00.setValue(dateFormat.parse(customer.dob));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        smpr_idtype_00.setTitle(getResources().getString(R.string.id_type));
        ArrayList<SpinerHolder> id_type = new ArrayList<>();
        id_type.add(new SpinerHolder("KTP", "KTP", 0));
        id_type.add(new SpinerHolder("SIM", "SIM", 0));
        id_type.add(new SpinerHolder("PASSPORT", "PASSPORT", 0));
        smpr_idtype_00.setChoosers(id_type);
        smpr_idtype_00.setValue(customer.id_type);

        edtx_country_00.setTitle(getResources().getString(R.string.country));

        edtx_province_00.setTitle(getResources().getString(R.string.province));
        edtx_province_00.setValue(customer.province);

        edtx_city_00.setTitle(getResources().getString(R.string.city));
        edtx_city_00.setValue(customer.city);

        edtx_address_00.setTitle(getResources().getString(R.string.address));
        edtx_address_00.setValue(customer.address);

        edtx_zipcode_00.setTitle(getResources().getString(R.string.zip_code));
        edtx_zipcode_00.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtx_zipcode_00.setValue(customer.zip_code);

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

        edtx_country_00.setOnChangeListener(new EditextAutoComplateBlack.OnChangeListener() {
            @Override
            public void onchange(KeyValueHolder data) {
                requestProvince(data.getKey());
            }
        });

        edtx_province_00.setOnChangeListener(new EditextAutoComplateBlack.OnChangeListener() {
            @Override
            public void onchange(KeyValueHolder data) {
                requestCity(data.getKey());
            }
        });

        requestCountry();
    }

    private void requestCountry(){
        PostManager post = new PostManager(this);
        post.setApiUrl("country");
        post.execute("GET");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONArray data = obj.getJSONArray("data");
                        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                        for (int i=0; i< data.length(); i++){
                            JSONObject country = data.getJSONObject(i);
                            kvs.add(new KeyValueHolder(country.getString("id"), country.getString("name")));

                        }
                        edtx_country_00.setData(kvs);
                        edtx_country_00.setValue(customer.country);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void requestProvince(String country){
        Log.d("TAGRZ","Country ID "+ country);
        PostManager post = new PostManager(this);
        post.setApiUrl("province");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("country_id",country));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONArray data = obj.getJSONArray("data");
                        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                        for (int i=0; i< data.length(); i++){
                            JSONObject country = data.getJSONObject(i);
                            kvs.add(new KeyValueHolder(country.getString("id"), country.getString("name")));
                        }
                        edtx_province_00.setData(kvs);
                        edtx_province_00.setValue(customer.province);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void requestCity(String city){
        Log.d("TAGRZ","Country ID "+ city);
        PostManager post = new PostManager(this);
        post.setApiUrl("city");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("province_id",city));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONArray data = obj.getJSONArray("data");
                        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
                        for (int i=0; i< data.length(); i++){
                            JSONObject country = data.getJSONObject(i);
                            kvs.add(new KeyValueHolder(country.getString("id"), country.getString("name")));
                        }
                        edtx_city_00.setData(kvs);
                        edtx_city_00.setValue(customer.city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendApi(){
        String email    = edtx_email_00.getValue();
        String phone    = edtx_phone_00.getValue();
        String name     = edtx_name_00.getValue();
        String gender   = smpr_gender_00.getValue().key;
        String pob      = edtx_pob_00.getValue();
        String dob      = dtpk_dob_00.getValue();
        String id_type  = smpr_idtype_00.getValue().key;
        String identity = edtx_id_00.getValue();
        String country  = edtx_country_00.getKey();
        String province = edtx_province_00.getKey();
        String city     = edtx_city_00.getKey();
        String address  = edtx_address_00.getValue();
        String zip_code = edtx_zipcode_00.getValue();

        Log.d(TAG,"DATA : "+ country+" | "+ province+" | "+ city);

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
        else if (identity.isEmpty()){
            edtx_id_00.showNotif(getResources().getString(R.string.no_identity)+" Required ");
            return;
        }



        customer.phone      = phone;
        customer.email      = email;
        customer.identity   = identity;
        customer.name       = name;
        customer.gender     = gender;
        customer.pob        = pob;
        customer.dob        = dob;
        customer.id_type    = id_type;
        customer.country    = edtx_country_00.getValue();
        customer.province   = edtx_province_00.getValue();
        customer.city       = edtx_city_00.getValue();
        customer.address    = address;
        customer.zip_code   = zip_code;

        PostManager post = new PostManager(this);
        post.setApiUrl("user/update");
        JSONObject datasend = new JSONObject();
        try {
            datasend.put("token",customer.token);
            datasend.put("fullname",customer.name);
            datasend.put("dob",customer.dob);
            datasend.put("pob",customer.pob);
            datasend.put("phone",customer.phone);
            datasend.put("id_type",customer.id_type);
            datasend.put("id_number",customer.identity);
            datasend.put("gender",gender);
                JSONObject address_detail = new JSONObject();
                address_detail.put("country_id",country );
                address_detail.put("province_id",province );
                address_detail.put("city_id",city );
                address_detail.put("address",address );
                address_detail.put("zip_code",zip_code );
            datasend.put("address_detail",address_detail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(datasend);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        customer.clearData(EditCustomerActivity.this);
                        customer.insert(EditCustomerActivity.this);
                        Toast.makeText(EditCustomerActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        EditCustomerActivity.this.setResult(1001);
                        onBackPressed();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            Toast.makeText(EditCustomerActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(EditCustomerActivity.this, "Perubahan data gagal",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
