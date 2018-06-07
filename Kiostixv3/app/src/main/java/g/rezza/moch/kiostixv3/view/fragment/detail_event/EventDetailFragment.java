package g.rezza.moch.kiostixv3.view.fragment.detail_event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.CategoryQty;
import g.rezza.moch.kiostixv3.component.MyDatePicker;
import g.rezza.moch.kiostixv3.component.MyTimePicker;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 19/02/18.
 */

public class EventDetailFragment extends Fragment{
    private static final String TAG = "EventDetailFragment";
    private static final String ARG_POSITION = "position";

    private TextView txvw_venue_00;
    private TextView txvw_synopsis_desc_00;
    private TextView txvw_date_00;

    private int mPosition;
    private BookingDB mybooking = new BookingDB();

    public static Fragment newInstance(String obj) {
        EventDetailFragment f = new EventDetailFragment();
        Bundle b = new Bundle();
        b.putString(ARG_POSITION, obj);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mybooking.getData(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_event_detail, null);

        txvw_venue_00   = v.findViewById(R.id.txvw_venue_00);
        txvw_synopsis_desc_00
                        = v.findViewById(R.id.txvw_synopsis_desc_00);
        txvw_date_00    = v.findViewById(R.id.txvw_date_00);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void create(){
        mybooking.getData(getActivity());
        txvw_venue_00.setText(Html.fromHtml(mybooking.event_venue));
        txvw_synopsis_desc_00.setText(Html.fromHtml(mybooking.event_desc));
        DateFormat format_in  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format_out = new SimpleDateFormat("EEE, dd MMM, yyyyy");
        try {
            Date date_in = format_in.parse(mybooking.event_start_date);
            Date date_out = format_in.parse(mybooking.event_end_date);
            if (mybooking.event_start_date.equals(mybooking.event_end_date)){
                txvw_date_00.setText(format_out.format(date_in));
            }
            else {
                txvw_date_00.setText(format_out.format(date_in) +" - "+ format_out.format(date_out));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
