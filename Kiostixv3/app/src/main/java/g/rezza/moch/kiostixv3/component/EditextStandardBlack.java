package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;


/**
 * Created by rezza on 02/01/18.
 */

public class EditextStandardBlack extends RelativeLayout implements TextWatcher {

    private EditText edtx_00;
    private TextInputLayout txip_00;
    private TextView txvw_mandatory_00;
    private TextView txvw_message_00;

    public EditextStandardBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_editext_black, this, true);
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

        edtx_00.addTextChangedListener(this);
    }

    public void setMaxLength(int maxLength){
        edtx_00.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
    }

    public void setTitle(String title){
        txip_00.setHint(title);
    }
    public void setValue(String value){
        edtx_00.setText(getStringNotNull(value));
    }
    public String getValue(){
        return edtx_00.getText().toString();
    }

    public void setReadOnly(boolean readonly){
        if (readonly){
            edtx_00.setKeyListener(null);
            edtx_00.setFocusable(false);
            edtx_00.setClickable(true);
            txvw_mandatory_00.setVisibility(View.INVISIBLE);
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

    public void showNotif(String message){
        txvw_message_00.setText(message);
        txvw_message_00.setVisibility(View.VISIBLE);
        messageHandler.sendEmptyMessageDelayed(1, 5000);
    }

    public void setInputType(int inputType){
        edtx_00.setInputType(InputType.TYPE_CLASS_TEXT |inputType);
    }

    public void setInputType(int inputType1, int inputType2){
        edtx_00.setInputType(inputType1|inputType2);
    }

    public void setTextSizeC(float pSize){
        edtx_00.setTextSize(pSize);
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
        Log.d("TAGRZ","i "+ i+" | "+ "i1 "+ i1+ "| i2"+ i2);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mListener != null){
            mListener.onChange(getValue(), editable);
        }
    }

    public void setSelection(){
        edtx_00.setSelection(getValue().length());
    }

    public void setTag(String data){
        edtx_00.setTag(data);
    }

    public void removeTextChangedListener(){
        edtx_00.removeTextChangedListener(this);
    }

    public void addTextChenge(){
        edtx_00.addTextChangedListener(this);
    }

    public String getTag(){
        return edtx_00.getTag().toString();
    }

    private TextChangeListener mListener;
    public void setOnTextChangeListener(TextChangeListener onTextChangeListener){
        mListener = onTextChangeListener;
    }

    public interface TextChangeListener{
        public void onChange(String data, Editable editable);
    }


    private DeleteListener mDeleteListener;
    public void setOnDeleteListener(DeleteListener onDeleteListener){
        mDeleteListener = onDeleteListener;
    }

    public interface DeleteListener{
        public void onDelete();
        public void onOtherKey(KeyEvent keyEvent);
    }
}
