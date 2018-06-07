package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;

/**
 * Created by rezza on 08/01/18.
 */

public class ItemDetailVIew extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout  rvly_root_00;
    private TextView        txvw_title_00;
    private ImageView       imvw_status_00;
    private boolean isOpen  = false;


    public ItemDetailVIew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_item_detail, this, true);
        rvly_root_00    = (RelativeLayout) findViewById(R.id.rvly_root_00);
        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        imvw_status_00  = (ImageView) findViewById(R.id.imvw_status_00);

        imvw_status_00.setBackgroundResource(R.drawable.ic_arrow_right);
        rvly_root_00.setOnClickListener(this);
    }

    public void setOpen(){
        onClick(rvly_root_00);
    }


    public void setTitle(String title){
        txvw_title_00.setText(title);
    }

    private OnClickPanelListener pListener;
    public void setOnClickPanelListener(OnClickPanelListener mListener){
        pListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if (isOpen){
            isOpen  = false;
            imvw_status_00.setBackgroundResource(R.drawable.ic_arrow_right);
        }
        else {
            isOpen  = true;
            imvw_status_00.setBackgroundResource(R.drawable.ic_arrow_bottom);
        }

        if (pListener != null){
            pListener.onClick(ItemDetailVIew.this, isOpen);
        }
    }

    public interface OnClickPanelListener{
        public void onClick(View view, boolean open);
    }
}
