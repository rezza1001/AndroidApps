package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 10/12/17.
 */

public class StepComponent extends MasterComponent {

    private RelativeLayout  rvly_step_20;
    private RelativeLayout  rvly_step_30;
    private TextView        txvw_step_40;

    public StepComponent(Context context) {
        super(context);
    }

    public StepComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_step, this, true);
        rvly_step_20    = (RelativeLayout)  findViewById(R.id.rvly_step_20);
        rvly_step_30    = (RelativeLayout)  findViewById(R.id.rvly_step_30);
        txvw_step_40    = (TextView)        findViewById(R.id.txvw_step_40);
    }

    public void setNumberStep(int number){
        txvw_step_40.setText(number+"");
    }

    public void isEndStep(){
        rvly_step_20.setVisibility(View.GONE);
    }

    public void active(){
        txvw_step_40.setTextColor(getResources().getColor(R.color.colorWhite_00));
        rvly_step_30.setBackgroundResource(R.drawable.background_round_active_blue);
    }

    public void nonActive(){
        txvw_step_40.setTextColor(getResources().getColor(R.color.colorBlue_06));
        rvly_step_30.setBackgroundResource(R.drawable.background_round_nonactive_blue);
    }
}
