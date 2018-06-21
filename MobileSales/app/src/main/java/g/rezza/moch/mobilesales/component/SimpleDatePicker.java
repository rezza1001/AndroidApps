package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 03/01/18.
 */

public class SimpleDatePicker extends RelativeLayout  {

    private EditText edtx_dd_00;
    private EditText edtx_mm_00;
    private EditText edtx_yy_00;
    private TextView txvw_title_00;
    private TextView txvw_mandatory_00;

    public SimpleDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_simple_datepicker,this,true);
        createLayout();
        registerListener();
    }

    private void createLayout(){
        edtx_dd_00      = (EditText) findViewById(R.id.edtx_dd_00);
        edtx_mm_00      = (EditText) findViewById(R.id.edtx_mm_00);
        edtx_yy_00      = (EditText) findViewById(R.id.edtx_yy_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        txvw_mandatory_00   = (TextView) findViewById(R.id.txvw_mandatory_00);

        txvw_mandatory_00.setVisibility(View.INVISIBLE);
    }

    public void setTitle(String title){
        txvw_title_00.setText(title);
    }
    public void setValue(String value){
        try {
            edtx_dd_00.setText(value.split("-")[0]);
            edtx_mm_00.setText(value.split("-")[1]);
            edtx_yy_00.setText(value.split("-")[2]);
        }catch (Exception e){

        }

    }
    public String getValue(){
        String date     = edtx_dd_00.getText().toString();
        String month    = edtx_mm_00.getText().toString();
        String year     = edtx_yy_00.getText().toString();
        String x = "";
        try {
            int dateInt = Integer.parseInt(date);
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);

            x = (dateInt< 10 ? "0"+dateInt: dateInt) + "-" + (monthInt< 10 ? "0"+monthInt: monthInt) + "-" + yearInt;
        }catch (Exception e){
            x = "";
        }
        return x;
    }

    public String getValueToServer(){
        String date     = edtx_dd_00.getText().toString();
        String month    = edtx_mm_00.getText().toString();
        String year     = edtx_yy_00.getText().toString();
        String x = "";
        try {
            int dateInt = Integer.parseInt(date);
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);

            x =yearInt + "-" + (monthInt< 10 ? "0"+monthInt: monthInt) + "-" + (dateInt< 10 ? "0"+dateInt: dateInt) ;
        }catch (Exception e){
            x = "";
        }
        return x;
    }

    public void setReadOnly(boolean readonly){
        if (readonly){
            doReadOnly(edtx_dd_00);
            doReadOnly(edtx_mm_00);
            doReadOnly(edtx_yy_00);
        }
    }

    public void setMandatory(boolean mandatory){
        if(mandatory){
            txvw_mandatory_00.setVisibility(View.VISIBLE);
        }
        else {
            txvw_mandatory_00.setVisibility(View.INVISIBLE);
        }
    }

    private void doReadOnly(EditText edtx){
        edtx.setKeyListener(null);
        edtx.setFocusable(false);
        edtx.setClickable(true);
    }

    private void registerListener(){
        edtx_dd_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    try {
                        int x = Integer.parseInt(s.toString());
                        if (x > 31){
                            edtx_dd_00.setText("");
                        }
                        if (s.toString().length() == 2){
                            edtx_mm_00.requestFocus();
                        }
                    }catch (Exception e){
                        edtx_dd_00.setText("");
                    }
                }

            }
        });
        edtx_mm_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")){
                    try {
                        int x = Integer.parseInt(s.toString());
                        if (x > 12){
                            edtx_mm_00.setText("");
                        }
                        if (s.toString().length() == 2){
                            edtx_yy_00.requestFocus();
                        }
                    }catch (Exception e){
                        edtx_mm_00.setText("");
                    }
                }
            }
        });
        edtx_yy_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                try {
//                    if (!s.toString().equals("")){
//                        int x = Integer.parseInt(s.toString());
//                        if (x > 2000){
//                            Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
//                            edtx_yy_00.setText("");
//                        }
//                        else if (s.length() == 4 && x<1945){
//                            Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
//                            edtx_yy_00.setText("");
//                        }
//                    }
//                }catch (Exception e){
//                    edtx_yy_00.setText("");
//                }

            }
        });
    }

}
