package g.rezza.moch.kiospos.lib;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.AllSteps;

/**
 * Created by Rezza on 11/14/17.
 */

public abstract class MasterTransActivity extends Activity {

    private static final String TAG = "MasterTransActivity";
    private HashMap<MasterTransActivity, Integer>
                                mMappingStap = new HashMap<>();

    protected AllSteps          allstp_01;
    protected JSONObject        mObjectData
                                = new JSONObject();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate "+ this.getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        try {
            mObjectData = new JSONObject(intent.getStringExtra("DATA"));
            Log.d(TAG, mObjectData.toString());
        } catch (Exception e) {
            Log.d(TAG, "NO OBJECT DATA");
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void showStep (int total_step, int current_step){
        allstp_01    = (AllSteps)       findViewById(R.id.allstp_hdr_01);
        allstp_01.create(total_step);
        allstp_01.activeStep(current_step);
    }

    protected void addObject(JSONObject pData){
        try {
            mObjectData.put(this.getClass().getSimpleName(),pData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void changeActivity(Class pNext){
        Log.d(TAG, "changeActivity "+pNext.getName() );
        Intent intent = new Intent(MasterTransActivity.this, pNext);
        intent.putExtra("DATA", mObjectData.toString());
        startActivity(intent);
        MasterTransActivity.this.finish();
    }

    protected abstract void registerListener();


    @Override
    public void onBackPressed() {
        AlertDialog.Builder notifDialog = new AlertDialog.Builder(this);
        notifDialog.setMessage("Are you sure to close this app ?");
        notifDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MasterTransActivity.this.finish();
            }
        });
        notifDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = notifDialog.create();
        alert.show();
    }

    protected void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
