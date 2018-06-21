package g.rezza.moch.mobilesales.component;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import faranjit.currency.edittext.CurrencyEditText;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.Database.BalanceDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;

/**
 * Created by rezza on 28/12/17.
 */

public class TopUpAllocationDialog extends Dialog {

    private EditextStandardC edtx_account_00;
    private CurrencyEditText edtx_nominal_00;
    private Button bbtn_action_00;
    Resources r;

    public TopUpAllocationDialog(Context context) {
        super(context);
        r = context.getResources();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_top_up_alloaction);

        edtx_account_00 = (EditextStandardC) findViewById(R.id.edtx_account_00);
        edtx_nominal_00 = (CurrencyEditText) findViewById(R.id.edtx_nominal_00);
        bbtn_action_00  = (Button)           findViewById(R.id.bbtn_action_00);

        edtx_account_00.setReadOnly(true);


        edtx_account_00.setTitle(r.getString(R.string.allocation_to_account));

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void create(String account_to){
        edtx_account_00.setValue(account_to);
    }

    public void send(){
        String nominal = "0";
        try {
            DecimalFormat format = new DecimalFormat("0.#");
            nominal = format.format(edtx_nominal_00.getCurrencyDouble()) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        PostManager pos = new PostManager(getContext());
        pos.setApiUrl("allocation-balance");
        ArrayList<KeyValueHolder> kvs = new ArrayList<>();
        kvs.add(new KeyValueHolder("account_to",edtx_account_00.getValue()));
        kvs.add(new KeyValueHolder("amount",nominal));
        pos.setData(kvs);
        pos.execute("POST");
        pos.setOnReceiveListener(new PostManager.onReceiveListener() {
            @Override
            public void onReceive(JSONObject obj, int code) {
                if (code == ErrorCode.OK){
                    BalanceDB balance = new BalanceDB();
                    balance.Syncronize(getContext());

                    Toast.makeText(getContext(), r.getString(R.string.allocation_success), Toast.LENGTH_SHORT ).show();
                    if (mListener != null){
                        mListener.onSubmit();
                    }
                    dismiss();
                }
                else {
                    Toast.makeText(getContext(), r.getString(R.string.failed_to_allocation), Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }


    private OnSubmitListener mListener;
    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        mListener = onSubmitListener;
    }
    public interface OnSubmitListener{
        public void onSubmit();
    }
}
