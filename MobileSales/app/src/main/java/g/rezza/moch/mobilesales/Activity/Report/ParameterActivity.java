package g.rezza.moch.mobilesales.Activity.Report;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.MyDatePicker;
import g.rezza.moch.mobilesales.component.MySimpleSpinner;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.holder.SpinerHolder;

public class ParameterActivity extends AppCompatActivity{

    private ImageView imvw_back_00;
    private MyDatePicker    dpck_start_00;
    private MyDatePicker    dpck_end_00;
    private MySimpleSpinner spnr_store_00;
    private MySimpleSpinner spnr_status_00;
    private Button          bbtn_action_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        imvw_back_00    = (ImageView)         findViewById(R.id.imvw_back_00);
        dpck_start_00   = (MyDatePicker)      findViewById(R.id.dpck_start_00);
        dpck_end_00     = (MyDatePicker)      findViewById(R.id.dpck_end_00);
        spnr_store_00   = (MySimpleSpinner)     findViewById(R.id.spnr_store_00);
        spnr_status_00  = (MySimpleSpinner)  findViewById(R.id.spnr_status_00);
        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);

        dpck_end_00.disable();

        Calendar defaultCal = Calendar.getInstance();
        dpck_start_00.setValue(defaultCal.getTime());
        dpck_end_00.setValue(defaultCal.getTime());
        dpck_start_00.setOnChangeListener(new MyDatePicker.ChangeListener() {
            @Override
            public void after(String s) {
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                try {
                    calendar.setTime(dateFormat2.parse(s));
                    dpck_end_00.setMinimum(calendar);

                    Calendar max = Calendar.getInstance();
                    max.setTime(dateFormat2.parse(s));
                    max.add(Calendar.MONTH, 3);
                    dpck_end_00.setMaximum(max);
                    dpck_end_00.setValue(calendar.getTime());
                    dpck_end_00.enable();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dpck_end_00.setOnChangeListener(new MyDatePicker.ChangeListener() {
            @Override
            public void after(String s) {
                DateFormat dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(dateFormat2.parse(s));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final ArrayList<SpinerHolder> spnr_status = new ArrayList<>();
        spnr_status.add(new SpinerHolder("-99","All Status",0));
        spnr_status.add(new SpinerHolder("1","New",0));
        spnr_status.add(new SpinerHolder("6","Pending",0));
        spnr_status.add(new SpinerHolder("4","Success",0));
        spnr_status.add(new SpinerHolder("5","Reject",0));
        spnr_status_00.setChoosers(spnr_status);

        loadData();

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent output = new Intent();
                output.putExtra("START_DATE", dpck_start_00.getValue());
                output.putExtra("END_DATE", dpck_end_00.getValue());
                output.putExtra("STORE", spnr_store_00.getValue().key);
                output.putExtra("STATUS", spnr_status_00.getValue().key);
                setResult(1, output);
                finish();
            }
        });
    }

    private void loadData(){
        final ArrayList<SpinerHolder> spnr_status = new ArrayList<>();
        spnr_status.add(new SpinerHolder("-99","All Store",0));
        spnr_store_00.setChoosers(spnr_status);
        PostManager pos = new PostManager(this);
        pos.setApiUrl("list-store");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONArray arr_store = obj.getJSONArray("DATA");
                        for (int i=0; i<arr_store.length(); i++){
                            spnr_status.add(new SpinerHolder(arr_store.getJSONObject(i).getString("USER"),arr_store.getJSONObject(i).getString("COMPANY"), 0));
                        }
                        spnr_store_00.setChoosers(spnr_status);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(-1);
    }

}
