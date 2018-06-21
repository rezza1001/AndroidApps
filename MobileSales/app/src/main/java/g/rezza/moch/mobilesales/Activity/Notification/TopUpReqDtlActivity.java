package g.rezza.moch.mobilesales.Activity.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import g.rezza.moch.mobilesales.Activity.TopUp.TopUpAllocationActivity;
import g.rezza.moch.mobilesales.Database.NotificationDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.holder.NotificationHolder;
import g.rezza.moch.mobilesales.lib.Master.ActivityDtl;
import g.rezza.moch.mobilesales.lib.Parse;

public class TopUpReqDtlActivity extends ActivityDtl {

    private FieldTransDtl ftdl_balance_00;
    private FieldTransDtl ftdl_account_00;
    private FieldTransDtl ftdl_accname_00;
    private FieldTransDtl ftdl_status_00;
    private FieldTransDtl ftdl_approved_date_00;
    private FieldTransDtl ftdl_note_00;
    private Button        bbtn_action_00;
    private TextView      header_subject_00;
    private TextView      txvw_name_00;
    private TextView      txvw_when_00;

    private NotificationDB notificationDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_req_dtl);

    }

    @Override
    protected void onPostLayout() {
        setTitleHeader(r.getString(R.string.notification));
        hideRightMenu(true);
        String id = getIntent().getStringExtra("ID");
        notificationDB = new NotificationDB();
        notificationDB.getData(this,NotificationDB.FIELD_ID +"="+ id );

        ftdl_balance_00 = (FieldTransDtl) findViewById(R.id.ftdl_balance_00);
        ftdl_account_00 = (FieldTransDtl) findViewById(R.id.ftdl_account_00);
        ftdl_accname_00 = (FieldTransDtl) findViewById(R.id.ftdl_accname_00);
        ftdl_status_00 = (FieldTransDtl) findViewById(R.id.ftdl_status_00);
        ftdl_note_00    = (FieldTransDtl) findViewById(R.id.ftdl_note_00);
        ftdl_approved_date_00 = (FieldTransDtl) findViewById(R.id.ftdl_approved_date_00);
        bbtn_action_00  = (Button)          findViewById(R.id.bbtn_action_00);
        header_subject_00 = (TextView)      findViewById(R.id.header_subject_00);
        txvw_name_00 = (TextView)      findViewById(R.id.txvw_name_00);
        txvw_when_00 = (TextView)      findViewById(R.id.txvw_when_00);

        header_subject_00.setText(notificationDB.what);
        txvw_name_00.setText(notificationDB.who.equalsIgnoreCase("NULL")?"Sales":notificationDB.who );
        txvw_when_00.setText(notificationDB.when);
        if (notificationDB.status == NotificationHolder.OUTBOX){
            bbtn_action_00.setVisibility(View.GONE);
        }


        ftdl_balance_00.setTitle(r.getString(R.string.nominal));
        ftdl_balance_00.setValue("Rp. " + Parse.toCurrnecy(notificationDB.nominal));

        ftdl_account_00.setTitle(r.getString(R.string.account_number));
        ftdl_account_00.setValue(notificationDB.account_no_req);

        ftdl_accname_00.setTitle(r.getString(R.string.account_name));
        ftdl_accname_00.setValue(notificationDB.account_name_req);

        ftdl_status_00.setTitle(r.getString(R.string.status));
        ftdl_status_00.setValue(notificationDB.status_desc);

        ftdl_approved_date_00.setTitle(r.getString(R.string.approved_date));
        ftdl_approved_date_00.setValue("-");
        ftdl_note_00.setTitle(r.getString(R.string.note));
        ftdl_note_00.setValue("");


//        bbtn_action_00.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent  intent = new Intent(TopUpReqDtlActivity.this, TopUpAllocationActivity.class);
//                intent.putExtra("ID", notificationDB.id + "");
//                startActivity(intent);
//                TopUpReqDtlActivity.this.finish();
//            }
//        });
    }
}
