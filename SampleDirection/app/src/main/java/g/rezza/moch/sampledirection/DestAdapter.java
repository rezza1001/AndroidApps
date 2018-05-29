package g.rezza.moch.sampledirection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by rezza on 11/02/18.
 */

public class DestAdapter extends ArrayAdapter<DirectionList> {
    private static String TAG = "DestAdapter";
    private LayoutInflater mInflater;

    public DestAdapter(Context context, ArrayList<DirectionList> values) {
        super(context, R.layout.adapter_dest, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final DirectionList Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_dest, parent, false);
            holder = new Holder();
            holder.txvw_address_00    = (TextView)  convertView.findViewById(R.id.txvw_address_00);
            holder.txvw_dest_00     = (TextView)  convertView.findViewById(R.id.txvw_dest_00);
            holder.txvw_time_00     = (TextView)  convertView.findViewById(R.id.txvw_time_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txvw_address_00.setText(Event.street);
        holder.txvw_dest_00.setText(Event.dest);
        holder.txvw_time_00.setText(Event.durasi);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onSelected(Event, position);
                }
            }
        });

        return convertView;
    }

    private static class Holder {
        public TextView txvw_address_00;
        public TextView txvw_dest_00;
        public TextView txvw_time_00;
    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener listener){
        mListener = listener;
    }
    public interface OnSelectedListener{
        public void onSelected(DirectionList holder, int position);
    }

}
