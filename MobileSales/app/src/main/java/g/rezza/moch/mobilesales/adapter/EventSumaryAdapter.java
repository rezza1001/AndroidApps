package g.rezza.moch.mobilesales.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.FieldTransDtl;
import g.rezza.moch.mobilesales.holder.EventCategoryHolder;
import g.rezza.moch.mobilesales.lib.Parse;

/**
 * Created by rezza on 10/01/18.
 */

public class EventSumaryAdapter extends RelativeLayout {

    private TextView txvw_category_00;
    private FieldTransDtl ftdl_price_00;
    private FieldTransDtl ftdl_qty_00;
    private FieldTransDtl ftdl_total_00;

    private Resources r;

    public EventSumaryAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.adapter_event_summary, this, true);
        r = getResources();
        createLayout();
    }

    private void createLayout(){
        txvw_category_00 = (TextView)   findViewById(R.id.txvw_category_00);
        ftdl_price_00    = (FieldTransDtl)   findViewById(R.id.ftdl_price_00);
        ftdl_qty_00    = (FieldTransDtl)   findViewById(R.id.ftdl_qty_00);
        ftdl_total_00    = (FieldTransDtl)   findViewById(R.id.ftdl_total_00);

        ftdl_price_00.setTitle(r.getString(R.string.price));
        ftdl_qty_00.setTitle(r.getString(R.string.quantity));
        ftdl_total_00.setTitle(r.getString(R.string.total));
    }

    public void create(EventCategoryHolder category){
        txvw_category_00.setText(category.name);
        ftdl_price_00.setValue("IDR "+ Parse.toCurrnecy(category.price));
        ftdl_qty_00.setValue(category.quantity +"");

        float total_price = category.price * category.quantity;
        ftdl_total_00.setValue("IDR "+ Parse.toCurrnecy(total_price));
    }
}
