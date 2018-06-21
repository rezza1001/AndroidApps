package g.rezza.moch.mobilesales.view.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by Rezza on 8/23/17.
 */

public class FcmNotif extends Dialog {

    private TextView txvw_title_00;
    private TextView txvw_message_00;
    private Button bbtn_action_00;

    public FcmNotif(Context context) {
        super(context);
    }

    public void create(String title, String message){
        show();
        txvw_title_00.setText(title);
        txvw_message_00.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_fcm);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        txvw_message_00 = (TextView) findViewById(R.id.txvw_message_00);
        bbtn_action_00  = (Button)   findViewById(R.id.bbtn_action_00);

        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FcmNotif.this.dismiss();
            }
        });
    }


}
