package g.rezza.moch.mobilesales.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 28/12/17.
 */

public class EventQtyChooserDialog extends Dialog {

    private TextView txvw_title_00;
    private MyChooserQty cqty_adult_00;
    private MyChooserQty cqty_child_00;
    private Button  bbtn_action_00;

    public EventQtyChooserDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_event_chooser);

        txvw_title_00 = (TextView) findViewById(R.id.txvw_title_00);
        cqty_adult_00 = (MyChooserQty) findViewById(R.id.cqty_adult_00);
        cqty_child_00 = (MyChooserQty) findViewById(R.id.cqty_child_00);
        bbtn_action_00= (Button) findViewById(R.id.bbtn_action_00);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onSubmit(getValueChild(), getValueAdult());
                }
                EventQtyChooserDialog.this.dismiss();
            }
        });
    }

    public void setTitle(String title){
        txvw_title_00.setText(title);
    }

    public void setValueChild(String value){
        int x = 0;
        try {
            x = Integer.parseInt(value);
        }catch (Exception e){
        }
        cqty_child_00.setValue(x);
    }

    public void setValueAdult(String value){
        int x = 0;
        try {
            x = Integer.parseInt(value);
        }catch (Exception e){
        }
        cqty_adult_00.setValue(x);
    }

    public int getValueChild(){
        int x = 0;
        try {
            x = cqty_child_00.getValue();
        }catch (Exception e){
        }
        return x;
    }

    public int getValueAdult(){
        int x = 0;
        try {
            x = cqty_adult_00.getValue();
        }catch (Exception e){
        }
        return x;
    }

    private OnSubmitListener mListener;
    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        mListener = onSubmitListener;
    }
    public interface OnSubmitListener{
        public void onSubmit(int child, int adult);
    }
}
