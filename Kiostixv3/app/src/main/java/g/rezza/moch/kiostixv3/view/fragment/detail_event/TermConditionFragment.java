package g.rezza.moch.kiostixv3.view.fragment.detail_event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.CategoryQty;
import g.rezza.moch.kiostixv3.database.BookingDB;
import g.rezza.moch.kiostixv3.lib.Parse;

/**
 * Created by rezza on 19/02/18.
 */

public class TermConditionFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    private TextView        txvw_term_00;
    private int mPosition;
    private long mTotal = 0;

    public static Fragment newInstance(int position) {
        TermConditionFragment f = new TermConditionFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_term_condition, null);
        txvw_term_00    = (TextView)        v.findViewById(R.id.txvw_term_00);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    public void create(){
        BookingDB bookingDB = new BookingDB();
        bookingDB.getData(getActivity());
        txvw_term_00.setText(Html.fromHtml(bookingDB.event_term));
    }

}
