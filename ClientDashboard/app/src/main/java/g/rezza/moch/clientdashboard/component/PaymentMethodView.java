package g.rezza.moch.clientdashboard.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;
import g.rezza.moch.clientdashboard.libs.MasterView;
import g.rezza.moch.clientdashboard.libs.Utils;

/**
 * Created by Rezza on 10/3/17.
 */

public class PaymentMethodView extends MasterView {
    public static String TAG = "PaymentMethodView";

    private PieChart pchrt_report_04;

    public PaymentMethodView(Context context) {
        super(context);
    }

    public PaymentMethodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_payment_method, this, true);
        createPie();
    }

    public void create(){
        ParameterHolder param = new ParameterHolder();
        param.getData(getContext());
        loadToServer(param.event_id);
    }

    private void createPie(){

        pchrt_report_04 = (PieChart) findViewById(R.id.pchrt_report_04);
        pchrt_report_04.setUsePercentValues(true);
        pchrt_report_04.getDescription().setEnabled(false);
        pchrt_report_04.setExtraOffsets(5, 10, 5, 5);

        pchrt_report_04.setDragDecelerationFrictionCoef(0.95f);
//        pchrt_report_04.setCenterTextTypeface(mTfLight);


        pchrt_report_04.setDrawHoleEnabled(true);
        pchrt_report_04.setHoleColor(Color.WHITE);

        pchrt_report_04.setTransparentCircleColor(Color.WHITE);
        pchrt_report_04.setTransparentCircleAlpha(10);

        pchrt_report_04.setHoleRadius(50f);
        pchrt_report_04.setTransparentCircleRadius(61f);

        pchrt_report_04.setDrawCenterText(true);

        pchrt_report_04.setRotationAngle(0);
        // enable rotation of the chart by touch
        pchrt_report_04.setRotationEnabled(true);
        pchrt_report_04.setHighlightPerTapEnabled(true);
        pchrt_report_04.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // add a selection listener

        Legend l = pchrt_report_04.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(true);

        // entry label styling
        pchrt_report_04.setEntryLabelColor(Color.WHITE);
//        pchrt_report_04.setEntryLabelTypeface(mTfRegular);
        pchrt_report_04.setEntryLabelTextSize(0f);

        pchrt_report_04.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void loadToServer(String event_id){
        try {

            JSONArray ja        =new JSONArray();
            {
                JSONObject jo = new JSONObject();
                jo.put("key","Credit Card");
                jo.put("value","30");
                ja.put(jo);
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("key","Bank Transfer");
                jo.put("value","20.3");
                ja.put(jo);
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("key","CIMB Clicks");
                jo.put("value","19.7");
                ja.put(jo);
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("key","Promotor");
                jo.put("value","30");
                ja.put(jo);
            }
            setDataPie(ja);
            mOnReceiveListener.onReceive(this,"00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataPie(JSONArray ja) {
        pchrt_report_04.setCenterText("");
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i=0; i<ja.length(); i++){
            try {
                int total   = ja.getJSONObject(i).getInt("value");
                String name = ja.getJSONObject(i).getString("key");
                entries.add(new PieEntry(total, name));
                colors.add(new Utils().Colors().getColor(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PieDataSet dataSet = new PieDataSet(entries,"" );
        dataSet.setDrawIcons(false);
        dataSet.setValueTextSize(8f);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.WHITE);
        pchrt_report_04.setData(data);

        // undo all highlights
        pchrt_report_04.highlightValues(null);
        pchrt_report_04.invalidate();
    }

    public void setEmpty(){
        pchrt_report_04.setCenterText("");
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        entries.add(new PieEntry(0, "Empty"));
        colors.add(Color.DKGRAY);
        PieDataSet dataSet = new PieDataSet(entries,"" );
        dataSet.setDrawIcons(false);
        dataSet.setValueTextSize(8f);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.WHITE);
        pchrt_report_04.setData(data);

        pchrt_report_04.highlightValues(null);
        pchrt_report_04.invalidate();
    }

    private SpannableString generateCenterSpannableText(double count, double mx) {
        double prc = (count / mx)* 100;
        SpannableString s = new SpannableString( prc + "%");
        return s;
    }
}
