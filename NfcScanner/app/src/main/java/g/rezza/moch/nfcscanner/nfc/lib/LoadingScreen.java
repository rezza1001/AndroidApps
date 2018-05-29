package g.rezza.moch.nfcscanner.nfc.lib;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Rezza on 8/22/17.
 */

public class LoadingScreen {
private Context mContext;
    ProgressDialog progressDialog = null;

    public LoadingScreen (Context pContext){
        mContext = pContext;
    }
    public void show() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("please wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dimiss(){
        progressDialog.dismiss();
    }


}
