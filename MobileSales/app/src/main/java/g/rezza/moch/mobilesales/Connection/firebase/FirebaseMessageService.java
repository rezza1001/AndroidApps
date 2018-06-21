package g.rezza.moch.mobilesales.Connection.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by rezza on 29/01/18.
 */

public class FirebaseMessageService extends FirebaseMessagingService{
    private static final String TAG = "FirebaseMsgService";


    public final static String MY_ACTION = "MY_ACTION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("data"));
        Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("Message", remoteMessage.getNotification().getBody());
        intent.putExtra("Data", remoteMessage.getData().get("data"));
        intent.putExtra("Title", remoteMessage.getNotification().getTitle());
        sendBroadcast(intent);
    }
}
