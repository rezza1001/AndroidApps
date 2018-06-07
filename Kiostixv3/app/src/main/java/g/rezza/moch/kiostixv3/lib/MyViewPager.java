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

public class MyViewPager extends ViewPager {
    private static String TAG  = "MyViewPager";
    private boolean iswitch = true;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;

        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
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
        if (iswitch){
            return super.onTouchEvent(ev);
        }
        else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (iswitch){
            return super.onInterceptTouchEvent(ev);
        }
        else {
            return false;
        }
    }
}
