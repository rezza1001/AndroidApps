package g.rezza.moch.kiostixv3.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rezza on 19/02/18.
 */

public class MyViewPagerHome extends ViewPager {
    private static String TAG  = "MyViewPager";
    private boolean iswitch = true;
    public MyViewPagerHome(Context context) {
        super(context);
    }

    public MyViewPagerHome(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void disableSwipe(){
        iswitch = false;
    }

    public void enableSwipe(){
        iswitch = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
