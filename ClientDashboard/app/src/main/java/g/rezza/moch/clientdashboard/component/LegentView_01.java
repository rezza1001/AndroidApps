package g.rezza.moch.clientdashboard.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.clientdashboard.R;

/**
 * Created by Rezza on 9/20/17.
 */

public class LegentView_01 extends RelativeLayout {
    public static final String TAG = "RZ LegentView_01";

    private LinearLayout lnly_body_01;
    private LinearLayout lnly_underline_00;

    private TextView txvw_title_01;
    private TextView txvw_title_02;
    private ImageView imvw_icon_01;

    public LegentView_01(Context context) {
        super(context);
    }

    public LegentView_01(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_legent_01, this, true);

        lnly_body_01        = (LinearLayout) findViewById(R.id.lnly_body_01);
        lnly_underline_00   = (LinearLayout) findViewById(R.id.lnly_underline_00);
        txvw_title_01       = (TextView) findViewById(R.id.txvw_title_01);
        txvw_title_02       = (TextView) findViewById(R.id.txvw_title_02);
        imvw_icon_01        = (ImageView) findViewById(R.id.imvw_icon_01);
    }

    public void setBodyColor(int color){
        lnly_body_01.setBackgroundColor(color);
    }

    public void setBodyImage(int resource){
        lnly_body_01.setBackgroundResource(resource);
    }

    public void setTitle(int index, String value){
        switch (index){
            case 0 : txvw_title_01.setText(value); break;
            case 1 : txvw_title_02.setText(value); break;
        }
    }

    public void setIcon(int icon){
        imvw_icon_01.setImageResource(icon);
    }

    public void setUnderline(boolean show){
        if (!show){
            lnly_underline_00.setVisibility(View.GONE);
        }
        else {
            lnly_underline_00.setVisibility(View.VISIBLE);
        }
    }
}
