package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.holder.KeyValueHolder;


/**
 * Created by rezza on 02/01/18.
 */

public class EditextAutoComplateBlack extends RelativeLayout implements AdapterView.OnItemClickListener{

    private AutoCompleteTextView edtx_00;
    private TextInputLayout txip_00;
    private TextView txvw_mandatory_00;
    private TextView txvw_message_00;
    private HashMap<String, String> MAP_DATA = new HashMap<>();

    public EditextAutoComplateBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_editext_autocomplate_black, this, true);
        createLayout();
    }

    private void createLayout(){
        edtx_00 = (AutoCompleteTextView) findViewById(R.id.edtx_00);
        txip_00 = (TextInputLayout) findViewById(R.id.txip_00);
        txvw_mandatory_00
                = (TextView)    findViewById(R.id.txvw_mandatory_00);
        txvw_message_00
                = (TextView)    findViewById(R.id.txvw_message_00);
        txvw_mandatory_00.setVisibility(View.INVISIBLE);
        txvw_message_00.setVisibility(View.INVISIBLE);


        edtx_00.setOnItemClickListener(this);



    }

    public void setData(ArrayList<KeyValueHolder> datas){
        MAP_DATA.clear();
        String [] arrData = new String[datas.size()];
        for(int i=0; i<datas.size(); i++){
            arrData[i]= datas.get(i).getValue();
            MAP_DATA.put(datas.get(i).getValue(), datas.get(i).getKey());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrData);
        edtx_00.setAdapter(adapter);

    }

    public void setTitle(String title){
        txip_00.setHint(title);
    }
    public void setValue(String value){
        edtx_00.setText(getStringNotNull(value));
        if (pListener != null){
            pListener.onchange(new KeyValueHolder( MAP_DATA.get(edtx_00.getText().toString()),edtx_00.getText().toString()));
        }
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

    public String getKey(){
        if (MAP_DATA.get(getValue()) != null){
            return MAP_DATA.get(getValue());
        }
        else {
            return  edtx_00.getText().toString();
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

    private OnChangeListener pListener;
    public void setOnChangeListener(OnChangeListener onChangeListener){
        pListener = onChangeListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (pListener != null){
            pListener.onchange(new KeyValueHolder( MAP_DATA.get(edtx_00.getText().toString()),edtx_00.getText().toString()));
        }
    }

    public interface OnChangeListener{
        public void onchange(KeyValueHolder data);
    }
}
