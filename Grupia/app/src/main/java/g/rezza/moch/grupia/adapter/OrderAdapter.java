package g.rezza.moch.grupia.adapter;

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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.listHolder.OrderLHolder;

/**
 * Created by rezza on 26/02/18.
 */

public class OrderAdapter  extends ArrayAdapter<OrderLHolder> {
    public static final String TAG = "ItemDataAdapter";

    private ArrayList<OrderLHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_order;


    public OrderAdapter(@NonNull Context context, ArrayList<OrderLHolder> pElement) {
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

            ImageView imvw_image_00 = (ImageView) view.findViewById(R.id.imvw_image_00);
            TextView txvw_title_00  = (TextView) view.findViewById(R.id.txvw_title_00);
            TextView txvw_price_00  = (TextView) view.findViewById(R.id.txvw_price_00);

            final OrderLHolder holder = holders.get(position);
            if (holder != null) {
                Glide.with(getContext()).load(holder.imageUrl).into(imvw_image_00);
                txvw_title_00.setText(holder.title);
                txvw_price_00.setText("Rp. "+ holder.price);
            }


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

        /*
     * Register Listener
     */

    private OnSelectedItemListener mOnSelectedItemListener;

    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener) {
        mOnSelectedItemListener = pOnSelectedItemListener;
    }

    /*
     * Interface Listener
     */

    public interface OnSelectedItemListener {
        public void selectedItem(OrderLHolder holder, int position);
    }
}
