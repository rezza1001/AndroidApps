package g.rezza.moch.mobilesales.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.PaymentAdapter;
import g.rezza.moch.mobilesales.lib.layout.ListViewNoScroll;

/**
 * Created by rezza on 03/01/18.
 */

public class PaymentMethod extends RelativeLayout {

    private ListViewNoScroll lsvw_payment_00;
    private TextView         txvw_title_00;

    private ArrayList<PaymentAdapter.PaymentMethodeHolder> mHolders;
    public PaymentMethod(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_payment_method, this, true);
        lsvw_payment_00 = (ListViewNoScroll) findViewById(R.id.lsvw_payment_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00) ;
        mHolders = new ArrayList<>();
    }

    public void init(){
        mHolders.clear();
        txvw_title_00.setVisibility(View.GONE);
    }
    public void init(String title){
        mHolders.clear();
        txvw_title_00.setText(title);
    }

    public void addData(String id, String name, Drawable icon){
        PaymentAdapter.PaymentMethodeHolder
                holder = new PaymentAdapter.PaymentMethodeHolder();

        holder.icon = icon;
        holder.id = id;
        holder.name = name;
        if (txvw_title_00.getVisibility() == View.VISIBLE){
            holder.category = txvw_title_00.getText().toString();
        }
        else {
            holder.category = "OTHER";
        }
        mHolders.add(holder);
    }

    public void create(){
        lsvw_payment_00.create();
        for (int i=0; i<mHolders.size(); i++){
            PaymentAdapter paymentAdapter = new PaymentAdapter(getContext(), null);
            paymentAdapter.create(mHolders.get(i));
            lsvw_payment_00.addData(paymentAdapter, mHolders.get(i));
        }
        lsvw_payment_00.setOnSelectedItemListener(new ListViewNoScroll.OnSelectedItemListener() {
            @Override
            public void OnItemSelected(int index, Object data) {
                PaymentAdapter.PaymentMethodeHolder pData = (PaymentAdapter.PaymentMethodeHolder) data;
//                notifyChange(pData);
                if (onSelectedItemListener != null){
                    onSelectedItemListener.OnItemSelected(index,pData);
                }
            }
        });
    }

    public void notifyChange(PaymentAdapter.PaymentMethodeHolder pholder){
        for (int i=0; i<mHolders.size(); i++){
            if (mHolders.get(i).id.equals(pholder.id)){
                mHolders.get(i).check = true;
            }
            else {
                mHolders.get(i).check = false;
            }
        }
        create();
    }

    private OnSelectedItemListener onSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        onSelectedItemListener = pOnSelectedItemListener;
    }
    public interface OnSelectedItemListener{
        public void OnItemSelected(int index, PaymentAdapter.PaymentMethodeHolder mHolder);
    }
}
