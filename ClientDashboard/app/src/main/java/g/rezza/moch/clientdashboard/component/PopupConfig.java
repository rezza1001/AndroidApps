package g.rezza.moch.clientdashboard.component;

import android.app.Dialog;
import android.content.Context;
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
import g.rezza.moch.clientdashboard.holder.ConfigHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.Utils;


/**
 * Created by Rezza on 8/23/17.
 */

public class PopupConfig extends Dialog  {

    public static final String TAG = "PopupConfig";


    private EditText        edtx_ipaddress_02;
    private EditText        edtx_port_02;
    private Button          bbtn_ok_01;

    private String current  = "";
    private String ddmmyyyy = "DDMMYYYY";
    private Calendar cal    = Calendar.getInstance();

    private int type        = 1; // 1 = Default, 2 = Range
    private String value    = "";

    public PopupConfig(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_popup_config);
        edtx_ipaddress_02   = (EditText)    findViewById(R.id.edtx_ipaddress_02);
        edtx_port_02        = (EditText)    findViewById(R.id.edtx_port_02);
        bbtn_ok_01          = (Button)      findViewById(R.id.bbtn_ok_01);

        create();

    }

    public void create(){
        final ConfigHolder configHolder = new ConfigHolder();
        configHolder.getData(getContext());
        if (!configHolder.ip_address.isEmpty()){
            edtx_ipaddress_02.setText(configHolder.ip_address);
            edtx_port_02.setText(configHolder.port);
        }

        bbtn_ok_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupConfig.this.dismiss();
                configHolder.clearData(getContext());
                configHolder.ip_address = edtx_ipaddress_02.getText().toString();
                configHolder.port       = edtx_port_02.getText().toString();
                configHolder.insert(getContext());
                if (mListener != null){
                    mListener.onClick(configHolder);
                }
            }
        });
    }


    private OnPositifClickListener mListener;
    public void setOnPositifClickListener(OnPositifClickListener positifClickListener){
        mListener = positifClickListener;
    }

    /*
     * Interface Listener
     */
    public interface OnPositifClickListener{
        public void onClick(ConfigHolder configHolder);
    }

}
