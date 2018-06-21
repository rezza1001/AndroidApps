package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 03/01/18.
 */

public class PaymentAdapter extends RelativeLayout {

    private TextView txvw_name_00;
    private ImageView imvw_icon_00;
    private ImageView imvw_icon_01;

    public PaymentMethodeHolder mHolder ;
    public PaymentAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_payment_methode, this, true);

        imvw_icon_00 = (ImageView) findViewById(R.id.imvw_icon_00);
        imvw_icon_01 = (ImageView) findViewById(R.id.imvw_icon_01);
        txvw_name_00 = (TextView)  findViewById(R.id.txvw_name_00);
    }

    public void create(PaymentMethodeHolder holder){
        imvw_icon_00.setImageDrawable(holder.icon);
        txvw_name_00.setText(holder.name);
        if (holder.check){
            imvw_icon_01.setImageResource(R.drawable.ic_check_black);
        }
        else {
            imvw_icon_01.setImageResource(0);
        }
        mHolder = holder;
    }

    public static class PaymentMethodeHolder {
        public String   id;
        public String   name;
        public Drawable icon;
        public String category;
        public boolean check = false;
    }
}
