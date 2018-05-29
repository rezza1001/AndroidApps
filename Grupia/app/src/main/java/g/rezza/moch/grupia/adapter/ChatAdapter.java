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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.listHolder.ChatLHolder;


/**
 * Created by Rezza on 9/22/17.
 */

public class ChatAdapter extends ArrayAdapter<ChatLHolder> {
    public static final String TAG = "ContactAdapter";

    private ArrayList<ChatLHolder> holders = new ArrayList<>();
    public static final int RESOURCE = R.layout.adapter_chat;
    public Resources r;

    public ChatAdapter(@NonNull Context context, ArrayList<ChatLHolder> pElement) {
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

            RelativeLayout rvly_left_00         = view.findViewById(R.id.rvly_left_00);
            TextView       txvw_message_l_00    = view.findViewById(R.id.txvw_message_l_00);

            RelativeLayout rvly_rigth_00        = view.findViewById(R.id.rvly_rigth_00);
            TextView       txvw_message_r_00    = view.findViewById(R.id.txvw_message_r_00);

            final ChatLHolder holder = holders.get(position);
            if (holder != null){
                if (holder.sender_id.equals("1")){
                    rvly_left_00.setVisibility(View.GONE);
                    rvly_rigth_00.setVisibility(View.VISIBLE);
                    txvw_message_r_00.setText(holder.message);

                }
                else {
                    rvly_left_00.setVisibility(View.VISIBLE);
                    rvly_rigth_00.setVisibility(View.GONE);
                    txvw_message_l_00.setText(holder.message);
                }
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
        public void selectedItem(ChatLHolder holder, int position);
    }
}
