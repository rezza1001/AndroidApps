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
import g.rezza.moch.grupia.listHolder.NotifLHolder;
import g.rezza.moch.grupia.listHolder.NotifLHolder;


/**
 * Created by Rezza on 9/22/17.
 */

public class NotificationAdapter extends ArrayAdapter<NotifLHolder> {
    public static final String TAG = "NotificationAdapter";

    private ArrayList<NotifLHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_notification;
    public Resources r;

    public NotificationAdapter(@NonNull Context context, ArrayList<NotifLHolder> pElement) {
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

            TextView txvw_create_by_00      = view.findViewById(R.id.txvw_create_by_00);
            TextView txvw_create_date_00    = view.findViewById(R.id.txvw_create_date_00);
            TextView txvw_subject_00        = view.findViewById(R.id.txvw_subject_00);
            TextView txvw_body_00           = view.findViewById(R.id.txvw_body_00);

            final NotifLHolder holder = holders.get(position);
            if (holder != null){
                txvw_create_by_00.setText(holder.created_by);
                txvw_create_date_00.setText(holder.create_date);
                txvw_subject_00.setText(holder.subject);
                txvw_body_00.setText(holder.body);
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
        public void selectedItem(NotifLHolder holder, int position);
    }
}
