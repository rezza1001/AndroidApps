package g.rezza.moch.kiostixv3.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import g.rezza.moch.kiostixv3.R;

/**
 * Created by rezza on 21/02/18.
 */

public class MyTimePicker extends RelativeLayout {
    private RelativeLayout rvly_date_00;
    private TextView edtx_search_00;

    private String mTextDefault = "Pilih Waktu";
    private ArrayList<Timepoint> listCalenders = new ArrayList<>();
    private int mHour = 0;
    private int mMinute = 0;

    public MyTimePicker(Context context) {
        super(context);
    }

    public MyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mytimepicker, this, true);
        rvly_date_00 = (RelativeLayout) findViewById(R.id.rvly_date_00);
        edtx_search_00 = (TextView) findViewById(R.id.edtx_search_00);


        rvly_date_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        setValue(hourOfDay, minute);
                        if (mListener != null) {
                            mListener.after(getValue());
                        }

                    }
                },mHour,mMinute,true);
                tpd.setVersion(TimePickerDialog.Version.VERSION_1);
                Activity activity = (Activity) getContext();
                tpd.show(activity.getFragmentManager(), "Timepickerdialog");

                if (listCalenders.size() > 0) {
                    Timepoint timepoint[] = listCalenders.toArray(new Timepoint[listCalenders.size()]);
                    tpd.setSelectableTimes(timepoint);
                }

            }
        });
    }

    public void setValue(int hour, int minute) {
        mHour   = hour;
        mMinute = minute;
        String sHour    = hour   >=10 ? hour+""  :"0"+hour;
        String sMinute  = minute >=10 ? minute+"":"0"+minute;
        edtx_search_00.setText(sHour+" : "+ sMinute );
    }

    public String getValue() {
        if (mTextDefault.equalsIgnoreCase(edtx_search_00.getText().toString())){
            return "";
        }
        return edtx_search_00.getText().toString();
    }

    public void enableTimes(ArrayList<Timepoint> times){
        listCalenders = times;
    }

    protected ChangeListener mListener;
    public void setOnChangeListener(ChangeListener pListener) {
        mListener = pListener;
    }
    public interface ChangeListener {
        public void after(String s);
    }
}
