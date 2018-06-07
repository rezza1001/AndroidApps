package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.holder.EventsList;

/**
 * Created by rezza on 11/02/18.
 */

public class SeeAllAdapter extends ArrayAdapter<EventsList> {
    private static String TAG = "SeeAllAdapter";
    private LayoutInflater mInflater;

    public SeeAllAdapter(Context context, ArrayList<EventsList> values) {
        super(context, R.layout.adapter_see_all, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final EventsList Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_see_all, parent, false);
            holder = new Holder();
            holder.txvw_title_00    = (TextView)  convertView.findViewById(R.id.txvw_title_00);
            holder.txvw_venue_00    = (TextView)  convertView.findViewById(R.id.txvw_venue_00);
            holder.imvw_event_00    = (ImageView) convertView.findViewById(R.id.imvw_event_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txvw_title_00.setText(Event.name);
        holder.txvw_venue_00.setText(Event.venue);
        Glide.with(getContext()).load(Event.img_url).placeholder(R.drawable.no_image).into(holder.imvw_event_00);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.OnSelect(Event);
                }
            }
        });

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imvw_event_00;
        public TextView txvw_title_00;
        public TextView txvw_venue_00;
    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void OnSelect(EventsList event);
    }
}
