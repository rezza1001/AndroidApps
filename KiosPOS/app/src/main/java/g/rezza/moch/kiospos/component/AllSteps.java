package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;

import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.R;

/**
 * Created by Rezza on 10/12/17.
 */

public class AllSteps extends MasterComponent {

    private LinearLayout lnly_step_00;
    private ArrayList<StepComponent> mSteps = new ArrayList<>();

    public AllSteps(Context context) {
        super(context);
    }

    public AllSteps(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_all_step, this, true);
        lnly_step_00 = (LinearLayout) findViewById(R.id.lnly_step_00);
    }

    public void create(int countSteps){
        mSteps.clear();
        lnly_step_00.removeAllViews();
        for (int i=0; i<countSteps; i++){
            StepComponent st = new StepComponent(getContext(), null);
            mSteps.add(st);
            if (i == countSteps-1){
                st.isEndStep();
            }
            st.setNumberStep(i+1);
            lnly_step_00.addView(st);
        }
    }

    public void activeStep(int index){
        for (StepComponent sc: mSteps){
            sc.nonActive();
        }
        mSteps.get(index).active();
    }
}
