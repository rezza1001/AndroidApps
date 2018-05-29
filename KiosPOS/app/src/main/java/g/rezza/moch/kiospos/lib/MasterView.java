package g.rezza.moch.kiospos.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import g.rezza.moch.kiospos.R;


/**
 * Created by Rezza on 10/12/17.
 */

public class MasterView extends RelativeLayout {

    protected  JSONObject      mData;
    public MasterView(Context context) {
        super(context);
    }

    public MasterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mData = new JSONObject();
    }

    protected void disableLogo(){
        ImageView imvw_icon_10 = (ImageView) findViewById(R.id.imvw_icon_10);
        imvw_icon_10.setVisibility(View.GONE);
    }

    public void create(JSONObject jo){

    }

    protected OnFinishPageListener mFinishListener;
    public void setOnFinishPageListener(OnFinishPageListener pFinishListener){
        mFinishListener = pFinishListener;
    }
    public interface OnFinishPageListener{
        public void onNext(MasterView view, JSONObject data);
        public void onBack(MasterView view, JSONObject data);
        public void onSave(MasterView view, JSONObject data);
    }
}
