package g.rezza.moch.kiospos.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.ConfigHolder;


/**
 * Created by Rezza on 8/23/17.
 */

public class PopupConfig extends Dialog  {

    public static final String TAG = "PopupConfig";


    private MyEditextNumber        myedtx_ipaddress_30;
    private MyEditextNumber        myedtx_port_30;
    private Button                  bbtn_ok_01;

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
        myedtx_ipaddress_30     = (MyEditextNumber)     findViewById(R.id.myedtx_ipaddress_30);
        myedtx_port_30          = (MyEditextNumber)     findViewById(R.id.myedtx_port_30);
        bbtn_ok_01              = (Button)              findViewById(R.id.bbtn_ok_01);

        create();

    }

    public void create(){
        myedtx_ipaddress_30.setTitle("IP Address");
        myedtx_port_30.setTitle("Port");

        final ConfigHolder configHolder = new ConfigHolder();
        configHolder.getData(getContext());
        if (!configHolder.ip_address.isEmpty()){
            myedtx_ipaddress_30.setValue(configHolder.ip_address);
            myedtx_port_30.setValue(configHolder.port);
        }

        bbtn_ok_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupConfig.this.dismiss();
                configHolder.clearData(getContext());
                configHolder.ip_address = myedtx_ipaddress_30.getValue();
                configHolder.port       = myedtx_port_30.getValue();
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
