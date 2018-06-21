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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.EventCategoryHolder;
import g.rezza.moch.mobilesales.holder.EventCategoryHolder;
import g.rezza.moch.mobilesales.lib.Parse;

/**
 * Created by rezza on 03/01/18.
 */

public class EventCategoryAdapter extends ArrayAdapter<EventCategoryHolder> {
    public static final String TAG = "NotificationAdapter";

    private ArrayList<EventCategoryHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_event_category;

    public EventCategoryAdapter(@NonNull Context context, ArrayList<EventCategoryHolder> pElement) {
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

            TextView txvw_name_00        = (TextView) 	view.findViewById(R.id.txvw_name_00);
            TextView txvw_adult_00       = (TextView) 	view.findViewById(R.id.txvw_adult_00);
            TextView txvw_child_00       = (TextView) 	view.findViewById(R.id.txvw_child_00);
            TextView txvw_price_00       = (TextView) 	view.findViewById(R.id.txvw_price_00);
            RelativeLayout bbtn_add_00   = (RelativeLayout)     view.findViewById(R.id.bbtn_add_00);


            final EventCategoryHolder holder = holders.get(position);
            if (holder != null){
                txvw_name_00.setText(holder.name);
////                txvw_adult_00.setText(TextUtils.concat( "Adult (IDR ",
////                        Parse.BoldText(Parse.toCurrnecy(holder.price_adult)),"/Ticket)",
////                        " X " ,Parse.BoldText(""+holder.quantity_adult) ));
////                txvw_child_00.setText(TextUtils.concat( "Child (IDR ",
////                        Parse.BoldText(Parse.toCurrnecy(holder.price_child)),"/Ticket)",
////                        " X " ,Parse.BoldText(""+holder.quantity_child) ));
////
////                float total_adult = holder.price_adult * holder.quantity_adult;
////                float total_child = holder.price_child * holder.quantity_child;
//                float total_price = total_adult + total_child;
//                txvw_price_00.setText("IDR "+ Parse.toCurrnecy(total_price));
            }

            bbtn_add_00.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
        public void selectedItem(EventCategoryHolder holder, int position);
    }
}
