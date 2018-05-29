package g.rezza.moch.nfcscanner.nfc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.nfcscanner.R;
import g.rezza.moch.nfcscanner.nfc.holder.KeyValueHolder;
import g.rezza.moch.nfcscanner.nfc.lib.LoadingScreen;
import g.rezza.moch.nfcscanner.nfc.lib.LongRunningGetIO;

public class SummaryActivity extends AppCompatActivity {

    String code = "";
    private TextView txvw_order_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        txvw_order_00 = (TextView) findViewById(R.id.txvw_order_00);
    }

    @Override
    protected void onStart() {
        super.onStart();
        code = getIntent().getStringExtra("CODE");
        checkData();
    }

    public void checkData(){
        final LoadingScreen loading = new LoadingScreen(this);
        LongRunningGetIO mClient =  new LongRunningGetIO(this);
//        mClient.setUrl("http://apidev.kiostix.com/kiostix/order/check");
        mClient.setUrl("https://api.kiostix.com/kiostix/order/check");
        mClient.addParam(new KeyValueHolder("token","3550ce1cde81d3e6eb2c175506ffbe96"));
        mClient.addParam(new KeyValueHolder("order_key", code));
        mClient.addParam(new KeyValueHolder("event_id[0]", "pz9k77qjt4cl"));
        mClient.addParam(new KeyValueHolder("event_id[1]", "pz9k77qjt4cl"));
        mClient.addParam(new KeyValueHolder("event_id[2]", "p523n7qjt49l"));

        loading.show();
        mClient.execute();

        mClient.setOnReceiveListener(new LongRunningGetIO.onReceiveListener() {
            @Override
            public void onReceive(Object obj) {
                String data = (String) obj;
                loading.dimiss();
                if (data == null){
                    Toast.makeText(SummaryActivity.this,"Cannot connect to server !", Toast.LENGTH_LONG).show();

                    return;
                }
                try {
                    JSONObject jResponse = new JSONObject(data);
                    JSONArray jaData = jResponse.getJSONArray("data");
                    int status = Integer.parseInt(jResponse.get("status").toString()) ;
                    String description = jResponse.get("description").toString() ;

                    if (status == 1000){
                        String name  = (String) jaData.getJSONObject(0).get("customer_firstname");
                        String phone  = (String) jaData.getJSONObject(0).get("customer_phone");
                        String mail  = (String) jaData.getJSONObject(0).get("customer_email");
                        String orderno  = (String) jaData.getJSONObject(0).get("order_no");
                        String city  = (String) jaData.getJSONObject(0).get("customer_city");
                        String gender  = (String) jaData.getJSONObject(0).get("customer_gender");
                        String customer_birthday  = (String) jaData.getJSONObject(0).get("customer_birthday");
                        String order_status  = (String) jaData.getJSONObject(0).get("order_status");
                        String status_order  = (String) jaData.getJSONObject(0).get("order_status");

                        StringBuilder sb = new StringBuilder();
                        sb.append("ORDER NO : ").append(orderno).append("\n");
                        sb.append("ORDER STATUS : ").append(order_status).append("\n");
                        sb.append("NAME : ").append(name).append("\n");
                        sb.append("PHONE : ").append(phone).append("\n");
                        sb.append("EMAIL : ").append(mail).append("\n");
                        sb.append("CITY : ").append(city).append("\n");
                        sb.append("GENDER : ").append(gender).append("\n");

                        txvw_order_00.setText(sb.toString());

                    }
                    else {
                        txvw_order_00.setText("");
                        Toast.makeText(SummaryActivity.this, description, Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    Log.e("TAGRZ", e.getMessage());
                }
            }
        });

    }

}
