package g.rezza.moch.clientdashboard.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.Utils;


/**
 * Created by Rezza on 8/23/17.
 */

public class PopupParameter extends Dialog implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "PopupParameter";

    private MyCombobox      mycb_param_01;
    private RadioButton     rb_default_01;
    private RadioButton     rb_range_01;
    private EditText        edtx_start_date_02;
    private EditText        edtx_end_date_02;
    private Button          bbtn_ok_01;
    private TextView        txvw_note_01;

    private String current  = "";
    private String ddmmyyyy = "DDMMYYYY";
    private Calendar cal    = Calendar.getInstance();

    private int type        = 1; // 1 = Default, 2 = Range
    private String value    = "";

    public PopupParameter(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_popup_parameter);
        mycb_param_01       = (MyCombobox)  findViewById(R.id.mycb_param_01);
        rb_default_01       = (RadioButton) findViewById(R.id.rb_default_01);
        rb_range_01         = (RadioButton) findViewById(R.id.rb_range_01);
        edtx_start_date_02  = (EditText)    findViewById(R.id.edtx_start_date_02);
        edtx_end_date_02    = (EditText)    findViewById(R.id.edtx_end_date_02);
        bbtn_ok_01          = (Button)      findViewById(R.id.bbtn_ok_01);
        txvw_note_01        = (TextView)    findViewById(R.id.txvw_note_01);

        create();

    }

    public void create(){
        createComboValue();
        createDateRange();

        rb_default_01.setOnCheckedChangeListener(this);
        rb_range_01.setOnCheckedChangeListener(this);
        bbtn_ok_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "valueValidation = "+ valueValidation()+ "");
                if (!valueValidation()){
                    return;
                }

                PopupParameter.this.dismiss();
                if (mListener != null){
                    mListener.onClick(type, getValue());
                }
            }
        });
    }

    private void createDateRange(){
        edtx_start_date_02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateValidation(edtx_start_date_02,s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtx_end_date_02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateValidation(edtx_end_date_02,s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtx_end_date_02.setEnabled(false);
        edtx_end_date_02.setText("");
        edtx_start_date_02.setEnabled(false);
        edtx_start_date_02.setText("");
        txvw_note_01.setVisibility(View.GONE);

    }

    private void createComboValue(){
        mycb_param_01.setTitle("Default Parameter");
        ArrayList<String> params = new ArrayList<>();
        params.add(Utils.getParameterValue("1"));
        params.add(Utils.getParameterValue("2"));
        params.add(Utils.getParameterValue("3"));
        params.add(Utils.getParameterValue("4"));
        params.add(Utils.getParameterValue("5"));

        mycb_param_01.setEnabled(false);
        mycb_param_01.setChoosers(params);

    }

    public void setValueChooset(int position){
        mycb_param_01.setValue(position);
    }

    private void dateValidation(EditText pComponent, CharSequence s, int start, int count, int after){

        if (!s.toString().equals(current) && !s.toString().isEmpty()) {
            int color = getContext().getResources().getColor(R.color.colorBlueBlack_02);
            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8 ){
                clean = clean + ddmmyyyy.substring(clean.length());
                color = getContext().getResources().getColor(R.color.colorred_01);
            }else {
                color = getContext().getResources().getColor(R.color.colorBlueBlack_02);
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900:(year>2100)?2100:year;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            pComponent.setText(current);
            pComponent.setTextColor(color);
            pComponent.setSelection(sel < current.length() ? sel : current.length());
        }
    }

    public void setValue(int type, String value){
        this.type   = type;
        this.value  = value;
    }

    public String getValue(){
        String data = "";
        if (type == 1){
            return mycb_param_01.getValuePosition()+"";
        }
        else {
            if (!edtx_start_date_02.getText().toString().isEmpty() && !edtx_end_date_02.getText().toString().isEmpty()){
                return edtx_start_date_02.getText().toString() +" - "+ edtx_end_date_02.getText().toString();
            }
            else {
                return "";
            }

        }
    }

    private boolean valueValidation(){
        if (type == 1){
            if (getValue().isEmpty()){
                Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.FIELD_REQUIRED_PARAMETER,
                        AlertMessage.INDONESIA), Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                return true;
            }
        }
        else {
            String date = getValue().trim();
            boolean status = true;
            if (date.isEmpty()){
                status = false;
                Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.FIELD_REQUIRED_PARAMETER,
                        AlertMessage.INDONESIA), Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i=0; i< date.length(); i++){
                    char crt = date.charAt(i);
                    if (!Character.toString(crt).equals("/") && !Character.toString(crt).equals("-") &&  !Character.toString(crt).equals(" ")){
                        try {
                            Integer.parseInt(Character.toString(crt));
                        }catch (Exception e){
                            Log.d(TAG, Character.toString(crt));
                            status = false;
                        }
                    }
                }
                if (status){
                    Date date_start = Utils.toDate(edtx_start_date_02.getText().toString(),"dd/MM/yyyy");
                    Date date_until = Utils.toDate(edtx_end_date_02.getText().toString(),"dd/MM/yyyy");
                    long diff       = Utils.getDifferenceDate(date_start, date_until);
                    Log.d(TAG, "DIFF DAYS "+ diff);

                    if (diff > 30 || diff < 0){
                        Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.RANGE_DATE_MUST_BE_30_DAYS,
                                AlertMessage.INDONESIA), Toast.LENGTH_SHORT).show();
                        status = false;
                    }

                }
                else {
                    Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.FIELD_REQUIRED_PARAMETER,
                            AlertMessage.INDONESIA), Toast.LENGTH_SHORT).show();
                }
            }
            return status;
        }
    }


    /*
     * Register Interface Listener
     */

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.rb_default_01) {
                rb_range_01.setChecked(false);
                mycb_param_01.setEnabled(true);
                edtx_end_date_02.setEnabled(false);
                edtx_end_date_02.setText("");
                edtx_start_date_02.setEnabled(false);
                edtx_start_date_02.setText("");
                txvw_note_01.setVisibility(View.GONE);
                type = 1;
            }
            if (buttonView.getId() == R.id.rb_range_01) {
                rb_default_01.setChecked(false);
                mycb_param_01.setEnabled(false);
                edtx_end_date_02.setEnabled(true);
                edtx_start_date_02.setEnabled(true);
                txvw_note_01.setVisibility(View.VISIBLE);
                createComboValue();
                type = 2;
            }
        }
    }

    private OnPositifClickListener mListener;
    public void setOnPositifClickListener(OnPositifClickListener positifClickListener){
        mListener = positifClickListener;
    }

    /*
     * Interface Listener
     */
    public interface OnPositifClickListener{
        public void onClick(int type, String value);
    }

}
