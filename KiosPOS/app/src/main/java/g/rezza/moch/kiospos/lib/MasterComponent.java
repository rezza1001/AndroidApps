package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Rezza on 10/12/17.
 */

public class MasterComponent extends RelativeLayout {
    public MasterComponent(Context context) {
        super(context);
    }

    public MasterComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    protected ChangeListener mListener;

    public void setOnChangeListener(ChangeListener pListener) {
        mListener = pListener;
    }

    public interface ChangeListener {
        public void after(String s);
    }

}
