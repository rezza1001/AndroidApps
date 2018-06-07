package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.core.PaymentMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.PaymentAdapter;
import g.rezza.moch.kiostixv3.database.PaymentDB;

/**
 * Created by rezza on 28/02/18.
 */

public class paymentMethod extends RelativeLayout  implements View.OnClickListener{
    private static final String TAG = "paymentMethod";


    private LinearLayout    lnly_list_00;
    private PaymentAdapter pSelect = null;

    public paymentMethod(Context context){
        super(context, null);
    }

    public paymentMethod(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_payment_method, this, true);

        lnly_list_00 = (LinearLayout) findViewById(R.id.lnly_list_00);
    }

    public void create(){
        lnly_list_00.removeAllViews();
        PaymentDB paymentDB = new PaymentDB();
        ArrayList<PaymentDB> data = paymentDB.getDatas(getContext());
        for (int i=0; i<data.size(); i++){
            PaymentAdapter payment = new PaymentAdapter(getContext(), null);
            payment.create(data.get(i).id, data.get(i).name,data.get(i).image_url);
            lnly_list_00.addView(payment);
            payment.hideSparatorTop();
            payment.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View view) {

        for (int i=0; i<lnly_list_00.getChildCount(); i++){
            PaymentAdapter adapter = (PaymentAdapter) lnly_list_00.getChildAt(i);
//            adapter.unSelected();
            if (view == adapter){
                pSelect = adapter;
            }
        }
//        pSelect.setSelected();
        if (pListener != null){
            pListener.onClick(getData());
        }
    }

    public JSONObject getData(){
        if (pSelect == null){
            return null;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("ID",pSelect.getData().getKey());
            data.put("NAME",pSelect.getData().getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getID(){
        return pSelect.getData().getKey();
    }

    public String getCode(){
        return pSelect.getPayment_code();
    }

    private OnPaymentClickListener pListener;
    public void setOnPaymentClickListener(OnPaymentClickListener onPaymentClickListener){
        pListener = onPaymentClickListener;
    }

    public interface OnPaymentClickListener{
        public void onClick(JSONObject data);
    }
}
