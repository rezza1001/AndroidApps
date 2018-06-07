package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.database.MyTixDB;
import g.rezza.moch.kiostixv3.holder.MyTixList;
import g.rezza.moch.kiostixv3.holder.MyTixList;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 11/02/18.
 */

public class MyTixAdapter extends ArrayAdapter<MyTixList> {
    private static String TAG = "MyTixAdapter";
    private LayoutInflater mInflater;

    public MyTixAdapter(Context context, ArrayList<MyTixList> values) {
        super(context, R.layout.adapter_mytix, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final MyTixList Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_mytix, parent, false);
            holder = new Holder();
            holder.txvw_title_00    = (TextView)  convertView.findViewById(R.id.txvw_title_00);
            holder.txvw_date_00     = (TextView)  convertView.findViewById(R.id.txvw_date_00);
            holder.txvw_time_00     = (TextView)  convertView.findViewById(R.id.txvw_time_00);
            holder.txvw_month_00    = (TextView)  convertView.findViewById(R.id.txvw_month_00);
            holder.txvw_year_00     = (TextView)  convertView.findViewById(R.id.txvw_year_00);
            holder.txvw_venue_00    = (TextView)  convertView.findViewById(R.id.txvw_venue_00);
            holder.txvw_price_00    = (TextView)  convertView.findViewById(R.id.txvw_price_00);
            holder.txvw_status_00   = (TextView)  convertView.findViewById(R.id.txvw_status_00);
            holder.txvw_seedtl_00   = (TextView)  convertView.findViewById(R.id.txvw_seedtl_00);
            holder.txvw_expdate_00  = (TextView)  convertView.findViewById(R.id.txvw_expdate_00);
            holder.imvw_tix_00      = (ImageView) convertView.findViewById(R.id.imvw_tix_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txvw_title_00.setText(Event.event_name);
        holder.txvw_venue_00.setText(Event.event_venue);
        holder.txvw_price_00.setText("IDR "+ Parse.toCurrnecy(Event.event_price));
        holder.txvw_status_00.setText(Event.event_status_desc);
        holder.txvw_time_00.setText(Event.event_time);
        DateFormat format   = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        DateFormat format2  = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar   = Calendar.getInstance();
        Calendar calendar2  = Calendar.getInstance();
        String expire       = "";
        String date        = "";
        try {
            calendar.setTime(format2.parse(Event.event_date));
            calendar2.setTime(format2.parse(Event.event_expired_date));
            date   = format.format(calendar.getTime());
            expire  = format2.format(calendar2.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txvw_date_00.setText(date.split(" ")[0]);
        holder.txvw_month_00.setText(date.split(" ")[1]);
        holder.txvw_year_00.setText(calendar.get(Calendar.YEAR)+"");
        holder.txvw_expdate_00.setText(expire +" | "+Event.event_expired_time );

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onSelected(Event);
                }
            }
        });

        holder.txvw_seedtl_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onSeeDetail(Event);
                }
            }
        });
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imvw_tix_00;
        public TextView txvw_title_00;
        public TextView txvw_venue_00;
        public TextView txvw_time_00;
        public TextView txvw_price_00;
        public TextView txvw_status_00;
        public TextView txvw_seedtl_00;
        public TextView txvw_month_00;
        public TextView txvw_date_00;
        public TextView txvw_year_00;
        public TextView txvw_expdate_00;
    }

    private OnSelectedListener mListener;

    public void setOnselectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }

    public interface OnSelectedListener{
        public void onSelected(MyTixList myTixDB);
        public void onSeeDetail(MyTixList myTixDB);
    }

}
