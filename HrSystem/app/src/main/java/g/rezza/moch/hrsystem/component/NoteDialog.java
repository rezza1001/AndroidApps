package g.rezza.moch.hrsystem.component;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import g.rezza.moch.hrsystem.R;

/**
 * Created by rezza on 12/02/18.
 */

public class NoteDialog extends Dialog {

    private TextView txvw_title_00;
    private TextView txvw_message_00;
    private EditText edtx_message_00;
    private Button   bbtn_cancel_00;
    private Button   bbtn_ok_00;
    private LinearLayout
                     lnly_notif_00;
    private TextView txvw_notif_00;

    public NoteDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.component_note_dialog);

        txvw_title_00       = (TextView) findViewById(R.id.txvw_title_00);
        txvw_message_00     = (TextView) findViewById(R.id.txvw_message_00);
        txvw_notif_00       = (TextView) findViewById(R.id.txvw_notif_00);
        edtx_message_00     = (EditText) findViewById(R.id.edtx_message_00);
        bbtn_cancel_00      = (Button)   findViewById(R.id.bbtn_cancel_00);
        bbtn_ok_00          = (Button)   findViewById(R.id.bbtn_ok_00);
        lnly_notif_00       = (LinearLayout) findViewById(R.id.lnly_notif_00);

        lnly_notif_00.setVisibility(View.GONE);
        bbtn_ok_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtx_message_00.getText().toString().isEmpty()){
                    lnly_notif_00.setVisibility(View.VISIBLE);
                    return;
                }
                if (mListener != null){
                    mListener.onPositive(edtx_message_00.getText().toString());
                }
                dismiss();

            }
        });

        bbtn_cancel_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null){
                    mListener.onNegative();
                }
            }
        });
    }

    public void create(String title, String message){
        show();
        txvw_title_00.setText(title);
        txvw_message_00.setText(message);
    }
    public void setHint(String hint){
        edtx_message_00.setHint(hint);
    }

    public void setNotifError(String notif){
        txvw_notif_00.setText(notif);
    }

    private OnActionListener mListener;
    public void setOnActionListener(OnActionListener pOnActionListener){
        mListener = pOnActionListener;
    }
    public interface OnActionListener{
        public void onPositive(String message);
        public void onNegative();
    }
}
