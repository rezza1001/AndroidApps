package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.BillingLisDtltHolder;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class BillingDetailAdapter extends ArrayAdapter<BillingLisDtltHolder> {
    public static final String TAG = "BillingAdapter";

    private ArrayList<BillingLisDtltHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_billing_detail;
    public Resources r;

    public BillingDetailAdapter(@NonNull Context context, ArrayList<BillingLisDtltHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
        r = context.getResources();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null){
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }

            TextView txvw_header_00       = (TextView) 	    view.findViewById(R.id.txvw_header_00);
            TextView txvw_date_00         = (TextView) 	    view.findViewById(R.id.txvw_date_00);
            TextView txvw_initial_01      = (TextView) 	    view.findViewById(R.id.txvw_initial_01);
            TextView txvw_amount_01       = (TextView) 	    view.findViewById(R.id.txvw_amount_01);
            TextView txvw_total_01        = (TextView) 	    view.findViewById(R.id.txvw_total_01);
            RelativeLayout rvly_sales_00  = (RelativeLayout)view.findViewById(R.id.rvly_sales_00);

            final BillingLisDtltHolder holder = holders.get(position);
            if (holder != null){
                txvw_header_00.setText(holder.title);
                txvw_date_00.setText(holder.date);
                txvw_initial_01.setText("Rp. "+ Parse.toCurrnecy(holder.initial_bill));
                txvw_amount_01.setText("Rp. "+ Parse.toCurrnecy(holder.amount));
                txvw_total_01.setText("Rp. "+ Parse.toCurrnecy(holder.final_bill));

                if (holder.type.equals("Credit")){
                    rvly_sales_00.setBackgroundResource(R.drawable.adapter_store_orange);
                }
                else {
                    rvly_sales_00.setBackgroundResource(R.drawable.adapter_store_green);
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSelectedItemListener != null){
                        mOnSelectedItemListener.selectedItem(holder, position);
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

    /*
     * Register Listener
     */

    private OnSelectedItemListener mOnSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        mOnSelectedItemListener = pOnSelectedItemListener;
    }

    /*
     * Interface Listener
     */

    public interface OnSelectedItemListener{
        public void selectedItem(BillingLisDtltHolder holder, int position);
    }
}
