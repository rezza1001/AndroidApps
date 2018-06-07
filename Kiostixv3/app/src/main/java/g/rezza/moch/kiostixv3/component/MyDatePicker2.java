package g.rezza.moch.kiostixv3.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.CalenderActivity;

/**
 * Created by rezza on 21/02/18.
 */

public class MyDatePicker2 extends RelativeLayout {
    private RelativeLayout rvly_date_00;
    private TextView edtx_search_00;

    private String mTextDefault = "Pilih Tanggal";
    private Date mDate = new Date();
    private DateFormat dateFormat1, dateFormat2;
    private ArrayList<Calendar> listCalenders = new ArrayList<>();

    public MyDatePicker2(Context context) {
        super(context);
    }

    public MyDatePicker2(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mydatepicker2, this, true);
        rvly_date_00 = (RelativeLayout) findViewById(R.id.rvly_date_00);
        edtx_search_00 = (TextView) findViewById(R.id.edtx_search_00);

        dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        dateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        rvly_date_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                if (!edtx_search_00.getText().toString().equalsIgnoreCase(mTextDefault)) {
                    now.setTime(mDate);
                }
                Intent intent = new Intent(getContext(), CalenderActivity.class);
                ((Activity)getContext()).startActivityForResult(intent,1);

            }
        });
    }

    public void setValue(Date pDate) {
        mDate = pDate;
        edtx_search_00.setText(dateFormat1.format(mDate));
    }

    public String getValue() {
        try {
            dateFormat2.parse(edtx_search_00.getText().toString());
            return dateFormat2.format(mDate);
        }catch (Exception e){
            return "";
        }
    }

    public void enableDates(ArrayList<Calendar> calendars){
        listCalenders = calendars;
    }

    protected ChangeListener mListener;
    public void setOnChangeListener(ChangeListener pListener) {
        mListener = pListener;
    }
    public interface ChangeListener {
        public void after(String s);
    }
}
