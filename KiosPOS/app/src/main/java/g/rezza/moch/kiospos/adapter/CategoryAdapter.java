package g.rezza.moch.kiospos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.holder.CategoryHolder;

/**
 * Created by Rezza on 9/22/17.
 */

public class CategoryAdapter extends ArrayAdapter<CategoryHolder> {
    public static final String TAG = "RZ Category";

    private ArrayList<CategoryHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_category;

    public CategoryAdapter(@NonNull Context context, ArrayList<CategoryHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null){
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }

            TextView    txvw_option_20             = (TextView) 	view.findViewById(R.id.txvw_option_20);
            ImageView   imvw_option_21             = (ImageView)    view.findViewById(R.id.imvw_option_21) ;

            final CategoryHolder holder = holders.get(position);
            if (holder != null){
                txvw_option_20.setText(holder.value);
                if (holder.checked){
                    imvw_option_21.setVisibility(View.VISIBLE);
                }
                else {
                    imvw_option_21.setVisibility(View.GONE);
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.checked){
                        holder.checked = true;
                    }
                    else {
                        holder.checked = false;
                    }
                    holder.checked = true;
                    notifyDataSetChanged();
                    if (mOnSelectedItemListener != null){
                        mOnSelectedItemListener.selectedItem(holder, position);
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

    /*
     * Register Listener
     */

    private OnSelectedItemListener mOnSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener pOnSelectedItemListener){
        mOnSelectedItemListener = pOnSelectedItemListener;
    }

    /*
     * Interface Listener
     */

    public interface OnSelectedItemListener{
        public void selectedItem(CategoryHolder holder, int position);
    }
}
