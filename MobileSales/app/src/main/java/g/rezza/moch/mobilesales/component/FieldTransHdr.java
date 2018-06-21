package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 04/01/18.
 */

public class FieldTransHdr extends RelativeLayout {

    private TextView txvw_key_00;
    private TextView txvw_value_00;
    public FieldTransHdr(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_field_transhdr, this, true);

        txvw_key_00     = (TextView) findViewById(R.id.txvw_key_00);
        txvw_value_00   = (TextView) findViewById(R.id.txvw_value_00);
    }

    public void setTitle(String title){
        txvw_key_00.setText(title);
    }


    public void setValue(String value){
        txvw_value_00.setText(value);
    }

    public String getValue(){
        return txvw_value_00.getText().toString();
    }

    public void setValue(CharSequence value){
        txvw_value_00.setText(value);
    }


    public void setTextStyle(int pStyle){
        if (Build.VERSION.SDK_INT < 23) {
            txvw_value_00.setTextAppearance(getContext(), pStyle);
        } else {
            txvw_value_00.setTextAppearance(pStyle);
        }
    }

}
