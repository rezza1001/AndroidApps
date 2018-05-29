package g.rezza.moch.kiospos.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.AttendenzHolder;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 11/15/17.
 */

public class GuestAdapter extends MasterComponent {

    TextView txvw_option_20;
    ImageView imvw_option_21;
    AttendenzHolder mData;


    public GuestAdapter(Context context) {
        super(context);
    }

    public GuestAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_attendenz, this, true);

         txvw_option_20      = (TextView) 	    findViewById(R.id.txvw_option_20);
         imvw_option_21      = (ImageView)      findViewById(R.id.imvw_option_21) ;

        mData = new AttendenzHolder();
    }

    public void create(final AttendenzHolder data, final int position){
        mData = data;
        if (data.complete){
            txvw_option_20.setText(data.nama);
            imvw_option_21.setImageResource(R.drawable.ic_check);
        }
        else if (!data.complete){
            txvw_option_20.setText(data.nama +" " +  position + " : - ");
            imvw_option_21.setImageResource(R.drawable.ic_view);
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectedItemListener != null ){
                    onSelectedItemListener.OnItemSelected(position, data);
                }
            }
        });
    }

    public AttendenzHolder getData(){
        return mData;
    }

    private OnSelectedItemListener onSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        onSelectedItemListener = pOnSelectedItemListener;
    }
    public interface OnSelectedItemListener{
        public void OnItemSelected(int index, AttendenzHolder data);
    }
}
