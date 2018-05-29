package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.R;


/**
 * Created by Rezza on 3/6/17.
 */

public class MyChooserQty extends MasterComponent implements View.OnClickListener{
    private ImageView   imvw_minus;
    private ImageView   imvw_plus;
    private TextView    txvw_value;
    private int mValue = 0;
    private int mMin = 0;
    private int mMax = 100;

    public MyChooserQty(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_chooserqty,this,true);
        imvw_minus  = (ImageView)   findViewById(R.id.imvw_minus);
        imvw_plus   = (ImageView)   findViewById(R.id.imvw_plus);
        txvw_value  = (TextView)    findViewById(R.id.txvw_value);

        imvw_minus.setOnClickListener(this);
        imvw_plus.setOnClickListener(this);
        txvw_value.setText(mValue + "");
    }

    public MyChooserQty(Context context) {
        super(context, null);
    }


    public void enable(){

    }

    public void disable(){

    }

    public int getValue(){
        mValue = Integer.parseInt(txvw_value.getText().toString());
        return mValue;
    }

    public void setValue(int value){
        mValue = value;
        txvw_value.setText(mValue + "");
    }

    @Override
    public void onClick(View v) {
        if (v == imvw_minus){
            if (mValue > mMin){
                mValue = mValue - 1;
            }
            if (mactionlistener != null){
                mactionlistener.onMinus(mValue);
            }
        }
        else  if (v == imvw_plus){
            if (mValue < mMax){
                mValue = mValue + 1;
            }
            if (mactionlistener != null){
                mactionlistener.onPlus(mValue);
            }
        }

        txvw_value.setText(mValue + "");
    }

    public void setMax(int max){
        mMax = max;
    }

    public void setMin(int min){
        mMin = min;
        txvw_value.setText(mMin + "");
    }


    private ActionListener mactionlistener;
    public void setonActionListener(ActionListener p_actionlistener){
        mactionlistener = p_actionlistener;
    }
    public interface ActionListener{
        public void onPlus(int qty);
        public void onMinus(int qty);
    }
}
