package g.rezza.moch.grupia.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.listHolder.ContactLHolder;


/**
 * Created by Rezza on 9/22/17.
 */

public class ContactAdapter extends ArrayAdapter<ContactLHolder> {
    public static final String TAG = "ContactAdapter";

    private ArrayList<ContactLHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_contact;
    public Resources r;

    public ContactAdapter(@NonNull Context context, ArrayList<ContactLHolder> pElement) {
        super(context, RESOURCE, pElement);
        holders = pElement;
        r = context.getResources();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null){
                view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(RESOURCE, null);
            }

            CircleImageView imvw_profile_00 = (CircleImageView) view.findViewById(R.id.imvw_profile_00);
            TextView        txvw_name_00    = view.findViewById(R.id.txvw_name_00);
            TextView        txvw_no_00      = view.findViewById(R.id.txvw_no_00);

            Log.d(TAG,"getView "+ holders.size());
            final ContactLHolder holder = holders.get(position);
            if (holder != null){
                txvw_name_00.setText(holder.name);
                txvw_no_00.setText(holder.no);
                Glide.with(getContext()).load(holder.imageUrl).into(imvw_profile_00);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        public void selectedItem(ContactLHolder holder, int position);
    }
}
