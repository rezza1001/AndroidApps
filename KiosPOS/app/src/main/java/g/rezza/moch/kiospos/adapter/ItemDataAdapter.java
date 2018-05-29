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
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.EventHolder;
import g.rezza.moch.kiospos.lib.Parse;

/**
 * Created by Rezza on 11/9/17.
 */

public class ItemDataAdapter   extends ArrayAdapter<EventHolder> {
    public static final String TAG = "ItemDataAdapter";

    private ArrayList<EventHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_item_data;


    public ItemDataAdapter (@NonNull Context context, ArrayList<EventHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null) {
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }

            ImageView imvw_image_10 = (ImageView)   view.findViewById(R.id.imvw_image_10);
            TextView txvw_title_11  = (TextView)    view.findViewById(R.id.txvw_title_11);
            TextView txvw_date_12   = (TextView)    view.findViewById(R.id.txvw_date_12);

            final EventHolder holder = holders.get(position);
            if (holder != null){
                imvw_image_10.setImageBitmap(holder.image);
                txvw_title_11.setText(Parse.formatTextTrim(holder.name, 38));
                txvw_date_12.setText(holder.liveDate);
            }


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
        public void selectedItem(EventHolder holder, int position);
    }
}
