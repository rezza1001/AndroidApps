package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 02/01/18.
 */

public class EditextCurrency extends RelativeLayout implements TextWatcher{

    private String TAG = "EditextCurrency";

    private EditText edtx_00;
    private TextInputLayout txip_00;
    private TextView txvw_mandatory_00;
    private TextView txvw_message_00;
    private String current = "";


    public EditextCurrency(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_editext_standard, this, true);
        createLayout();
    }

    private void createLayout(){
        edtx_00 = (EditText) findViewById(R.id.edtx_00);
        txip_00 = (TextInputLayout) findViewById(R.id.txip_00);
        txvw_mandatory_00
                = (TextView)    findViewById(R.id.txvw_mandatory_00);
        txvw_message_00
                = (TextView)    findViewById(R.id.txvw_message_00);
        txvw_mandatory_00.setVisibility(View.INVISIBLE);
        txvw_message_00.setVisibility(View.INVISIBLE);

        edtx_00.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_CLASS_NUMBER);

        edtx_00.addTextChangedListener(this);
    }

    public void setTitle(String title){
        txip_00.setHint(title);
    }
    public void setValue(String value){
        edtx_00.setText(getStringNotNull(value));
    }
    public String getValue(){
        try {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("id"));
            String data = edtx_00.getText().toString().replaceAll("\\.","");
            return data;
        }catch (Exception e){
            Log.e("TAG",e.getMessage());
            return "0";
        }
    }

    public void setReadOnly(boolean readonly){
        if (readonly){
            edtx_00.setKeyListener(null);
            edtx_00.setFocusable(false);
            edtx_00.setClickable(true);
            txvw_mandatory_00.setVisibility(View.INVISIBLE);
        }
    }

    public void setID(String id){

    }

    public void setMandatory(boolean mandatory){
        if(mandatory){
            txvw_mandatory_00.setVisibility(View.VISIBLE);
        }
        else {
            txvw_mandatory_00.setVisibility(View.INVISIBLE);
        }
    }

    public void showNotif(String message){
        txvw_message_00.setText(message);
        txvw_message_00.setVisibility(View.VISIBLE);
        messageHandler.sendEmptyMessageDelayed(1, 5000);
    }


    private String toCurrnecy(String pData){
        try {
            double dData = Double.parseDouble(pData);
            NumberFormat formatKurensi = NumberFormat.getCurrencyInstance(new Locale("id"));
            String data = formatKurensi.format(dData);
            data = data.substring(1,(data.length()-3));
            return data;
        }catch (Exception e){
            return "0";
        }

    }

    public void setTextStyle(int pStyle){
        if (Build.VERSION.SDK_INT < 23) {
            edtx_00.setTextAppearance(getContext(), pStyle);
        } else {
            edtx_00.setTextAppearance(pStyle);
        }
    }

    public String getStringNotNull(String data){
        try {
            if (data.equalsIgnoreCase("NULL")){
                return "";
            }
            else {
                return data;
            }
        }catch (Exception e){
            return "";
        }

    }

    Handler messageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            txvw_message_00.setVisibility(View.INVISIBLE);
            return false;
        }
    });

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0)
            return;

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().equals(current)) {
            edtx_00.removeTextChangedListener(this);

            Locale local = new Locale("id", "id");
            String replaceable = String.format("[Rp,.\\s]",
                    NumberFormat.getCurrencyInstance().getCurrency()
                            .getSymbol(local));
            String cleanString = s.toString().replaceAll(replaceable,
                    "");

            double parsed;
            try {
                parsed = Double.parseDouble(cleanString);
            } catch (NumberFormatException e) {
                parsed = 0.00;
            }

            NumberFormat formatter = NumberFormat
                    .getCurrencyInstance(local);
            formatter.setMaximumFractionDigits(0);
            formatter.setParseIntegerOnly(true);
            String formatted = formatter.format((parsed));

            String replace = String.format("[Rp\\s]",
                    NumberFormat.getCurrencyInstance().getCurrency()
                            .getSymbol(local));
            String clean = formatted.replaceAll(replace, "");

            current = formatted;
            edtx_00.setText(clean);
            edtx_00.setSelection(clean.length());
            edtx_00.addTextChangedListener(this);


        }
    }

    private OnchangeListener pOnchangeListener;
    public void setOnChangeListener(OnchangeListener onChangeListener){
        pOnchangeListener = onChangeListener;
    }

    public interface OnchangeListener{
        public void onchange(View v, long nominal);
    }
}
