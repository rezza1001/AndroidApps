package g.rezza.moch.clientdashboard.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.component.ChannelView;
import g.rezza.moch.clientdashboard.component.OrderReportView;
import g.rezza.moch.clientdashboard.component.PaymentMethodView;
import g.rezza.moch.clientdashboard.component.TicketSectionView;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.MasterView;

/**
 * Created by Rezza on 10/3/17.
 */

public class TransactionGrafikView extends Fragment implements MasterView.OnReceiveListener {
    public static String TAG = "TransactionGrafikView";

//    private OrderReportView ordvw_report_00;
    private TicketSectionView tsvw_report_00;
    private ChannelView       chnvw_report_00;
    private PaymentMethodView pmtd_report_00;
    private RelativeLayout    rvly_load_more_01;

    public TransactionGrafikView(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_transaction_grafik, container, false);

//        ordvw_report_00   = (OrderReportView) rootView.findViewById(R.id.ordvw_report_00);
        tsvw_report_00    = (TicketSectionView) rootView.findViewById(R.id.tsvw_report_00);
        chnvw_report_00   = (ChannelView) rootView.findViewById(R.id.chnvw_report_00);
        pmtd_report_00    = (PaymentMethodView) rootView.findViewById(R.id.pmtd_report_00);
        rvly_load_more_01 = (RelativeLayout) rootView.findViewById(R.id.rvly_load_more_01);

        rvly_load_more_01.setVisibility(View.GONE);
        chnvw_report_00.setOnReceiveListener(this);
        pmtd_report_00.setOnReceiveListener(this);
        tsvw_report_00.setOnReceiveListener(this);
        create();
        return rootView;

    }

    public void create(){
        tsvw_report_00.create();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    chnvw_report_00.create();
                case 2:
                    pmtd_report_00.create();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onReceive(MasterView v, String response) {
        Log.d(TAG,"onReceive");
        if (v.equals(tsvw_report_00)){
            mHandler.sendEmptyMessageDelayed(1,100);
        }
        else {
            mHandler.sendEmptyMessageDelayed(2,100);
        }



    }
}
