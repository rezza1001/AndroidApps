package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiospos.lib.Converter;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.MyChooserQty;
import g.rezza.moch.kiospos.component.TextKeyValue;

/**
 * Created by Rezza on 10/13/17.
 */

public class ChooseQty extends MasterView implements View.OnClickListener{

    private TextView        texvw_selected_31;
    private TextKeyValue    txkv_orderdtl_40;
    private TextKeyValue    txkv_price_41;
    private MyChooserQty    cqty_qty_43;
    private Button          bbtn_next_31;
    private Button          bbtn_back_30;
    boolean isValidData     = false;

    public ChooseQty(Context context) {
        super(context);
    }

    public ChooseQty(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_choose_qty, this, true);

        texvw_selected_31   = (TextView)        findViewById(R.id.texvw_selected_31);
        txkv_orderdtl_40    = (TextKeyValue)    findViewById(R.id.txkv_orderdtl_40);
        txkv_price_41       = (TextKeyValue)    findViewById(R.id.txkv_price_41);
        cqty_qty_43         = (MyChooserQty)    findViewById(R.id.cqty_qty_43);
        bbtn_next_31        = (Button)          findViewById(R.id.bbtn_next_31);
        bbtn_back_30        = (Button)          findViewById(R.id.bbtn_back_30);

        bbtn_back_30.setOnClickListener(this);
        bbtn_next_31.setOnClickListener(this);
        cqty_qty_43.setMax(10);
        cqty_qty_43.setMin(0);
    }

    public void create(JSONObject jo){
        mData   = jo;
        disableLogo();

        try {
            JSONObject data = mData.getJSONObject("Category");
            texvw_selected_31.setText(data.getString("value"));
            txkv_orderdtl_40.create("ORDER DETAILS",data.getString("value"));
            txkv_price_41.create("PRICE PER TICKETS","IDR "+ Converter.toCurrnecy(data.getString("price")));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cqty_qty_43.setonActionListener(new MyChooserQty.ActionListener() {
            @Override
            public void onPlus(int qty) {
                isValidData = true;
                bbtn_next_31.setBackgroundResource(R.drawable.button_blue06_round_right);
            }

            @Override
            public void onMinus(int qty) {
                if (qty == 0){
                    isValidData = false;
                    bbtn_next_31.setBackgroundResource(R.drawable.button_blue06t30_round_right);
                }
                else {
                    isValidData = true;
                    bbtn_next_31.setBackgroundResource(R.drawable.button_blue06_round_right);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bbtn_next_31)){
            if (mFinishListener != null && isValidData){
                JSONObject jo = new JSONObject();
                try {
                    jo.put("quantity",cqty_qty_43.getValue());
                    mData.put("ChooseQty", jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mFinishListener.onNext(ChooseQty.this, mData);
            }
        }
        else {
            if (mFinishListener != null){
                mFinishListener.onBack(ChooseQty.this, mData);
            }
        }
    }
}
