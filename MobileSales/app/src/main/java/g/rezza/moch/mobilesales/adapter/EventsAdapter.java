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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.EventListHolder;

/**
 * Created by rezza on 03/01/18.
 */

public class EventsAdapter extends ArrayAdapter<EventListHolder> {
    public static final String TAG = "EventsAdapter";

    private ArrayList<EventListHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_events;

    public EventsAdapter(@NonNull Context context, ArrayList<EventListHolder> pElement) {
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

            ImageView imvw_icon_00       = (ImageView) 	view.findViewById(R.id.imvw_icon_00);
            ImageView imvw_ratting_00    = (ImageView) 	view.findViewById(R.id.imvw_ratting_00);
            TextView txvw_name_00        = (TextView) 	view.findViewById(R.id.txvw_name_00);
            TextView txvw_date_00        = (TextView) 	view.findViewById(R.id.txvw_date_00);

            LinearLayout rvly_ratting_00 = (LinearLayout) view.findViewById(R.id.rvly_ratting_00);


            final EventListHolder holder = holders.get(position);
            if (holder != null){
                Glide.with(getContext()).load(holder.imageUrl).into(imvw_icon_00);
                txvw_name_00.setText(holder.name);
                txvw_date_00.setText(holder.liveDate);
                imvw_ratting_00.setImageResource(R.drawable.ic_ratting);
                if (holder.genreID.equals("1")){
                    rvly_ratting_00.setVisibility(View.GONE);
                }
                else {
                    rvly_ratting_00.setVisibility(View.VISIBLE);
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
        public void selectedItem(EventListHolder holder, int position);
    }
}
