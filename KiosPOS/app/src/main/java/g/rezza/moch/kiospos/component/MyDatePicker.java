package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.R;


/**
 * Created by Rezza on 3/6/17.
 */

public class MyDatePicker extends MasterComponent implements View.OnFocusChangeListener {
    private EditText edtx_date_30;
    private EditText edtx_month_31;
    private EditText edtx_year_32;
    private ImageView imvw_icon_30;
    private TextView txvw_title_10;

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mydatepicker,this,true);
        edtx_date_30   = (EditText) findViewById(R.id.edtx_date_30);
        edtx_month_31  = (EditText) findViewById(R.id.edtx_month_31);
        edtx_year_32   = (EditText) findViewById(R.id.edtx_year_32);
        imvw_icon_30   = (ImageView)   findViewById(R.id.imvw_icon_30);
        txvw_title_10   = (TextView)    findViewById(R.id.txvw_title_10);


        edtx_date_30.setOnFocusChangeListener(this);
        edtx_month_31.setOnFocusChangeListener(this);
        edtx_year_32.setOnFocusChangeListener(this);
        this.requestFocus();
        imvw_icon_30.setImageResource(R.drawable.ic_date_off);
        edtx_date_30.addTextChangedListener(new TextWatcher() {
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
                            edtx_date_30.setText("");
                        }
                        if (s.toString().length() == 2){
                            edtx_month_31.requestFocus();
                        }
                    }catch (Exception e){
                        edtx_date_30.setText("");
                    }
                }
                if (mListener != null){
                    mListener.after(s.toString());
                }
            }
        });
        edtx_month_31.addTextChangedListener(new TextWatcher() {
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
                            edtx_month_31.setText("");
                        }
                        if (s.toString().length() == 2){
                            edtx_year_32.requestFocus();
                        }
                    }catch (Exception e){
                        edtx_month_31.setText("");
                    }
                }
                if (mListener != null){
                    mListener.after(s.toString());
                }
            }
        });
        edtx_year_32.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!s.toString().equals("")){
                        int x = Integer.parseInt(s.toString());
                        if (x > 2000){
                            Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
                            edtx_year_32.setText("");
                        }
                        else if (s.length() == 4 && x<1945){
                            Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
                            edtx_year_32.setText("");
                        }
                    }
                }catch (Exception e){
                    edtx_year_32.setText("");
                }
                if (mListener != null){
                    mListener.after(s.toString());
                }
            }
        });

    }

    public void setFocus(){
        edtx_date_30.requestFocus();
    }

    public MyDatePicker(Context context) {
        super(context, null);
    }

    public void setDateTitle(String pText){
        edtx_date_30.setHint(pText);
    }
    public void setDateValue(String pText){
        edtx_date_30.setText(pText);
    }

    public void setValue(String pDate){
        edtx_date_30.setText(pDate.split("/")[0]);
        edtx_month_31.setText(pDate.split("/")[1]);
        edtx_year_32.setText(pDate.split("/")[2]);
    }

    public void disable(){
        edtx_date_30.setEnabled(false);
        edtx_year_32.setEnabled(false);
        edtx_month_31.setEnabled(false);
    }

    public void setMonthTitle(String pText){
        edtx_month_31.setHint(pText);
    }
    public void setMonthValue(String month){
        edtx_month_31.setText(month);
    }
    public void setYearTitle(String pText){
        edtx_year_32.setHint(pText);
    }
    public void setYearValue(String year){
        edtx_year_32.setText(year);
    }

    public void setTitle(String pText) {
        txvw_title_10.setText(pText);
    }

    public String getValue(){
        String date = edtx_date_30.getText().toString();
        String month = edtx_month_31.getText().toString();
        String year = edtx_year_32.getText().toString();
        String x = "";
        try {
            int dateInt = Integer.parseInt(date);
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            x = dateInt + "/" + monthInt + "/" + yearInt;
        }catch (Exception e){
            x = "";
        }
        return x;
    }

    public ChangeListener mListener;
    public void setOnChangeListener(ChangeListener pListener){
        mListener = pListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            imvw_icon_30.setImageResource(R.drawable.ic_date_on);
        }
        else {
            imvw_icon_30.setImageResource(R.drawable.ic_date_off);
        }

        if (v.equals(edtx_year_32)){
            if (!hasFocus) {
                String s = edtx_year_32.getText().toString();
                if (!s.equals("")){
                    int x = Integer.parseInt(s);
                    if (x > 2000){
                        Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
                        edtx_year_32.setText("");
                    }
                    else if (x<1945){
                        Toast.makeText(getContext(),"Year of birth must be between 1945-2000", Toast.LENGTH_LONG).show();
                        edtx_year_32.setText("");
                    }
                }
            }
        }
    }

}
