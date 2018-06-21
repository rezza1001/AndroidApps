package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 27/12/17.
 */

public class MenuAdapter extends RelativeLayout {

    private LinearLayout lnly_root_menu_00;
    private ImageView imvw_menu;
    private TextView  txvw_menu;
    private int mID = 0;

    public MenuAdapter(Context context) {
        super(context);
    }

    public MenuAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_menu, this, true);

        lnly_root_menu_00 = (LinearLayout) findViewById(R.id.lnly_root_menu_00);
        imvw_menu = (ImageView) findViewById(R.id.imvw_menu);
        txvw_menu = (TextView)  findViewById(R.id.txvw_menu);

        lnly_root_menu_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuListener != null){
                    menuListener.onClick(mID);
                }
            }
        });
    }

    public void create(int id, String title, Drawable image){
        imvw_menu.setImageDrawable(image);
        txvw_menu.setText(title);
        mID = id;
    }

    public void setBackgroundR(Drawable rColor){
        lnly_root_menu_00.setBackground(rColor);
    }

    public int getID(){
        return mID;
    }

    private OnClickMenuListener menuListener;
    public void setOnClickMenuListener(OnClickMenuListener pListener){
        menuListener = pListener;
    }
    public interface OnClickMenuListener{
        public void onClick(int menuID);
    }
}
