package g.rezza.moch.clientdashboard.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import g.rezza.moch.clientdashboard.R;
import g.rezza.moch.clientdashboard.adapter.TransDtlAdapter;
import g.rezza.moch.clientdashboard.holder.ExcelHolder;
import g.rezza.moch.clientdashboard.holder.KeyValueHolder;
import g.rezza.moch.clientdashboard.holder.ParameterHolder;
import g.rezza.moch.clientdashboard.holder.TransDtlHolder;
import g.rezza.moch.clientdashboard.libs.AlertMessage;
import g.rezza.moch.clientdashboard.libs.LongRunningGetIO;
import g.rezza.moch.clientdashboard.libs.Utils;

/**
 * Created by Rezza on 10/3/17.
 */

public class TransactionDetailView extends Fragment {

    public static final String TAG = "RZ MoreInfoActivity";

    private ListView                    lsvw_trans_dtl_10;
    private TransDtlAdapter             trans_dtl_adapter;
    private ArrayList<TransDtlHolder>   list_trans_dtl;
    private ImageView                   imvw_export_01;
    private ProgressDialog              mProgressDlg;
    private RelativeLayout              rvly_parent_0;

    public TransactionDetailView(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_transaction_detail, container, false);
        list_trans_dtl      = new ArrayList<>();
        lsvw_trans_dtl_10   = (ListView)    rootView.findViewById(R.id.lsvw_trans_dtl_10);
        trans_dtl_adapter   = new TransDtlAdapter(getContext(), list_trans_dtl);
        imvw_export_01      = (ImageView)   rootView.findViewById(R.id.imvw_export_01);
        rvly_parent_0       = (RelativeLayout) rootView.findViewById(R.id.rvly_parent_0);

        lsvw_trans_dtl_10.setAdapter(trans_dtl_adapter);
        trans_dtl_adapter.setOnSelectedItemListener(new TransDtlAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(TransDtlHolder holder, int position) {

            }
        });

        imvw_export_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDlg = new ProgressDialog(getContext());
                mProgressDlg.setTitle(AlertMessage.getMessage(AlertMessage.DOWNLOAD_REPORT, AlertMessage.ENGLISH));
                mProgressDlg.setMessage(AlertMessage.getMessage(AlertMessage.PLEASE_WAIT, AlertMessage.ENGLISH));
                mProgressDlg.show();
                mHandler.sendEmptyMessageDelayed(1,3000);
            }
        });
        return rootView;
    }

    public void create(){
        ParameterHolder param = new ParameterHolder();
        param.getData(getContext());
        loadToServer(param.event_id);
    }

    private void loadToServer(String event_id){
        try {
            JSONArray ja        = new JSONArray();
            {
                JSONObject jo = new JSONObject();
                jo.put("order_date","02/09/17 11:21");
                jo.put("payment_method","Bank Transfer");
                jo.put("order_qty","1");
                jo.put("sales_channel","Internet/Website");
                jo.put("order_value","Rp 500.000");
                for (int i=0 ;i<5; i++){
                    jo.put("order_no","30092017000001"+i);
                    ja.put(jo);
                }

            }
            {
                JSONObject jo = new JSONObject();
                jo.put("order_date","04/09/17 12:21");
                jo.put("payment_method","Credit Card");
                jo.put("order_qty","2");
                jo.put("sales_channel","Internet/Website");
                jo.put("order_value","Rp 4.000.000");
                for (int i=0 ;i<5; i++){
                    jo.put("order_no","30092017000002"+i);
                    ja.put(jo);
                }
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("order_date","2017-10-01 08:21");
                jo.put("payment_method","CIMB Clicks");
                jo.put("order_qty","5");
                jo.put("sales_channel","Internet/Website");
                jo.put("order_value","Rp 6.000.000");
                for (int i=0 ;i<2; i++){
                    jo.put("order_no","30092017000003"+i);
                    ja.put(jo);
                }
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("order_no","30092017000004");
                jo.put("order_date","06/09/17 08:21");
                jo.put("payment_method","Promotor");
                jo.put("order_qty","3");
                jo.put("sales_channel","Internet/Website");
                jo.put("order_value","Rp 2.500.000");
                ja.put(jo);
            }
            {
                JSONObject jo = new JSONObject();
                jo.put("order_no","30092017000005");
                jo.put("order_date","05/09/17 08:21");
                jo.put("payment_method","Promotor");
                jo.put("order_qty","8");
                jo.put("sales_channel","Internet/Website");
                jo.put("order_value","Rp 7.500.000");
                ja.put(jo);
            }
            buldData(ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void buldData(JSONArray ja){
        list_trans_dtl.clear();
        for (int i=0; i<ja.length(); i++){
            try {
                String order_no         = ja.getJSONObject(i).getString("order_no");
                String order_date       = ja.getJSONObject(i).getString("order_date");
                String order_qty        = ja.getJSONObject(i).getString("order_qty");
                String payment_method   = ja.getJSONObject(i).getString("payment_method");
                String sales_channel    = ja.getJSONObject(i).getString("sales_channel");
                String order_value      = ja.getJSONObject(i).getString("order_value");
//                 pOrder_no, pCreated_date,  porder_value,  pSales,  qty,  payment_method
                list_trans_dtl.add(new TransDtlHolder(order_no,order_date,order_value,sales_channel,order_qty,payment_method));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        trans_dtl_adapter.notifyDataSetChanged();
    }

    private void setEmpty(){
        list_trans_dtl.clear();
        trans_dtl_adapter.notifyDataSetChanged();
    }

    public  ArrayList<ExcelHolder> buildDataExcel(){
        ArrayList<ExcelHolder> excelHolders = new ArrayList<ExcelHolder>();
        for (int i=0; i<6; i++){
            ExcelHolder excel;
            switch (i){
                case 1:
                    excel= new ExcelHolder();
                    excel.parameter = "Order Number";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getOrderno());
                    }
                    break;
                case 2:
                    excel = new ExcelHolder();
                    excel.parameter = "Payment Method";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getPaymentMethod());
                    }
                    break;
                case 3:
                    excel = new ExcelHolder();
                    excel.parameter = "Sales/Channel";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getSalesChannel());
                    }
                    break;
                case 4:
                    excel = new ExcelHolder();
                    excel.parameter = "Quantity";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getQty());
                    }
                    break;
                case 5:
                    excel = new ExcelHolder();
                    excel.parameter = "Order Value";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getOrderValue());
                    }
                    break;
                case 6:
                    excel = new ExcelHolder();
                    excel.parameter = "Order Date";
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData(data.getCreatedDate());
                    }
                    break;
                default:
                    excel = new ExcelHolder();
                    excel.parameter = "No";
                    int number = 1;
                    for(TransDtlHolder data: list_trans_dtl){
                        excel.addData("" + number);
                        number++;
                    }
                    break;
            }
            excelHolders.add(excel);

        }
        return excelHolders;

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mProgressDlg.dismiss();
                    boolean status = Utils.exportToExcel("Transaction",buildDataExcel());
                    if (status){
                        Snackbar.make(rvly_parent_0 ,AlertMessage.getMessage(AlertMessage.DATA_SAVED_TO_FOLDER, AlertMessage.ENGLISH), Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                        Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.DATA_SAVED_TO_FOLDER, AlertMessage.ENGLISH),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Snackbar.make(rvly_parent_0 ,AlertMessage.getMessage(AlertMessage.DOWNLOAD_REPORT_FAILED, AlertMessage.ENGLISH), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
//                        Toast.makeText(getContext(), AlertMessage.getMessage(AlertMessage.DOWNLOAD_REPORT_FAILED, AlertMessage.ENGLISH),Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        }
    });
}
