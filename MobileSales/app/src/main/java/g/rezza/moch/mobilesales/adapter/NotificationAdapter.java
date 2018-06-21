package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.NotificationHolder;
import g.rezza.moch.mobilesales.lib.Parse;


/**
 * Created by Rezza on 9/22/17.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationHolder> {
    public static final String TAG = "NotificationAdapter";

    private ArrayList<NotificationHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_notification;

    public NotificationAdapter(@NonNull Context context, ArrayList<NotificationHolder> pElement) {
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

            TextView txvw_who_00        = (TextView) 	view.findViewById(R.id.txvw_who_00);
            TextView txvw_what_00       = (TextView) 	view.findViewById(R.id.txvw_what_00);
            TextView txvw_when_00       = (TextView) 	view.findViewById(R.id.txvw_when_00);
            TextView txvw_owner_00      = (TextView) 	view.findViewById(R.id.txvw_owner_00);
            RelativeLayout
                     rvly_store_00      = (RelativeLayout)
                                                        view.findViewById(R.id.rvly_store_00);
            ImageView imvw_status_00    = (ImageView)   view.findViewById(R.id.imvw_status_00);
            imvw_status_00.bringToFront();
            imvw_status_00.setVisibility(View.GONE);

            final NotificationHolder holder = holders.get(position);
            if (holder != null){
                double nominal = Double.parseDouble(holder.nominal);
                txvw_who_00.setText(holder.who.equalsIgnoreCase("NULL")? "Sales": holder.who);
                txvw_owner_00.setText(holder.account_name);
                txvw_what_00.setText(TextUtils.concat(holder.what, " ",
                        Parse.BoldText(Parse.toCurrnecy(nominal))));
                txvw_when_00.setText(holder.when);
                if (holder.status == holder.IN_PROGRESS){
                    imvw_status_00.setImageResource(R.drawable.ic_in);
                    rvly_store_00.setBackgroundResource(R.drawable.adapter_store_orange);
                }
                else if (holder.status == holder.REJECT){
                    imvw_status_00.setImageResource(R.drawable.ic_out);
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
        public void selectedItem(NotificationHolder holder, int position);
    }
}
