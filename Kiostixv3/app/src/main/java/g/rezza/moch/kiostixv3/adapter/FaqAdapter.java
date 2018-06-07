package g.rezza.moch.kiostixv3.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.ItemDetailVIew;
import g.rezza.moch.kiostixv3.holder.FaqHolder;

/**
 * Created by rezza on 11/02/18.
 */

public class FaqAdapter extends ArrayAdapter<FaqHolder> {
    private static String TAG = "FaqAdapter";
    private LayoutInflater mInflater;

    public FaqAdapter(Context context, ArrayList<FaqHolder> values) {
        super(context, R.layout.adapter_see_all, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final FaqHolder Event = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_faq, parent, false);
            holder = new Holder();
            holder.itdt_faq_00    = (ItemDetailVIew)  convertView.findViewById(R.id.itdt_faq_00);
            holder.rvly_faq_00    = (RelativeLayout)  convertView.findViewById(R.id.rvly_faq_00);
            holder.txvw_faq_00    = (TextView)        convertView.findViewById(R.id.txvw_faq_00);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.itdt_faq_00.setTitle(Event.title);
        holder.txvw_faq_00.setText(Html.fromHtml(Event.description));

        holder.itdt_faq_00.setOnClickPanelListener(new ItemDetailVIew.OnClickPanelListener() {
            @Override
            public void onClick(View view, boolean open) {
                if (open){
                    holder.rvly_faq_00 .setVisibility(View.VISIBLE);
                }
                else {
                    holder.rvly_faq_00 .setVisibility(View.GONE);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.OnSelect(Event);
                }
            }
        });

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ItemDetailVIew itdt_faq_00;
        public RelativeLayout rvly_faq_00;
        public TextView txvw_faq_00;
    }

    private OnSelectedListener mListener;
    public void setOnSelectedListener(OnSelectedListener pListener){
        mListener = pListener;
    }
    public interface OnSelectedListener{
        public void OnSelect(FaqHolder event);
    }
}
