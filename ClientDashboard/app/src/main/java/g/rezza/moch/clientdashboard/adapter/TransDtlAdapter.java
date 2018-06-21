package g.rezza.moch.clientdashboard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.holder.TransDtlHolder;

/**
 * Created by Rezza on 9/22/17.
 */

public class TransDtlAdapter extends ArrayAdapter<TransDtlHolder> {
    public static final String TAG = "RZ TransDtlAdapter";

    private ArrayList<TransDtlHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_transdtl;

    public TransDtlAdapter(@NonNull Context context, ArrayList<TransDtlHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null){
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }

            TextView txvw_orderno_00            = (TextView) 	view.findViewById(R.id.txvw_orderno_00);
            TextView txvw_payment_method_02     = (TextView) 	view.findViewById(R.id.txvw_payment_method_02);
            TextView txvw_sales_channel_02      = (TextView) 	view.findViewById(R.id.txvw_sales_channel_02);
            TextView txvw_order_value_02        = (TextView) 	view.findViewById(R.id.txvw_order_value_02);
            TextView txvw_created_date_01       = (TextView) 	view.findViewById(R.id.txvw_created_date_01);
            TextView txvw_qty_01                = (TextView) 	view.findViewById(R.id.txvw_qty_01);

            final TransDtlHolder holder = holders.get(position);
            if (holder != null){
                txvw_orderno_00.setText(holder.getOrderno());
                txvw_payment_method_02.setText(holder.getPaymentMethod());
                txvw_sales_channel_02.setText(holder.getSalesChannel());
                txvw_order_value_02.setText(holder.getOrderValue());
                txvw_created_date_01.setText(holder.getCreatedDate());
                txvw_qty_01.setText(holder.getQty());

                if (position % 2 == 0){
                    view.setBackgroundResource(R.color.colorWhite_03);
                }
                else {
                    view.setBackgroundResource(R.color.colorWhite_02);
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
        public void selectedItem(TransDtlHolder holder, int position);
    }
}
