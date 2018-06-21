package g.rezza.moch.mobilesales.Connection.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by rezza on 29/01/18.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    public final static String MY_ACTION = "MY_TOKEN";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer : "+ token);
        // Add custom implementation, as needed.

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("Message", "Token");
        intent.putExtra("Token", token);
        sendBroadcast(intent);
    }
}
