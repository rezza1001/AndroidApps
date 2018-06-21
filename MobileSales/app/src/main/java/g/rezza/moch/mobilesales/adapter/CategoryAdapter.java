package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import g.rezza.moch.mobilesales.Database.SchedulesDB;
import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 11/02/18.
 */

public class CategoryAdapter extends ArrayAdapter<SchedulesDB> {
    private static String TAG = "CategoryAdapter";
    private LayoutInflater mInflater;
    private  ArrayList<SchedulesDB> mList = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<SchedulesDB> values) {
        super(context, R.layout.adapter_cayegory, values);
        mList = values;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final SchedulesDB schedule = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_cayegory, parent, false);
            holder = new Holder();

            holder.txvw_title_00    = (TextView)  convertView.findViewById(R.id.txvw_title_10);
            holder.txvw_price_00    = (TextView)  convertView.findViewById(R.id.txvw_price_00);
            holder.txvw_venue_00    = (TextView)  convertView.findViewById(R.id.txvw_venue_00);
            holder.txvw_date_10     = (TextView)  convertView.findViewById(R.id.txvw_date_10);
            holder.txvw_select_00     = (TextView)  convertView.findViewById(R.id.txvw_select_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.txvw_title_00.setText(schedule.name);
        holder.txvw_price_00.setText(schedule.getMaxprice());
        holder.txvw_venue_00.setText(schedule.venue);

        Calendar calendar       = Calendar.getInstance();
        TimeZone tz             = TimeZone.getTimeZone("Asia/Jakarta");
        DateFormat format       = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat formatout    = new SimpleDateFormat("EEEE dd MMMM yyyy");
        formatout.setTimeZone(tz);
        try {
            calendar.setTime(format.parse(schedule.date));
            holder.txvw_date_10.setText(formatout.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (schedule.selected){
            holder.txvw_select_00.setBackgroundResource(R.drawable.button_orange);
            holder.txvw_select_00.setText(getContext().getResources().getString(R.string.cancel));
        }
        else {
            holder.txvw_select_00.setBackgroundResource(R.drawable.button_round_green);
            holder.txvw_select_00.setText(getContext().getResources().getString(R.string.select));
        }


        holder.txvw_select_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.OnSelect(schedule, position);
                }
            }
        });
        return convertView;
    }

    public SchedulesDB getItem(int position){
        Log.d(TAG,"LIST SIZE "+ mList.size());
        return  mList.get(position);
    }

    public int getSize(){
        Log.d(TAG,"getSize "+ mList.size());
        return  mList.size();
    }
    public void clearAllSelected(){
        for (int i=0; i<mList.size(); i++){
            mList.get(i).selected = false;
        }
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView txvw_title_00;
        public TextView txvw_venue_00;
        public TextView txvw_date_10;
        public TextView txvw_price_00;
        public TextView txvw_select_00;
    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void OnSelect(SchedulesDB event, int position);
    }

}
