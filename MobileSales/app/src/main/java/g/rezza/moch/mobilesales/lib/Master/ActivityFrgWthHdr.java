package g.rezza.moch.mobilesales.lib.Master;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 27/12/17.
 */

public abstract class ActivityFrgWthHdr extends FragmentActivity {

    protected RelativeLayout    rvly_root_hdr_00;
    protected ScrollView        scvw_body_00;
    protected ImageView         imvw_back_00;
    protected Resources r;


    private TextView txvw_hdr_00;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = getResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        rvly_root_hdr_00    = (RelativeLayout)  findViewById(R.id.rvly_root_hdr_00);
        scvw_body_00        = (ScrollView)      findViewById(R.id.scvw_body_00);
        txvw_hdr_00         = (TextView)        findViewById(R.id.txvw_hdr_00);
        imvw_back_00        = (ImageView)       findViewById(R.id.imvw_back_00);
        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        onPostLayout();
        if (rvly_root_hdr_00 == null){
            return;
        }
        if (scvw_body_00 == null){
            return;
        }
        scvw_body_00.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scvw_body_00.getScrollY(); // For ScrollView
                if (scrollY >5 && scrollY <= 100){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark50));
                }
                else if (scrollY > 100 && scrollY <= 130){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark60));
                }
                else if (scrollY > 130 && scrollY <= 160){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark70));
                }
                else if (scrollY > 160 && scrollY <= 190){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark80));
                }
                else if (scrollY > 190 && scrollY <= 270){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark90));
                }
                else if (scrollY > 270 ){
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark100));
                }
                else {
                    rvly_root_hdr_00.setBackgroundColor(getResources().getColor(R.color.color_blue_dark00));
                }
            }
        });

    }




    protected void hideBody(boolean isHide){
        if (scvw_body_00 == null){
            return;
        }
        if (isHide){
            scvw_body_00.setVisibility(View.GONE);
        }
        else {
            scvw_body_00.setVisibility(View.VISIBLE);
        }
    }

    protected void setTitleHeader(String pTitle){
        Log.d("ActivityFrgWthHdr","setTitleHeader "+ pTitle);
        txvw_hdr_00.setText(pTitle);
    }

    protected abstract void onPostLayout();
}
