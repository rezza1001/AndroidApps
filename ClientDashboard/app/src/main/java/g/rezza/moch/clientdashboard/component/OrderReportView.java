package g.rezza.moch.clientdashboard.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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
import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;

/**
 * Created by Rezza on 10/3/17.
 */

public class OrderReportView extends RelativeLayout {
    public static final String TAG = "RZ OrderReportView";

    private LineChart       chrt_order_04;
    private YAxis           leftAxisOrder;
    int                     maxHeight = 0;
    private String          mEventID = "";
    private ProgressDialog  mProgress;
    private HashMap<Integer,String> MAPS_LEGEND = new HashMap<>();

    public OrderReportView(Context context) {
        super(context);
    }

    public OrderReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_order_report, this, true);
        chrt_order_04 = (LineChart) findViewById(R.id.chrt_order_04);
        bulidOrderChart();
    }

    public void create(){
        ParameterHolder parameter = new ParameterHolder();
        parameter.getData(getContext());
       loadToServer(parameter.event_id, parameter.paramter);
    }

    private void bulidOrderChart(){
        chrt_order_04.setDrawGridBackground(false);
        chrt_order_04.getDescription().setEnabled(false);
        chrt_order_04.setTouchEnabled(true);
        chrt_order_04.setDragEnabled(true);
        chrt_order_04.setScaleEnabled(true);
        chrt_order_04.setPinchZoom(true);
        // to use for it
        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.component_custom_marker_view);
        mv.setChartView(chrt_order_04); // For bounds control
        chrt_order_04.setMarker(mv); // Set the marker to the chart

        leftAxisOrder = chrt_order_04.getAxisLeft();
        leftAxisOrder.removeAllLimitLines();
        leftAxisOrder.setAxisMaximum(10000f);
        leftAxisOrder.setAxisMinimum(0f);
        leftAxisOrder.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxisOrder.enableGridDashedLine(10f, 10f, 0f);
        leftAxisOrder.setDrawZeroLine(false);
        leftAxisOrder.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ("$"+value).split("\\.")[0];
            }
        });

        leftAxisOrder.setDrawLimitLinesBehindData(true);
        chrt_order_04.getAxisRight().setEnabled(false);

        Legend l = chrt_order_04.getLegend();
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

    }
    private void setFormat(int type) {
        final XAxis xAxis = chrt_order_04.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        leftAxisOrder.setAxisMaximum(Float.parseFloat((maxHeight+10)+""));
        IAxisValueFormatter format = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Integer x = Integer.parseInt(("" + value).split("\\.")[0]);
                return MAPS_LEGEND.get(x);
            }
        };
        xAxis.setValueFormatter(format);
    }

    private void loadToServer(String event_id, String type){
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage(AlertMessage.getMessage(AlertMessage.PLEASE_WAIT, AlertMessage.ENGLISH));
        mProgress.show();

        LongRunningGetIO client2 = new LongRunningGetIO(getContext());
        client2.setUrl("mobile/dashboard/reportorder/");
        client2.addParam(new KeyValueHolder("event_id",event_id));
        client2.addParam(new KeyValueHolder("date_type",type));
        client2.execute();
        client2.setOnReceiveListener(new LongRunningGetIO.onReceiveListener() {
            @Override
            public void onReceive(Object obj) {
                String data = (String) obj;
                JSONObject jResponse = null;
                mProgress.dismiss();
                try {
                    jResponse = new JSONObject(data);
                    Log.d(TAG,jResponse.toString());
                    JSONArray ja        = jResponse.getJSONArray("body");

                    if (ja.length()== 0) {
                        setEmpty();
                    }
                    else {
                        ArrayList<Entry> values1 = createValueChart(ja);
                        setFormat(2);
                        setDataOrder(values1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setDataOrder(ArrayList<Entry> values1){
        chrt_order_04.animateY(1500);
        chrt_order_04.resetTracking();

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        LineDataSet set1 = new LineDataSet(values1, "# Ticket Sold");
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
        chrt_order_04.setData(data);
        chrt_order_04.invalidate();
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

    private void setEmpty(){
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(0f);
        // set data
        chrt_order_04.setData(data);
        chrt_order_04.invalidate();
    }
}
