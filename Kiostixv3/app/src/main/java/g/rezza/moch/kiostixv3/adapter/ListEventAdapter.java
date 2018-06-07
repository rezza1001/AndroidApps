package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
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

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.holder.EventsList;

/**
 * Created by rezza on 11/02/18.
 */

public class ListEventAdapter extends ArrayAdapter<EventsList> {
    private static String TAG = "ListEventAdapter";
    private LayoutInflater mInflater;

    public ListEventAdapter(Context context, ArrayList<EventsList> values) {
        super(context, R.layout.adapter_list_events, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final EventsList Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_list_events, parent, false);
            holder = new Holder();
            holder.txvw_title_00 = (TextView) convertView.findViewById(R.id.txvw_title_00);
            holder.txvw_venue_00 = (TextView) convertView.findViewById(R.id.txvw_venue_00);
            holder.imvw_image_00 = (ImageView) convertView.findViewById(R.id.imvw_image_00);
            holder.lnly_content_00 = (LinearLayout) convertView.findViewById(R.id.lnly_content_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txvw_title_00.setText(Event.name);
        holder.txvw_venue_00.setText(Event.venue);
        Glide.with(getContext()).load(Event.img_url).placeholder(R.drawable.no_image).into(holder.imvw_image_00);
        // Popul

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView txvw_title_00;
        public TextView txvw_venue_00;
        public ImageView imvw_image_00;
        public LinearLayout lnly_content_00;
    }

}
