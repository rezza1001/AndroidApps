package g.rezza.moch.unileverapp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.holder.OrderPayment;
import g.rezza.moch.unileverapp.lib.Parse;

public class ListOrder extends RelativeLayout {

    private ArrayList<OrderPayment> mData = new ArrayList<>();

    public ListOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_listorder,this,true);
        initLayout();
    }

    private LinearLayout lnly_body_00;
    private TextView    txvw_total_00;
    private TextView    txvw_disc_00;
    private TextView    txvw_disc_ammount_00;
    private TextView    txvw_subtotal_00;
    private TextView    txvw_ppn_00;
    private TextView    txvw_totalbuy_00;
    private void initLayout(){
        lnly_body_00    = (LinearLayout)    findViewById(R.id.lnly_body_00);
        txvw_total_00   = (TextView)        findViewById(R.id.txvw_total_00);
        txvw_disc_00    = (TextView)        findViewById(R.id.txvw_disc_00);
        txvw_disc_ammount_00    = (TextView)        findViewById(R.id.txvw_disc_ammount_00);
        txvw_subtotal_00    = (TextView)        findViewById(R.id.txvw_subtotal_00);
        txvw_ppn_00    = (TextView)        findViewById(R.id.txvw_ppn_00);
        txvw_totalbuy_00    = (TextView)        findViewById(R.id.txvw_totalbuy_00);
    }

    public void create(ArrayList<OrderPayment> data){
        mData = data;
        lnly_body_00.removeAllViews();
        double total = 0;
        double subtotal = 0;
        double ppn = 0;
        for (OrderPayment order: mData){
            ListItemOrder list = new ListItemOrder(getContext(), null);
            list.create(order);
            lnly_body_00.addView(list);
            total = total+ (order.price * order.qty);
        }
        subtotal = total;

        txvw_total_00.setText("Rp. "+ Parse.toCurrnecy(total));
        txvw_disc_00.setText("Discount 0 %");
        txvw_disc_ammount_00.setText("Rp. 0");
        txvw_subtotal_00.setText("Rp. "+ Parse.toCurrnecy(subtotal) );
        ppn = 0.1 * subtotal;
        txvw_ppn_00.setText("Rp. "+Parse.toCurrnecy(ppn) );

        double final_amount = subtotal + ppn;
        txvw_totalbuy_00.setText("Rp. "+ Parse.toCurrnecy(final_amount));
    }
}
