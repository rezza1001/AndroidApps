package g.rezza.moch.clientdashboard.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.json.JSONObject;

/**
 * Created by Rezza on 10/11/17.
 */

public class MasterView extends RelativeLayout {
    public MasterView(Context context) {
        super(context);
    }

    public MasterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected OnReceiveListener mOnReceiveListener;
    public void setOnReceiveListener(OnReceiveListener pOnReceiveListener){
        mOnReceiveListener = pOnReceiveListener;
    }
    public interface OnReceiveListener{
        public void onReceive(MasterView v, String response);
    }
}
