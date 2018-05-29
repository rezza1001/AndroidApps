package g.rezza.moch.kiospos.Activitty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.component.AllSteps;
import g.rezza.moch.kiospos.db.Database;
import g.rezza.moch.kiospos.holder.BluethoothHolder;
import g.rezza.moch.kiospos.view.Category;
import g.rezza.moch.kiospos.view.ChooseQty;
import g.rezza.moch.kiospos.view.Complete;
import g.rezza.moch.kiospos.view.DataCheck;
import g.rezza.moch.kiospos.view.GuestInformation;
import g.rezza.moch.kiospos.view.Summary;

public class MainPosActivity extends AppCompatActivity implements MasterView.OnFinishPageListener{

    public static final String TAG = "MainPosActivity";

    public static final int REQUEST_REGISTER_PRINTER = 1;
    public static final int REQUEST_PRINT = 2;

    private RelativeLayout      rvly_main_00;
    private DataCheck           mViewDataChek;
    private GuestInformation    mViewGuestInf;
    private Category            mViewCategory;
    private ChooseQty           mViewChooserQty;
    private Summary             mViewSummary;
    private Complete            mViewComplete;
    private AllSteps            allstp_01;
    private ImageView           imvw_icon_10;

    private RelativeLayout.LayoutParams     layoutParams;
    private HashMap<String, Integer>        mMapSteps       = new HashMap<>();
    private ArrayList<MasterView>           mListView       = new ArrayList<>();
    private JSONObject                      mMasetrData;
    private Database                        mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pos);
        rvly_main_00 = (RelativeLayout) findViewById(R.id.rvly_main_00);
        allstp_01    = (AllSteps)       findViewById(R.id.allstp_01);
        imvw_icon_10 = (ImageView)      findViewById(R.id.imvw_icon_10);

        clearDB();
        create();
    }

    public void create(){
        mListView.clear();
        mMapSteps.clear();
        mMasetrData     = new JSONObject();
        layoutParams    = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewDataChek   = new DataCheck(MainPosActivity.this, null);
        mViewGuestInf   = new GuestInformation(MainPosActivity.this, null);
        mViewCategory   = new Category(MainPosActivity.this, null);
        mViewChooserQty = new ChooseQty(MainPosActivity.this, null);
        mViewSummary    = new Summary(MainPosActivity.this, null);
        mViewComplete   = new Complete(MainPosActivity.this, null);

        mListView.add(mViewDataChek);
        mListView.add(mViewGuestInf);
        mListView.add(mViewCategory);
        mListView.add(mViewChooserQty);
        mListView.add(mViewSummary);
        mListView.add(mViewComplete);

        registerPermision();
        createView(0, null);
        registerStep();
        registerListener();


    }

    private void registerListener(){
        for (MasterView mv: mListView){
            mv.setOnFinishPageListener(this);
        }
    }

    private void createView( int index, JSONObject jo){
        rvly_main_00.removeAllViews();
        MasterView mv = mListView.get(index);
        mv.create(jo);
        rvly_main_00.addView(mv, layoutParams);
    }

    private void registerStep(){

        mMapSteps.put(mViewDataChek.getClass().getName(),0);
        mMapSteps.put(mViewGuestInf.getClass().getName(),1);
        mMapSteps.put(mViewCategory.getClass().getName(),2);
        mMapSteps.put(mViewChooserQty.getClass().getName(),3);
        mMapSteps.put(mViewSummary.getClass().getName(),4);
        mMapSteps.put(mViewComplete.getClass().getName(),5);
        allstp_01.create(mMapSteps.size());
        allstp_01.activeStep(0);
        mViewDataChek.checkPrinter();
    }

    @Override
    public void onNext(MasterView view, JSONObject data) {
        if (mMasetrData == null){
            mMasetrData = new JSONObject();
        }
        mMasetrData     = data;
        int currentPage = mMapSteps.get(view.getClass().getName()) + 1;
        if (currentPage == mListView.size()){
            create();
            return;
        }
        else if (currentPage == 1 ){
            create();
        }
        createView(currentPage, mMasetrData);
        allstp_01.activeStep(currentPage);
    }

    @Override
    public void onBack(MasterView view, JSONObject data) {
        int currentPage = mMapSteps.get(view.getClass().getName()) - 1;
        createView(currentPage, data);
        allstp_01.activeStep(currentPage);
    }

    @Override
    public void onSave(MasterView view, JSONObject data) {
        if (view.equals(mViewDataChek)){
            try {
                if (!data.getString("GuestInformation").equals("null")){
                    if (mMasetrData == null){
                        mMasetrData = new JSONObject();
                    }
                    mMasetrData = data;
                    createView(1, mMasetrData);
                    allstp_01.activeStep(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PRINT){
            int currentPage = mMapSteps.get(mViewComplete.getClass().getName());
            createView(currentPage, mMasetrData);
            allstp_01.activeStep(currentPage);
        }
        else if (requestCode == REQUEST_REGISTER_PRINTER){
            mViewDataChek.checkPrinter();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder notifDialog = new AlertDialog.Builder(this);
        notifDialog.setMessage("Are you sure to close this app ?");
        notifDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainPosActivity.this.finish();
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

    private void clearDB(){
        mDB = new Database(MainPosActivity.this);
        mDB.delete(BluethoothHolder.TABLE_NAME, "");
    }

    private void registerPermision(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            imvw_icon_10.setVisibility(View.VISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            imvw_icon_10.setVisibility(View.GONE);
        }
    }
}
