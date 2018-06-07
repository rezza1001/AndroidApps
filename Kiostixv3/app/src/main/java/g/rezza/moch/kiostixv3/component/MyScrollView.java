package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * Created by rezza on 21/02/18.
 */

public class MyScrollView extends ScrollView{

    private Runnable scrollerTask;
    private int initialPosition;
    private int newCheck = 100;


    public MyScrollView(Context context) {
        super(context);

        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void init(){
        scrollerTask = new Runnable() {

            public void run() {

                int newPosition = getScrollY();
                if(initialPosition - newPosition == 0){//has stopped

//                    if(onScrollStoppedListener!=null){
//
//                        onScrollStoppedListener.onScrollStopped();
//                    }

//                    Log.d("MyScrollView", "rock========stop");
                }else{
                    initialPosition = getScrollY();
                    MyScrollView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }


    public void startScrollerTask(){
        initialPosition = getScrollY();
        MyScrollView.this.postDelayed(scrollerTask, newCheck);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollListener != null){
            if(t > oldt){
//                Log.d("MyScrollView", "rock========up");
                mOnScrollListener.onUp();
            }else if(t < oldt){
                mOnScrollListener.onDown();
//                Log.d("MyScrollView", "rock========down");
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                startScrollerTask();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private OnScrollListener mOnScrollListener;
    public void setOnScrollListener(OnScrollListener pOnScrollListener){
        mOnScrollListener = pOnScrollListener;
    }

    public interface OnScrollListener{
        public void onUp();
        public void onDown();
    }
}