package g.rezza.moch.kiospos.lib;

import java.lang.ref.WeakReference;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class BackgroundHandler extends Handler {
    
	private final WeakReference<Activity> mActivity; 
    
	public BackgroundHandler(Activity p_ha) {
    	mActivity = new WeakReference<Activity>(p_ha);
    }
    
	@Override
	public void handleMessage(Message p_message) {
		Activity activity = mActivity.get();
		if (activity != null) {
			handleMessage(activity, p_message);
		}
	}

    public void handleMessage(Activity p_activity, Message p_message) {
    	
    }
	
}