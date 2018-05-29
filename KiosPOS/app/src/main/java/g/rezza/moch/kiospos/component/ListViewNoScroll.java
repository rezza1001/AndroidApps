package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 11/15/17.
 */

public class ListViewNoScroll extends MasterComponent {
    private static final String TAG = "ListViewNoScroll";

    private LinearLayout lnly_list_00;
    private LinearLayout lnly_parent_00;
    private int index = 0;

    public ListViewNoScroll(Context context) {
        super(context);
    }

    public ListViewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout.LayoutParams lpParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lnly_parent_00  = new LinearLayout(getContext(), null);
        lnly_list_00    = new LinearLayout(getContext(), null);

        addView(lnly_parent_00, lpParent);
        setLayoutParams(lpParent);

        LinearLayout.LayoutParams lpList = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lnly_list_00.setOrientation(LinearLayout.VERTICAL);
        lnly_parent_00.addView(lnly_list_00, lpList);
    }

    public void create(){
        lnly_list_00.removeAllViews();
         index = 0;
    }

    public void addData(View obj, final Object data){
        lnly_list_00.addView(obj);
        Log.d(TAG, "addData");
        obj.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectedItemListener != null){
                    onSelectedItemListener.OnItemSelected(index, data);
                }
            }
        });
        index ++;
    }


    private OnSelectedItemListener onSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        onSelectedItemListener = pOnSelectedItemListener;
    }
    public interface OnSelectedItemListener{
        public void OnItemSelected(int index, Object data);
    }
}
