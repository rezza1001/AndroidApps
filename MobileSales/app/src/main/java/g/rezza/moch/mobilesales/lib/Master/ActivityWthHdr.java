package g.rezza.moch.mobilesales.lib.Master;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import g.rezza.moch.mobilesales.Connection.firebase.FirebaseMessageService;
import g.rezza.moch.mobilesales.Connection.firebase.MyReceiver;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 27/12/17.
 */

public abstract class ActivityWthHdr extends AppCompatActivity {

    protected  static final  String TAG = "ActivityWthHdr";

    protected RelativeLayout    rvly_root_hdr_00;
    protected ScrollView        scvw_body_00;
    private   RelativeLayout    rvly_right_10;
    private ImageView           imvw_back_00;

    private   TextView txvw_hdr_00;
    protected Resources r;
    protected UserDB userDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        r = getResources();
        userDB = new UserDB();
        userDB.getMine(this);
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");


        rvly_root_hdr_00    = (RelativeLayout)  findViewById(R.id.rvly_root_hdr_00);
        rvly_right_10       = (RelativeLayout)  findViewById(R.id.rvly_right_10);
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
        registerListener();



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


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    protected void setTitleHeader(String pTitle){
        txvw_hdr_00.setText(pTitle);
    }

    protected void hideMenuRight(boolean hide){
        if (hide){
            rvly_right_10.setVisibility(View.GONE);
        }
        else {
            rvly_right_10.setVisibility(View.VISIBLE);
        }
    }

    protected abstract void onPostLayout();
    protected abstract void registerListener();
}
