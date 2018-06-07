package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 28/02/18.
 */

public class DetailOrderAdapter extends RelativeLayout {
    private static final String TAG = "DetailOrderAdapter";

    private TextView txvw_title_00;
    private TextView txvw_venue_00;
    private TextView txvw_price_00;
    private TextView txvw_category_00;
    private TextView txvw_qty_00;
    private ImageView imvw_close_00;

    private String price    = "0";
    private String qty      = "0";

    public DetailOrderAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_detail_order, this, true);

        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        txvw_venue_00   = (TextView) findViewById(R.id.txvw_venue_00);
        txvw_price_00   = (TextView) findViewById(R.id.txvw_price_00);
        txvw_category_00 = (TextView) findViewById(R.id.txvw_category_00);
        txvw_qty_00     = (TextView) findViewById(R.id.txvw_qty_00);
        imvw_close_00   = (ImageView) findViewById(R.id.imvw_close_00);
    }

    public void create(final String id, String title, String venue, String price, String category, int qty){
        this.price = price;
        this.qty   = qty +"";
        txvw_title_00.setText(title);
        txvw_venue_00.setText(venue);
        txvw_price_00.setText(Parse.toCurrnecy(price));
        txvw_category_00.setText("Cat : " + category);
        txvw_qty_00.setText("X "+ qty);

        imvw_close_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onCancel(id, DetailOrderAdapter.this);
                }
            }
        });
    }

    public void setReadOnly(){
        imvw_close_00.setVisibility(GONE);
    }

    public String getPrice(){
        return price;
    }

    public String getQty() {
        return qty;
    }

    private OnCancelListener mListener;
    public void setOnCancelListener(OnCancelListener onCancelListener){
        mListener = onCancelListener;
    }
    public interface OnCancelListener{
        public void onCancel(String id, View v);
    }
}
