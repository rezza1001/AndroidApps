package g.rezza.moch.kiostixv3.component;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import g.rezza.moch.kiostixv3.R;

/**
 * Created by rezza on 21/02/18.
 */

public class MyDatePickerWthBorder extends RelativeLayout {

    private EditText edtx_00;
    private TextInputLayout txip_00;

    private String mTextDefault = "Pilih Tanggal";
    private Date mDate = new Date();
    private DateFormat dateFormat1, dateFormat2;
    private ArrayList<Calendar> listCalenders = new ArrayList<>();

    public MyDatePickerWthBorder(Context context) {
        super(context);
    }

    public MyDatePickerWthBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mydatepicker_wth_border, this, true);
        edtx_00 = (EditText) findViewById(R.id.edtx_00);
        txip_00 = (TextInputLayout) findViewById(R.id.txip_00);

        dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        dateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        edtx_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker();
            }
        });

        edtx_00.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    createDatePicker();
                }
            }
        });
    }

    public void init(){
        edtx_00.setText(mTextDefault);
        mDate = new Date();
    }

    public void setTitle(String title){
        txip_00.setHint(title);
    }

    public void setValue(Date pDate) {
        mDate = pDate;
        edtx_00.setText(dateFormat1.format(mDate));
    }

    public String getValue() {
        try {
            dateFormat2.parse(edtx_00.getText().toString());
            return dateFormat2.format(mDate);
        }catch (Exception e){
            return "";
        }
    }

    private void createDatePicker(){
        Calendar now = Calendar.getInstance();
        if (!edtx_00.getText().toString().equalsIgnoreCase(mTextDefault)) {
            now.setTime(mDate);
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder sb = new StringBuilder();
                        DateFormat formatter = new SimpleDateFormat("yyyy MM dd" +
                                "", Locale.US);
                        try {
                            sb.append(year).append(" ");
                            sb.append((monthOfYear + 1)).append(" ");
                            sb.append(dayOfMonth);
                            Log.d("DATE", sb.toString());
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
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
        Activity activity = (Activity) getContext();
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");

        // Code to disable particular date

        if (listCalenders.size() > 0) {
            Calendar[] days;
            days = listCalenders.toArray(new Calendar[listCalenders.size()]);
            dpd.setSelectableDays(days);
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
