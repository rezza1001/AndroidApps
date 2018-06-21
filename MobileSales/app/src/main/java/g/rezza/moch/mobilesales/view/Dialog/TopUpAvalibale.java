package g.rezza.moch.mobilesales.view.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Activity.BrowseActivity;
import g.rezza.moch.mobilesales.Activity.TopUpActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransHdr;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.Parse;

/**
 * Created by Rezza on 8/23/17.
 */

public class TopUpAvalibale extends Dialog {

    private Button bbtn_cancel_00;
    private Button bbtn_action_00;
    private ImageView bbtn_close_00;
    private FieldTransHdr fthd_transcode_00;
    private FieldTransHdr fthd_transdate_00;
    private FieldTransHdr fthd_transtatus_00;
    private FieldTransHdr fthd_transamount_00;
    private FieldTransHdr fthd_paycode_00;
    private FieldTransHdr fthd_payname_00;
    private FieldTransHdr fthd_paytrx_00;

    private String redirect_url = "";
    private Activity activity;
    private String ref_trans_code = "";

    public void setActivity(Activity pActivity){
        activity = pActivity;
    }

    public TopUpAvalibale(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_topupavaliable);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bbtn_action_00      = (Button)          findViewById(R.id.bbtn_action_00);
        bbtn_cancel_00      = (Button)          findViewById(R.id.bbtn_cancel_00);
        bbtn_close_00       = (ImageView)       findViewById(R.id.bbtn_close_00);
        fthd_transcode_00   = (FieldTransHdr) findViewById(R.id.fthd_transcode_00);
        fthd_transdate_00   = (FieldTransHdr) findViewById(R.id.fthd_transdate_00);
        fthd_transtatus_00  = (FieldTransHdr) findViewById(R.id.fthd_transtatus_00);
        fthd_transamount_00 = (FieldTransHdr) findViewById(R.id.fthd_transamount_00);
        fthd_paycode_00     = (FieldTransHdr) findViewById(R.id.fthd_paycode_00);
        fthd_payname_00     = (FieldTransHdr) findViewById(R.id.fthd_payname_00);
        fthd_paytrx_00      = (FieldTransHdr) findViewById(R.id.fthd_paytrx_00);

        fthd_transcode_00.setTitle("Transaction Code");
        fthd_transdate_00.setTitle("Transaction Date");
        fthd_transtatus_00.setTitle("Transaction Status");
        fthd_transamount_00.setTitle("Amount");
        fthd_paycode_00.setTitle("Payment Code");
        fthd_payname_00.setTitle("Amount");
        fthd_paytrx_00.setTitle("TRX ID");

        bbtn_close_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            close();
            }
        });

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BrowseActivity.class);
                intent.putExtra("URL",redirect_url);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        bbtn_cancel_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cancelPayment();
            }
        });
    }

    public void close(){
        TopUpAvalibale.this.dismiss();
        if (mListener != null){
            mListener.onClose();
        }
    }

    public void showData(JSONObject trans, JSONObject resps){
        show();
        try {
            fthd_transcode_00.setValue(trans.getString("transaction_code"));
            fthd_transdate_00.setValue(trans.getString("created_at"));
            fthd_transtatus_00.setValue(trans.getString("status_description"));
            fthd_transamount_00.setValue("IDR " + Parse.toCurrnecy(trans.getString("amount_payable")));
            fthd_paycode_00.setValue(trans.getString("payment_method"));
            fthd_payname_00.setValue(trans.getString("bank"));
            fthd_paytrx_00.setValue(resps.getString("trx_id"));
            redirect_url    = resps.getString("redirect_url");
            ref_trans_code  = trans.getString("reference_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cancelPayment(){
        PostManager post = new PostManager(getContext());
        post.setApiUrl("cancel-transaction");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("bill_no",ref_trans_code));
        post.setData(kvs);
        post.execute("POST");
        post.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    Toast.makeText(activity,"Your transaction has been canceled ", Toast.LENGTH_SHORT).show();
                    close();
                }
            }
        });
    }


    private OnCloseListener mListener;
    public void setOnCloseListener(OnCloseListener pListener){
        mListener = pListener;
    }
    public interface OnCloseListener{
        public void onClose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
}

