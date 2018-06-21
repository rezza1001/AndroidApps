package g.rezza.moch.mobilesales.lib.Master;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import g.rezza.moch.mobilesales.DataStatic.App;
import g.rezza.moch.mobilesales.Database.UserDB;
import g.rezza.moch.mobilesales.Database.UserInfoDB;
import g.rezza.moch.mobilesales.R;

public abstract class ActivityDtl extends AppCompatActivity {

    protected String TAG = "ActivityDtl";

    private TextView            txvw_hdr_00;
    private RelativeLayout      rvly_right_10;
    private RelativeLayout      rvly_left_11;
    protected RelativeLayout    rvly_root_hdr_00;
    protected ScrollView        scvw_body_00;
    private ImageView           imvw_right_10;
    private ImageView           imvw_back_00;

    protected Resources r;
    protected UserDB userDB;
    protected UserInfoDB userInfoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = ActivityDtl.this.getClass().getSimpleName();
        userDB = new UserDB();
        userDB.getMine(this);

        userInfoDB = new UserInfoDB();
        userInfoDB.getData(this, userDB.id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        r = getResources();
        rvly_root_hdr_00    = (RelativeLayout)  findViewById(R.id.rvly_root_hdr_00);
        txvw_hdr_00         = (TextView)        findViewById(R.id.txvw_hdr_00);
        rvly_right_10       = (RelativeLayout)  findViewById(R.id.rvly_right_10);
        rvly_left_11        = (RelativeLayout)  findViewById(R.id.rvly_left_11);
        scvw_body_00        = (ScrollView)      findViewById(R.id.scvw_body_00);
        imvw_right_10       = (ImageView)       findViewById(R.id.imvw_right_10);
        imvw_back_00        = (ImageView)       findViewById(R.id.imvw_back_00);

        imvw_back_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        onPostLayout();
        rvly_left_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvly_right_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMenuRight();
            }
        });

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


    protected void hideRightMenu(boolean hide){
        if (hide){
            rvly_right_10.setVisibility(View.GONE);
        }
        else {
            rvly_right_10.setVisibility(View.VISIBLE);
        }
    }

    public void setRightImage(int res){
        imvw_right_10.setImageResource(res);
    }

    protected void onClickMenuRight(){

    }
    protected void setTitleHeader(String pTitle){
        txvw_hdr_00.setText(pTitle);
    }

    protected abstract void onPostLayout();

    protected  void onReceive(String body, JSONObject data, String title){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
