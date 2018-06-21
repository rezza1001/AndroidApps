package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.MenuAdapter;

/**
 * Created by rezza on 27/12/17.
 */

public class MenuGridView extends RelativeLayout {

    private LinearLayout lnly_left_00;
    private LinearLayout lnly_right_00;

    private int mPosition = 0;

    public MenuGridView(Context context) {
        super(context);
    }

    public MenuGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_menu_grid, this, true);
        lnly_left_00    = (LinearLayout) findViewById(R.id.lnly_left_00);
        lnly_right_00   = (LinearLayout) findViewById(R.id.lnly_right_00);
    }

    public void addMenu(int id, String title, Drawable image){

        MenuAdapter menu = new MenuAdapter(getContext(), null);
        menu.create(id, title, image);
        menu.setBackgroundR(getResources().getDrawable(R.drawable.button_menus));
        menu.setOnClickMenuListener(new MenuAdapter.OnClickMenuListener() {
            @Override
            public void onClick(int menuID) {
                if (mListener != null){
                    mListener.onSelected(menuID);
                }
            }
        });

        if (mPosition % 2 == 0){
            lnly_left_00.addView(menu);
        }
        else {
            lnly_right_00.addView(menu);
        }
        mPosition ++;


    }

    public void reloadMenu(){
        mPosition = 0;
        lnly_left_00.removeAllViews();
        lnly_right_00.removeAllViews();
    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void onSelected(int menuID);
    }
}
