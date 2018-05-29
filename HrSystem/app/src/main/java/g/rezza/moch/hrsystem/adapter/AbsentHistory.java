package g.rezza.moch.hrsystem.adapter;

import android.content.Context;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.hrsystem.R;
import g.rezza.moch.hrsystem.holder.AbsentHistoryHolder;

/**
 * Created by rezza on 04/02/18.
 */

public class AbsentHistory extends ArrayAdapter<AbsentHistoryHolder> {
    public static final String TAG = "AbsentHistory";

    private ArrayList<AbsentHistoryHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_absent_history;

    public AbsentHistory(@NonNull Context context, ArrayList<AbsentHistoryHolder> pElement) {
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

            TextView txvw_work_00        = (TextView) 	view.findViewById(R.id.txvw_work_00);
            TextView txvw_checkin_00     = (TextView) 	view.findViewById(R.id.txvw_checkin_00);
            TextView txvw_checkout_00    = (TextView) 	view.findViewById(R.id.txvw_checkout_00);
            TextView txvw_note_00        = (TextView) 	view.findViewById(R.id.txvw_note_00);
            RelativeLayout rvly_store_00 = (RelativeLayout) view.findViewById(R.id.rvly_store_00);

            Calendar calendar     = Calendar.getInstance();
            Date currentTime      = calendar.getTime();
            DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

            final AbsentHistoryHolder holder = holders.get(position);
            if (holder != null){
                if (formatDate.format(currentTime).equalsIgnoreCase(holder.date)){
                    rvly_store_00.setBackgroundColor(getContext().getResources().getColor(R.color.green_01));
                }
                else {
                    rvly_store_00.setBackgroundColor(getContext().getResources().getColor(R.color.blue_01));
                }
                txvw_work_00.setText(holder.date_view);
                txvw_checkin_00.setText(holder.check_in);
                txvw_checkout_00.setText(holder.check_out);
                txvw_note_00.setText(holder.note);
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
        public void selectedItem(AbsentHistoryHolder holder, int position);
    }
}
