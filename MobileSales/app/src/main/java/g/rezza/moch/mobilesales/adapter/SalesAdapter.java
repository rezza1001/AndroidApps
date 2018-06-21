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
import g.rezza.moch.mobilesales.holder.SalesListHolder;
import g.rezza.moch.mobilesales.holder.SalesListHolder;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class SalesAdapter extends ArrayAdapter<SalesListHolder> {
    public static final String TAG = "SalesAdapter";

    private ArrayList<SalesListHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_sales;
    public Resources r;

    public SalesAdapter(@NonNull Context context, ArrayList<SalesListHolder> pElement) {
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

            TextView txvw_who_00          = (TextView) 	    view.findViewById(R.id.txvw_who_00);
            FieldTransDtl ftdl_balance_00 = (FieldTransDtl) view.findViewById(R.id.ftdl_balance_00) ;
            FieldTransDtl ftdl_sales_00   = (FieldTransDtl) view.findViewById(R.id.ftdl_sales_00) ;
            RelativeLayout rvly_sales_00  = (RelativeLayout)view.findViewById(R.id.rvly_sales_00);

            final SalesListHolder holder = holders.get(position);
            if (holder != null){
                txvw_who_00.setText(holder.name);
                ftdl_balance_00.setTitle(r.getString(R.string.balance));
                ftdl_sales_00.setTitle(r.getString(R.string.total_sales));

                ftdl_balance_00.setValue(TextUtils.concat("Rp.",Parse.toCurrnecy(holder.balance)));
                ftdl_sales_00.setValue(TextUtils.concat("Rp. ",Parse.toCurrnecy(holder.total_sales)));
                if ((position+1) % 2 == 0){
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
        public void selectedItem(SalesListHolder holder, int position);
    }
}
