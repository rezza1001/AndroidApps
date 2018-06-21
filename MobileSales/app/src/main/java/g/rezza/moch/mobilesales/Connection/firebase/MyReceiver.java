package g.rezza.moch.mobilesales.Connection.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Parse;

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
        if (datapassed.equals("Token")){
            String token = arg1.getStringExtra("Token");
            Log.d("MyReceiver","TOKEN REFRESH >>  "+ token);
            UserDB userDB = new UserDB();
            userDB.getMine(arg0);
            userDB.updateFcmToken(arg0,token);

        }
        else {
            if (mListener != null){
                try {
                    updateBalance(arg0);
                    JSONObject data = new JSONObject(datavalue);
                    mListener.onReceive(datapassed, data,title);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MyReceiver" , e.getMessage());
                }

            }
        }


    }

    private void updateBalance(final Context context){
        PostManager pos = new PostManager(context);
        pos.setApiUrl("check-balance");
        ArrayList<KeyValueHolder> mHolders = new ArrayList<>();
        pos.setData(mHolders);
        pos.showloading(false);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    try {
                        JSONObject jData = obj.getJSONArray("DATA").getJSONObject(0);
                        String balance = Parse.toCurrnecy(jData.getLong("BALANCE"));
                        BalanceDB balancedb = new BalanceDB();
                        balancedb.getData(context);
                        balancedb.balance = jData.getLong("BALANCE");
                        balancedb.insert(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private OnReceiveListener mListener;
    public void setOnReceiveListener(OnReceiveListener onReceiveListener){
        mListener = onReceiveListener;
    }
    public interface OnReceiveListener{
        public void onReceive(String body, JSONObject data, String title);
    }

}
