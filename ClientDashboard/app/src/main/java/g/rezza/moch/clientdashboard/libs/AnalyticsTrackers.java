package g.rezza.moch.clientdashboard.libs;

import android.content.Context;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import g.rezza.moch.clientdashboard.R;

/**
 * Created by Rezza on 9/25/17.
 */

public final class AnalyticsTrackers {
//    public enum Target {
//        APP,
//        // Add more trackers here if you need, and update the code in #get(Target) below
//    }
//
//    private static AnalyticsTrackers sInstance;
//    private final Map<Target, Tracker> mTrackers = new HashMap<>();
//    private final Context mContext;
//
//    private AnalyticsTrackers(Context context) {
//        mContext = context.getApplicationContext();
//    }
//
//    public static synchronized void initialize(Context context) {
//        if (sInstance != null) {
//            throw new IllegalStateException("Extra call to initialize analytics trackers");
//        }
//
//        sInstance = new AnalyticsTrackers(context);
//    }
//
//    public static synchronized AnalyticsTrackers getInstance() {
//        if (sInstance == null) {
//            throw new IllegalStateException("Call initialize() before getInstance()");
//        }
//
//        return sInstance;
//    }
//
//    public synchronized Tracker get(Target target) {
//        if (!mTrackers.containsKey(target)) {
//            Tracker tracker;
//            switch (target) {
//                case APP:
//                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unhandled analytics target " + target);
//            }
//            mTrackers.put(target, tracker);
//        }
//
//        return mTrackers.get(target);
//    }

}
