package g.rezza.moch.clientdashboard.adapter;

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

import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.holder.CategoryHolder;

/**
 * Created by Rezza on 9/22/17.
 */

public class EventCatAdapter extends ArrayAdapter<CategoryHolder> {
    public static final String TAG = "RZ EventCatAdapter";

    private ArrayList<CategoryHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_event_cat;

    public EventCatAdapter(@NonNull Context context, ArrayList<CategoryHolder> pElement) {
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

            final TextView txvw_name_00           = (TextView) 	view.findViewById(R.id.txvw_ctg_00);
            final RelativeLayout rvly_check_00    = (RelativeLayout) view.findViewById(R.id.rvly_check_00);

            final CategoryHolder holder = holders.get(position);
            if (holder != null){
                txvw_name_00.setText(holder.category_name);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Click Item");
                    if (holder.selected){
                        holder.selected = false;
                        rvly_check_00.setBackgroundResource(R.color.colorBlueBlack_01_t50);
                    }
                    else {
                        holder.selected = true;
                        rvly_check_00.setBackgroundColor(holder.color);
                    }

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
        public void selectedItem(CategoryHolder holder, int position);
    }
}
