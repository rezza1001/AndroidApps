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
import g.rezza.moch.mobilesales.holder.ReportList;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class ReportAdapter extends ArrayAdapter<ReportList> {
    public static final String TAG = "BillingAdapter";

    private ArrayList<ReportList> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_report;
    public Resources r;

    public ReportAdapter(@NonNull Context context, ArrayList<ReportList> pElement) {
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

            TextView txvw_tarnsid_00       = (TextView) 	    view.findViewById(R.id.txvw_tarnsid_00);
            TextView txvw_status_00       = (TextView) 	    view.findViewById(R.id.txvw_status_00);
            TextView txvw_date_00         = (TextView) 	    view.findViewById(R.id.txvw_date_00);
            TextView txvw_time_00         = (TextView) 	    view.findViewById(R.id.txvw_time_00);
            TextView txvw_amount_00         = (TextView) 	    view.findViewById(R.id.txvw_amount_00);


            final ReportList holder = holders.get(position);
            if (holder != null){
                txvw_tarnsid_00.setText(holder.order_no);
                txvw_status_00.setText(holder.status_desc);
                txvw_amount_00.setText(Parse.toCurrnecy(holder.amount));
//                txvw_date_00.setText(holder.date);
//                txvw_time_00.setText();
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
        public void selectedItem(ReportList holder, int position);
    }
}
