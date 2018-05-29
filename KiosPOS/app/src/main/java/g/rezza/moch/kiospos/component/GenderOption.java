package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.R;

/**
 * Created by Rezza on 10/12/17.
 */

public class GenderOption extends MasterComponent {
    private ImageView   imvw_icon_30;
    private TextView    txvw_title_10;
    private RadioGroup  rdg_gender_30;
    private RadioButton rd_male_40;
    private RadioButton rd_female_41;

    public GenderOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_gender_opt, this, true);
        imvw_icon_30    = (ImageView)   findViewById(R.id.imvw_icon_30);
        txvw_title_10   = (TextView)    findViewById(R.id.txvw_title_10);
        rdg_gender_30   = (RadioGroup)  findViewById(R.id.rdg_gender_30);
        rd_male_40      = (RadioButton) findViewById(R.id.rd_male_40);
        rd_female_41    = (RadioButton) findViewById(R.id.rd_female_41);

        rdg_gender_30.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mListener != null){
                    RadioButton rbselected  = (RadioButton) findViewById(checkedId);
                    String x = rbselected.getText().toString();
                    mListener.after(x);
                }
            }
        });

        imvw_icon_30.setImageResource(R.drawable.ic_check_off);
    }


    public GenderOption(Context context) {
        super(context, null);
    }

    public void setTitle(String pText) {
        txvw_title_10.setText(pText);
    }

    public void setValue(String pText) {
        if (pText.equals("Male")){
            rd_male_40.setChecked(true);
        }
        else {
            rd_female_41.setChecked(true);
        }
    }

    public String getValue() {
        int selectedId          = rdg_gender_30.getCheckedRadioButtonId();
        RadioButton rbselected  = (RadioButton) findViewById(selectedId);
        try {
            String x = rbselected.getText().toString();
            return x;
        }catch (Exception e){
            return "";
        }
    }


    public void disable() {
        rd_female_41.setEnabled(false);
        rd_male_40.setEnabled(false);
    }

    public void enable() {
        rd_female_41.setEnabled(true);
        rd_male_40.setEnabled(true);
    }



}
