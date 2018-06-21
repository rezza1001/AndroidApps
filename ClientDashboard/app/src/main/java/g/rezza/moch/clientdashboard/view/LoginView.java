package g.rezza.moch.clientdashboard.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.component.EditextWithTitle;
import g.rezza.moch.clientdashboard.component.PopupConfig;
import g.rezza.moch.clientdashboard.component.PopupParameter;
import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.UserSHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;
import g.rezza.moch.clientdashboard.libs.MyApplication;

/**
 * Created by Rezza on 9/20/17.
 */

public class LoginView extends RelativeLayout {
    public static final String TAG = "RZ LoginView";

    private EditextWithTitle edwt_username_01;
    private EditextWithTitle edwt_password_02;
    private Button           bbtn_login_01;
    private TextView         txvw_create_01;
    private TextView         txvw_notif_00;
    private ProgressDialog   mLoading;
    private RelativeLayout   rvly_body_01;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_login, this, true);

        edwt_username_01 = (EditextWithTitle) findViewById(R.id.edwt_username_01);
        edwt_password_02 = (EditextWithTitle) findViewById(R.id.edwt_password_02);
        bbtn_login_01    = (Button)           findViewById(R.id.bbtn_login_01);
        txvw_create_01   = (TextView)         findViewById(R.id.txvw_create_01);
        txvw_notif_00    = (TextView)         findViewById(R.id.txvw_notif_00);
        rvly_body_01     = (RelativeLayout)   findViewById(R.id.rvly_body_01);

        createLayout();

        rvly_body_01.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupConfig poup = new PopupConfig(getContext());
                poup.show();
                return false;
            }
        });

        edwt_username_01.setValue("aldifebrian@gmail.com");
        edwt_password_02.setValue("abcd1234");

    }

    private void createLayout(){
        txvw_notif_00.setVisibility(View.GONE);

        mLoading = new ProgressDialog(getContext());
        mLoading.setMessage(AlertMessage.getMessage(AlertMessage.PLEASE_WAIT, AlertMessage.ENGLISH));

        edwt_username_01.setTitle("Email Address");
        edwt_username_01.setTextColor(getResources().getColor(R.color.colorBlue_06));
        edwt_username_01.setTitleColor(getResources().getColor(R.color.colorBlue_06_t50));
        edwt_username_01.setBackground(R.drawable.editext_yellow_01);
        edwt_username_01.create(EditextWithTitle.TYPE_EMAIL);

        edwt_password_02.setTextColor(getResources().getColor(R.color.colorBlue_06));
        edwt_password_02.setTitleColor(getResources().getColor(R.color.colorBlue_06_t50));
        edwt_password_02.setTitle("Password");
        edwt_password_02.setBackground(R.drawable.editext_yellow_01);
        edwt_password_02.create(EditextWithTitle.TYPE_PASSWORD);

        UserSHolder userdef = new UserSHolder();
        userdef.getData(getContext());
        if (!userdef.email.isEmpty()){
            edwt_username_01.setValue(userdef.email);
        }

        bbtn_login_01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    mLoading.show();
                    String username = edwt_username_01.getValue().trim();
                    final String password = edwt_password_02.getValue().trim();
                    mHandler.sendEmptyMessageDelayed(2,2000);

                }

                // DO SOMETHING
            }
        });

        txvw_create_01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRegisterClickListener != null){
                    mOnRegisterClickListener.onclik();
                }
            }
        });

    }


    private boolean validation(){
        if (edwt_username_01.getValue().isEmpty()){
            Toast.makeText(getContext(), "Username Required !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (edwt_password_02.getValue().isEmpty()){
            Toast.makeText(getContext(), "Password Required !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    txvw_notif_00.setText(msg.getData().getString("DESC"));
                    txvw_notif_00.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    mLoading.dismiss();
                    if (mOnLoginListener != null){
                        mOnLoginListener.onLogin("00");
                    }
                    break;
            }
        }
    };
    /*
     * Register Listener
     */

    private OnRegisterClickListener mOnRegisterClickListener;
    public void setOnRegisterClickListener(OnRegisterClickListener pOnRegisterClickListener){
        mOnRegisterClickListener = pOnRegisterClickListener;
    }

    private OnLoginListener mOnLoginListener;
    public void setOnLoginListener(OnLoginListener pLoginListener){
        mOnLoginListener = pLoginListener;
    }


    /*
     * Interface Listener
     */

    public interface OnRegisterClickListener{
        public void onclik();
    }

    public interface OnLoginListener{
        public void onLogin(String status);
    }

}
