package g.rezza.moch.kiostixv3.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.DetailOrderView;
import g.rezza.moch.kiostixv3.connection.postmanager.PostManager;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.database.PaymentDB;
import g.rezza.moch.kiostixv3.datastatic.App;
import g.rezza.moch.kiostixv3.datastatic.ErrorCode;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.lib.LoadingScreen;
import g.rezza.moch.kiostixv3.view.fragment.payment.BankTransferFragment;
import g.rezza.moch.kiostixv3.view.fragment.payment.CreditCardFragment;

public class PaymentActivity extends AppCompatActivity implements TransactionFinishedCallback {
    private static final String TAG = "PaymentActivity";
    private static final int SEND_PAYMENT    = 1;
    private static final int GET_TOKEN       = 2;

    private ImageView   imvw_back_00;
    private Fragment    fragment = null;
    private FrameLayout container;
    private TextView    txvw_name_00;
    private EditText    edtx_vocher_00;
    private Button      bbtn_use_00;
    private Button      bbtn_action_00;
    private DetailOrderView dovw_detail_00;
    private BookingDB   mybooking;
    private PaymentDB   paymentDB;
    private FragmentTransaction fragmentTransaction;
    private CreditCardFragment creditCardFragment;

    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        imvw_back_00        = (ImageView)   findViewById(R.id.imvw_back_00);
        container           = (FrameLayout) findViewById(R.id.container);
        txvw_name_00        = (TextView)    findViewById(R.id.txvw_name_00);
        edtx_vocher_00      = (EditText)    findViewById(R.id.edtx_vocher_00);
        bbtn_use_00         = (Button)      findViewById(R.id.bbtn_use_00);
        bbtn_action_00      = (Button)      findViewById(R.id.bbtn_action_00);
        dovw_detail_00      = (DetailOrderView) findViewById(R.id.dovw_detail_00);

        mybooking = new BookingDB();
        mybooking.getData(this);

        final int type    = getIntent().getIntExtra("PAYMENT_CATEGORY",2);
        paymentDB   = new PaymentDB();
        paymentDB.getData(this, getIntent().getStringExtra("PAYMENT_ID"));
        create(type);
        txvw_name_00.setText(paymentDB.name);

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtx_vocher_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String data = editable.toString();
                if (data.isEmpty()){
                    bbtn_use_00.setBackgroundResource(R.drawable.button_grey_line);
                    bbtn_use_00.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    bbtn_use_00.setBackgroundResource(R.drawable.button_orange_line);
                    bbtn_use_00.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    handler.sendEmptyMessage(GET_TOKEN);
                }
                else {
                    Message message = new Message();
                    message.what = SEND_PAYMENT;
                    Bundle bundle = new Bundle();
                    bundle.putString("TOKEN","-1");
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

            }
        });


        bbtn_use_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtx_vocher_00.getText().toString().isEmpty()){
                    checkVocher(edtx_vocher_00.getText().toString());
                }
            }
        });

        loadingScreen = new LoadingScreen(this);
        initMidtransSdk();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void create(int input){
        dovw_detail_00.setFee(paymentDB.fee_value);
        dovw_detail_00.create(mybooking.event_category);
        if (input == 1){
            creditCardFragment = (CreditCardFragment) CreditCardFragment.newInstance();
            fragmentTransaction.replace(container.getId(), creditCardFragment,"1");
            fragmentTransaction.commit();
        }
        else {
            fragment = BankTransferFragment.newInstance(paymentDB.id);
            fragmentTransaction.replace(container.getId(), fragment,"2");
            fragmentTransaction.commit();
        }
    }

    private void checkVocher(String code){
        PostManager post = new PostManager(this);
        post.setApiUrl("transaction/voucher");
            ArrayList<KeyValueHolder> kvs = new ArrayList<>();
            kvs.add(new KeyValueHolder("voucher_code",code));
            kvs.add(new KeyValueHolder("order_no",mybooking.order_id));
            kvs.add(new KeyValueHolder("payment_id",paymentDB.id));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    try {
                        JSONObject data         = obj.getJSONArray("data").getJSONObject(0);
                        dovw_detail_00.setDiscount(data.getString("discount"), data.getString("total_discount"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    dovw_detail_00.setDiscount("0 %", "0");
                    try {
                        String message = obj.getString("message");
                        Toast.makeText(PaymentActivity.this, message,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(PaymentActivity.this, "Internal Error",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void sendPayment(String token){
        Calendar calendar = Calendar.getInstance();
        DateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = dtformat.parse(mybooking.expired_date);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().after(calendar.getTime())){
            Toast.makeText(this,getResources().getString(R.string.sorry_desc),Toast.LENGTH_LONG ).show();
            PaymentActivity.this.finish();
            return;
        }

        PostManager post = new PostManager(this);
        post.setApiUrl("transaction/pay");
        JSONObject data = new JSONObject();
        try {
            data.put("order_no",mybooking.order_id);
            JSONObject payment = new JSONObject();
            payment.put("id", paymentDB.id);
            payment.put("token", token);
            data.put("payment",payment);
            data.put("voucher_code",edtx_vocher_00.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.setData(data);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.SUCCSESS){
                    mybooking.payment_id = paymentDB.id;
                    String message = null;
                    try {
                        JSONObject data         = obj.getJSONArray("data").getJSONObject(0);
                        mybooking.trans_status  = data.getString("status");

                        String payment_code     = data.getString("payment_code");
                        String va_number        = data.getString("va_number");
                        String expired_date     = data.getString("expired_date");
                        if (!va_number.isEmpty()){
                            mybooking.virtual_account  = va_number;
                        }
                        else if (!payment_code.isEmpty()){
                            mybooking.virtual_account  = payment_code;
                        }
                        mybooking.expired_date  = expired_date;

                        message = obj.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mybooking.insert(PaymentActivity.this);
                    Toast.makeText(PaymentActivity.this,message,Toast.LENGTH_LONG ).show();
                    Intent intent = new Intent(PaymentActivity.this, FInishActivity.class);
                    PaymentActivity.this.startActivity(intent);
                    SummaryActivity.summaryActivity.finish();
                    PaymentActivity.this.finish();
                }
                else {
                    try {
                        String message = obj.getString("message");
                        Toast.makeText(PaymentActivity.this,message,Toast.LENGTH_LONG ).show();
                    } catch (JSONException e) {
                        Toast.makeText(PaymentActivity.this,"System Error",Toast.LENGTH_LONG ).show();
                        e.printStackTrace();
                    }

                    PaymentDB paymentDB = new PaymentDB();
                    paymentDB.clearData(PaymentActivity.this);

                    Intent intent = new Intent(PaymentActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    PaymentActivity.this.finish();
                    SummaryActivity.summaryActivity.finish();

                }
            }
        });
    }

    private void initMidtransSdk() {
        String client_key = App.MID_CLIENT_KEY;
        String base_url = App.MID_BASE_URL;

        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
    }

    private void getToken(){
        JSONObject data = creditCardFragment.getData();
        Log.d(TAG, "DATA CARD : "+ data);
        try {
            if (data.getString("ERROR_CODE").equals("00")){
                loadingScreen.show();
                CardTokenRequest card = new CardTokenRequest();
                card.setCardCVV(data.getString("CVV"));
                card.setCardExpiryMonth(data.getString("MONTH"));
                card.setCardExpiryYear(data.getString("YEAR"));
                card.setCardNumber(data.getString("CARD_NUMBER"));
                card.setIsSaved(false);
                card.setClientKey(App.MID_CLIENT_KEY);
                String amount = dovw_detail_00.getTotal();
                card.setGrossAmount(Double.parseDouble(amount));
                MidtransSDK.getInstance().getCardToken(card, new CardTokenCallback() {
                    @Override
                    public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {
                        String token = tokenDetailsResponse.getTokenId();
                        Message message = new Message();
                        message.what = SEND_PAYMENT;
                            Bundle bundle = new Bundle();
                            bundle.putString("TOKEN",token);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        loadingScreen.dimiss();
                    }

                    @Override
                    public void onFailure(TokenDetailsResponse tokenDetailsResponse, String s) {
                        Log.d(TAG,"onFailure "+ s);
                        loadingScreen.dimiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d(TAG,"onError "+throwable.getMessage());
                        loadingScreen.dimiss();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == SEND_PAYMENT){
                Bundle bundle = message.getData();
                sendPayment(bundle.getString("TOKEN"));
            }
            else if (message.what == GET_TOKEN){
                getToken();
            }
            return false;
        }
    });
}
