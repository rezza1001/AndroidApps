package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 23/11/17.
 */

public class CategoryQty extends RelativeLayout {

    private TextView    txvw_category_30;
    private TextView    txvw_qty_32;
    private TextView    txvw_price_31;
    private TextView    bbtn_minus_40;
    private TextView    bbtn_plus_41;
    private String      mID;
    private String      mPrice = "0";
    private String      mTotal = "0";

    public CategoryQty(Context context) {
        super(context);
    }

    public CategoryQty(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_category_qty, this, true);

        txvw_category_30    = (TextView)    findViewById(R.id.txvw_category_30);
        txvw_price_31       = (TextView)    findViewById(R.id.txvw_price_31);
        txvw_qty_32         = (TextView)    findViewById(R.id.txvw_qty_32);
        bbtn_minus_40       = (TextView)    findViewById(R.id.bbtn_minus_40);
        bbtn_plus_41        = (TextView)    findViewById(R.id.bbtn_plus_41);
    }

    public void create(String pID, String pTitle, String pPrice, int pDefault, final int pMax){
        mID = pID;
        mPrice = pPrice.equals("null")? "0": pPrice;
        txvw_category_30.setText(pTitle);
        txvw_price_31.setText("IDR " + Parse.toCurrnecy(pPrice.equals("null")? "0": pPrice));
        txvw_qty_32.setText(pDefault+"");

        final Long price = Long.parseLong(pPrice.equals("null")? "0": pPrice);

        bbtn_minus_40.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(txvw_qty_32.getText().toString());
                if (value != 0){
                    value --;
                    mTotal = (value * price) + "";
                    txvw_qty_32.setText(value +"");
                    if (mListener != null){
                        mListener.onMinus(value, price);
                    }
                }
            }
        });

        bbtn_plus_41.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(txvw_qty_32.getText().toString());
                if (value < pMax){
                    value ++;
                    mTotal = (value * price) + "";
                    txvw_qty_32.setText(value +"");
                    if (mListener != null){
                        mListener.onPlus(value, price);
                    }
                }
            }
        });
    }

    public String getPrice() {
        return mPrice;
    }

    public String getTotal() {
        return mTotal;
    }

    public String getTitle(){
        return txvw_category_30.getText().toString();
    }

    public KeyValueHolder getValue(){
        return new KeyValueHolder(mID,txvw_qty_32.getText().toString());
    }

    public int getValueQty(){
        return  Integer.parseInt(txvw_qty_32.getText().toString());
    }

    private OnActionListener mListener;
    public void setOnActionListener(OnActionListener onActionListener){
        mListener = onActionListener;
    }
    public interface OnActionListener{
        public void onPlus(int qty, long value);
        public void onMinus(int qty, long value);
    }
}
