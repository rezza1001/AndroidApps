package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.holder.BillingListHolder;
import g.rezza.moch.mobilesales.holder.BillingListHolder;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class BillingAdapter extends ArrayAdapter<BillingListHolder> {
    public static final String TAG = "BillingAdapter";

    private ArrayList<BillingListHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_billing;
    public Resources r;

    public BillingAdapter(@NonNull Context context, ArrayList<BillingListHolder> pElement) {
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
            TextView txvw_code_00         = (TextView) 	    view.findViewById(R.id.txvw_code_00);
            TextView txvw_status_00       = (TextView) 	    view.findViewById(R.id.txvw_status_00);
            RelativeLayout rvly_sales_00  = (RelativeLayout)view.findViewById(R.id.rvly_sales_00);

            final BillingListHolder holder = holders.get(position);
            if (holder != null){
                txvw_header_00.setText(holder.title);
                txvw_date_00.setText(holder.date);
                txvw_code_00.setText(holder.code);
                txvw_status_00.setText(holder.status_desc);

                if (holder.status == 8){
                    rvly_sales_00.setBackgroundResource(R.drawable.adapter_store_orange);
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
        public void selectedItem(BillingListHolder holder, int position);
    }
}
