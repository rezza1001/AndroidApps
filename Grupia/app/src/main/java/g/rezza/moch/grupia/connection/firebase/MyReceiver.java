package g.rezza.moch.grupia.connection.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rezza on 07/02/18.
 */

public class MyReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub

        String datapassed = arg1.getStringExtra("Message");
        String datavalue  = arg1.getStringExtra("Data");
        String title      = arg1.getStringExtra("Title");
        if (mListener != null){
            try {
                JSONObject data = new JSONObject(datavalue);
                mListener.onReceive(datapassed, data,title);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("MyReceiver" , e.getMessage());
            }

        }


    }



    private OnReceiveListener mListener;
    public void setOnReceiveListener(OnReceiveListener onReceiveListener){
        mListener = onReceiveListener;
    }
    public interface OnReceiveListener{
        public void onReceive(String body, JSONObject data, String title);
    }

}
