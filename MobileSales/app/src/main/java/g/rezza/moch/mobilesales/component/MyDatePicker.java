package g.rezza.moch.mobilesales.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 21/02/18.
 */

public class MyDatePicker extends RelativeLayout {
    private RelativeLayout rvly_date_00;
    private TextView edtx_search_00;

    private String mTextDefault = "Pilih Tanggal";
    private Date mDate = new Date();
    private DateFormat dateFormat1, dateFormat2;
    private ArrayList<Calendar> listCalenders = new ArrayList<>();
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    private ImageView   imvw_search_00;

    private Calendar minimumDate;
    private Calendar maximumDate;

    private boolean enable = true;

    public MyDatePicker(Context context) {
        super(context);
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mydatepicker, this, true);
        rvly_date_00    = (RelativeLayout) findViewById(R.id.rvly_date_00);
        edtx_search_00  = (TextView) findViewById(R.id.edtx_search_00);
        imvw_search_00  = (ImageView) findViewById(R.id.imvw_search_00);

        dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        dateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        rvly_date_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enable){
                    onSelectedDate();
                }

            }
        });

        dpd = new DatePickerDialog();
    }

    private void onSelectedDate(){
        Calendar now = Calendar.getInstance();
        if (!edtx_search_00.getText().toString().equalsIgnoreCase(mTextDefault)) {
            now.setTime(mDate);
        }
        dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder sb = new StringBuilder();
                        DateFormat formatter = new SimpleDateFormat("dd MM yyyy", Locale.US);
                        try {
                            sb.append(dayOfMonth).append(" ");
                            sb.append((monthOfYear + 1)).append(" ");
                            sb.append(year);
                            Log.d("TAG"," sb.toString() "+ sb.toString());
                            mDate = formatter.parse(sb.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        setValue(mDate);
                        if (mListener != null) {
                            mListener.after(getValue());
                        }
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        if (minimumDate != null){
            dpd.setMinDate(minimumDate);
        }

        if(maximumDate != null){
            dpd.setMaxDate(maximumDate);
        }

        dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_1);
        Activity activity = (Activity) getContext();
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");

        // Code to disable particular date

        if (listCalenders.size() > 0) {
            Calendar[] days;
            days = listCalenders.toArray(new Calendar[listCalenders.size()]);
            dpd.setSelectableDays(days);
        }
    }

    public void setMinimum(Calendar calendar){
        minimumDate = calendar;
    }

    public void setMaximum(Calendar calendar){
        maximumDate = calendar;
    }
    public void setValue(Date pDate) {
        mDate = pDate;
        Log.d("MyDatePicker","dateFormat1.format(mDate) "+ dateFormat1.format(mDate));
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

    public void disable(){
        enable = false;
        imvw_search_00.setImageDrawable(getResources().getDrawable(R.mipmap.ic_calender_disable));
    }

    public void enable(){
        enable = true;
        imvw_search_00.setImageDrawable(getResources().getDrawable(R.mipmap.ic_date));
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
