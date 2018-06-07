package g.rezza.moch.kiostixv3.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.kiostixv3.R;
import io.fabric.sdk.android.Fabric;

public class CalenderActivity extends AppCompatActivity {
    private static final String  TAG = "CalenderActivity";

    private CalendarPickerView calendar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_calender);


        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar_view.init(today, nextYear.getTime())
                .withSelectedDate(today);

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }
}
