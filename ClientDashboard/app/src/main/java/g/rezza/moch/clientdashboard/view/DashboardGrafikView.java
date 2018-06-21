package g.rezza.moch.clientdashboard.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.component.CustomMarkerView;
import g.rezza.moch.clientdashboard.component.LegentView_01;
import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;

/**
 * Created by Rezza on 9/20/17.
 */

public class DashboardGrafikView extends RelativeLayout implements View.OnClickListener {
    public static final String TAG = "RZ DashboardG";

    private LegentView_01 lgnd_sold_01;
    private LegentView_01 lgnd_sales_01;
    private LegentView_01 lgnd_pageview_01;
    private LegentView_01 lgnd_rate_01;
    private Button        bbtn_today_02;
    private Button        bbtn_yesterday_02;
    private Button        bbtn_7days_01;
    private Button        bbtn_month_02;
    private Button        bbtn_last_month_02;

    private LineChart     mLineChart;
    private YAxis         leftAxis;

    private ProgressDialog mProgress;
    private String         mEventID = "";
    int                    maxHeight = 0;
    private HashMap<Integer,String> MAPS_LEGEND = new HashMap<>();
    private String         mParamSelected = "1";

    public DashboardGrafikView(Context context) {
        super(context);
    }
    public DashboardGrafikView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_dashboard_v2, this, true);
        lgnd_sold_01        = (LegentView_01) findViewById(R.id.lgnd_sold_01);
        lgnd_sales_01       = (LegentView_01) findViewById(R.id.lgnd_sales_01);
        lgnd_pageview_01    = (LegentView_01) findViewById(R.id.lgnd_pageview_01);
        lgnd_rate_01        = (LegentView_01) findViewById(R.id.lgnd_rate_01);
        bbtn_today_02       = (Button)        findViewById(R.id.bbtn_today_02);
        bbtn_yesterday_02   = (Button)        findViewById(R.id.bbtn_yesterday_02);
        bbtn_7days_01       = (Button)        findViewById(R.id.bbtn_7days_01);
        bbtn_month_02       = (Button)        findViewById(R.id.bbtn_month_02);
        bbtn_last_month_02  = (Button)        findViewById(R.id.bbtn_last_month_02);

        bbtn_today_02.setOnClickListener(this);
        bbtn_yesterday_02.setOnClickListener(this);
        bbtn_7days_01.setOnClickListener(this);
        bbtn_month_02.setOnClickListener(this);
        bbtn_last_month_02.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switchButton(v);
        if (v == bbtn_today_02){
            loadToServer(mEventID,"1");
            mParamSelected = "1";
        }
        else if (v == bbtn_yesterday_02){
            loadToServer(mEventID,"2");
            mParamSelected = "2";
        }
        else if (v == bbtn_7days_01){
            loadToServer(mEventID,"3");
            mParamSelected = "3";
        }
        else if (v == bbtn_month_02){
            loadToServer(mEventID,"4");
            mParamSelected = "4";
        }
        else if (v == bbtn_last_month_02){
            loadToServer(mEventID,"5");
            mParamSelected = "5";
        }
    }
    public void create(){
        ParameterHolder parameter = new ParameterHolder();
        parameter.getData(getContext());

        mEventID = parameter.event_id;
        lgnd_sold_01.setTitle(1,"0");
        lgnd_sold_01.setIcon(R.drawable.ic_white_ticket);
        lgnd_sold_01.setBodyImage(R.drawable.baxkground_blue_lr_01);

        lgnd_sales_01.setTitle(0,"Total Volume");
        lgnd_sales_01.setTitle(1,"0");
        lgnd_sales_01.setBodyImage(R.drawable.baxkground_blue_lr_01);
        lgnd_sales_01.setIcon(R.drawable.ic_sales_01);

        lgnd_pageview_01.setTitle(0,"Page Views");
        lgnd_pageview_01.setTitle(1,"150");
        lgnd_pageview_01.setIcon(R.drawable.ic_page_view_01);
        lgnd_pageview_01.setBodyImage(R.drawable.baxkground_blue_lr_01);

        lgnd_rate_01.setTitle(0,"Conversion Rate");
        lgnd_rate_01.setTitle(1,"50,7 %");
        lgnd_rate_01.setIcon(R.drawable.ic_conversion_01);
        lgnd_rate_01.setBodyImage(R.drawable.baxkground_blue_lr_01);
        createLineChart();
        switch (parameter.paramter){
            case "1" : onClick(bbtn_today_02); break;
            case "2" : onClick(bbtn_yesterday_02); break;
            case "3" : onClick(bbtn_7days_01); break;
            case "4" : onClick(bbtn_month_02); break;
            case "5" : onClick(bbtn_last_month_02); break;
            default  :
                loadToServer(mEventID,"6");
                mParamSelected = "6";
                break;
        }


    }
    private void loadToServer(String event_id, String type){

       if (event_id.equals("1")){
           lgnd_sold_01.setTitle(1,"40");
           lgnd_sales_01.setTitle(1,"Rp 44.000.000");
           lgnd_pageview_01.setTitle(1,"150");
           lgnd_rate_01.setTitle(1,"50,7 %");

           try {
               JSONArray ja = new JSONArray();
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","0");
                   j1.put("value","0");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","02/09/17");
                   j1.put("value","5");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","04/09/17");
                   j1.put("value","20");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","06/09/17");
                   j1.put("value","3");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","07/09/17");
                   j1.put("value","8");
                   ja.put(j1);
               }

               ArrayList<Entry> values1 = createValueChart(ja);
               setFormat(2);
               setData(values1);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }else {
           lgnd_sold_01.setTitle(1,"21");
           lgnd_sales_01.setTitle(1,"Rp 24.000.000");
           lgnd_pageview_01.setTitle(1,"100");
           lgnd_rate_01.setTitle(1,"24,9 %");

           try {
               JSONArray ja = new JSONArray();
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","0");
                   j1.put("value","0");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","02/09/17");
                   j1.put("value","1");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","04/09/17");
                   j1.put("value","10");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","06/09/17");
                   j1.put("value","2");
                   ja.put(j1);
               }
               {
                   JSONObject j1 = new JSONObject();
                   j1.put("key","07/09/17");
                   j1.put("value","8");
                   ja.put(j1);
               }

               ArrayList<Entry> values1 = createValueChart(ja);
               setFormat(2);
               setData(values1);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }


    }

    private void createLineChart(){
        Log.d(TAG,"createLineChart()");
        mLineChart  = (LineChart) findViewById(R.id.chart1);

//        mLineChart.setOnChartGestureListener(this);
//        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        // to use for it
        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.component_custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv); // Set the marker to the chart

        leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(10000f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (""+value).split("\\.")[0];
            }
        });

        leftAxis.setDrawLimitLinesBehindData(true);
        mLineChart.getAxisRight().setEnabled(false);


        Legend l = mLineChart.getLegend();
        l.setDrawInside(false);
        l.setEnabled(false);
    }
    private void setData(ArrayList<Entry> values1){
        mLineChart.animateY(1500);
        mLineChart.resetTracking();

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        LineDataSet set1 = new LineDataSet(values1, "DataSet");
        set1.setLineWidth(1f);
        set1.setCircleRadius(4f);
        set1.setDrawCircleHole(true);
//        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        set1.setDrawIcons(false);
        set1.enableDashedLine(100f, 0f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        dataSets.add(set1); // add th


        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(0f);
        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }
    private void setFormat(int type){
        final XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        leftAxis.setAxisMaximum(Float.parseFloat((maxHeight+10)+""));
        IAxisValueFormatter format =  new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Integer x = Integer.parseInt((""+value).split("\\.")[0]);
               return   MAPS_LEGEND.get(x);
            }
        };
        xAxis.setValueFormatter(format );
    }
    private ArrayList<Entry> createValueChart(JSONArray ja){
        ArrayList<Entry> values1 = new ArrayList<Entry>();
        for (int i=0; i<ja.length(); i++){
            try {
                String key   = ja.getJSONObject(i).getString("key");
                int value    = ja.getJSONObject(i).getInt("value");
                MAPS_LEGEND.put(i,key);
                values1.add(new Entry(i, value));
                if (maxHeight < value){
                    maxHeight = value;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values1;
    }
    private void switchButton(View pbbtn){
        bbtn_today_02.setBackgroundResource(R.drawable.button_blue_3);
        bbtn_yesterday_02.setBackgroundResource(R.drawable.button_blue_3);
        bbtn_7days_01.setBackgroundResource(R.drawable.button_blue_3);
        bbtn_month_02.setBackgroundResource(R.drawable.button_blue_3);
        bbtn_last_month_02.setBackgroundResource(R.drawable.button_blue_3);
        pbbtn.setBackgroundResource(R.drawable.button_blue_4);

    }
    private void setEmpty(){
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(0f);
        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    public String getParamSelected(){
        return mParamSelected;
    }

    public String getEventID(){
        return mEventID;
    }
}
