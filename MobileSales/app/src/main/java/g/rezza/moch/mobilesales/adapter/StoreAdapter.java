package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
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
import g.rezza.moch.mobilesales.holder.CompanyInfoHolder;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class StoreAdapter extends ArrayAdapter<CompanyInfoHolder> {
    public static final String TAG = "RZ StoreAdapter";

    private ArrayList<CompanyInfoHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_store;

    public StoreAdapter(@NonNull Context context, ArrayList<CompanyInfoHolder> pElement) {
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

            TextView txvw_name_00       = (TextView) 	view.findViewById(R.id.txvw_name_00);
            TextView txvw_balance_00    = (TextView) 	view.findViewById(R.id.txvw_balance_00);
            TextView txvw_title_00      = (TextView) 	view.findViewById(R.id.txvw_title_00);
            TextView txvw_sales_00      = (TextView) 	view.findViewById(R.id.txvw_sales_00);
            RelativeLayout
                     rvly_store_00      = (RelativeLayout)
                                                        view.findViewById(R.id.rvly_store_00);

            final CompanyInfoHolder holder = holders.get(position);
            if (holder != null){
                txvw_name_00.setText(holder.name);
                txvw_balance_00.setText(" " + Parse.toCurrnecy(holder.total_transaction));
                txvw_sales_00.setText(" Rp. "+ Parse.toCurrnecy(holder.total_sales));

                int xposition = position + 1;

                if (xposition % 2 == 0 && position != 0){
                    rvly_store_00.setBackgroundResource(R.drawable.adapter_store_orange);
                }
                else if (xposition % 3 == 0){
                    rvly_store_00.setBackgroundResource(R.drawable.adapter_store_blue);
                }
                else if (xposition % 5 == 0){
                    rvly_store_00.setBackgroundResource(R.drawable.adapter_store_green);
                }
                else {
                    rvly_store_00.setBackgroundResource(R.drawable.adapter_store_yellow);
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
        public void selectedItem(CompanyInfoHolder holder, int position);
    }
}
