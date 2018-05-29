package g.rezza.moch.kiospos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.AttendenzHolder;

/**
 * Created by Rezza on 9/22/17.
 */

public class AttendenzAdapter extends ArrayAdapter<AttendenzHolder> {
    public static final String TAG = "RZ AttendenzAdapter";

    private ArrayList<AttendenzHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_attendenz;

    public AttendenzAdapter(@NonNull Context context, ArrayList<AttendenzHolder> pElement) {
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

            TextView        txvw_option_20      = (TextView) 	    view.findViewById(R.id.txvw_option_20);
            ImageView       imvw_option_21      = (ImageView)       view.findViewById(R.id.imvw_option_21) ;
            RelativeLayout  rvly_indicator_22   = (RelativeLayout)  view.findViewById(R.id.rvly_indicator_22) ;

            final AttendenzHolder holder = holders.get(position);
            if (holder != null){
                if (holder.complete){
                    txvw_option_20.setText(holder.nama);
                    imvw_option_21.setImageResource(R.drawable.ic_check);
                }
                else if (!holder.complete){
                    txvw_option_20.setText(holder.nama +" " +  position + " : - ");
                    imvw_option_21.setImageResource(R.drawable.ic_view);
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
        public void selectedItem(AttendenzHolder holder, int position);
    }
}
