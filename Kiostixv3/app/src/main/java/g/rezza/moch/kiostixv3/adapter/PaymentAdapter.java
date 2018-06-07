package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;

/**
 * Created by rezza on 28/02/18.
 */

public class PaymentAdapter extends RelativeLayout {
    private static final String TAG = "PaymentAdapter";

    private TextView    txvw_title_00;
    private ImageView   imvw_icon_00;
    private ImageView   imvw_status_00;
    private RelativeLayout rvly_sparator_00;
    private RelativeLayout rvly_sparator_01;
    private String id = "";
    private String payment_code = "";
    private float fee = 0;
    private boolean isSelected = false;

    public PaymentAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_payment, this, true);

        txvw_title_00    = (TextView)    findViewById(R.id.txvw_title_00);
        imvw_icon_00     = (ImageView)   findViewById(R.id.imvw_icon_00);
        imvw_status_00   = (ImageView)   findViewById(R.id.imvw_status_00);
        rvly_sparator_00 = (RelativeLayout) findViewById(R.id.rvly_sparator_00);
        rvly_sparator_01 = (RelativeLayout) findViewById(R.id.rvly_sparator_01);
    }

    public void setSelected(){
        isSelected = true;
        imvw_status_00.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected));
    }

    public void unSelected(){
        isSelected = false;
        imvw_status_00.setImageDrawable(getResources().getDrawable(R.mipmap.ic_next_grey));
    }

    public void setFee(Float fee){
        this.fee = fee;
    }

    public Float getFee(){
        return this.fee;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public boolean isSelected(){
        return  isSelected;
    }

    public void create(String code,String title, Drawable icon){
        this.id = code;
        this.payment_code = code;
        txvw_title_00.setText(title);
        imvw_icon_00.setImageDrawable(icon);
    }

    public void create(String code,String title, String imageurl){
        this.id = code;
        this.payment_code = code;
        txvw_title_00.setText(title);
        Glide.with(getContext()).load(imageurl).into(imvw_icon_00);
    }

    public KeyValueHolder getData(){
       return  new KeyValueHolder(this.id,txvw_title_00.getText().toString());
    }

    public void hideSparatorBottom(){
        rvly_sparator_00.setVisibility(GONE);
    }
    public void hideSparatorTop(){
        rvly_sparator_01.setVisibility(GONE);
    }

}
