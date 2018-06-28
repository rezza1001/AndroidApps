package g.rezza.moch.chatid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import g.rezza.moch.chatid.R;
import g.rezza.moch.chatid.holder.ContactHolder;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewAdapter> {

    private ArrayList<ContactHolder> mlist = new ArrayList<>();
    private Context mContext;

    public ContactAdapter(Context pContext, ArrayList<ContactHolder> pList){
        mlist       = pList;
        mContext    = pContext;
    }

    @NonNull
    @Override
    public ContactViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contact, parent, false);

        return new ContactViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewAdapter holder, int position) {
        ContactHolder contact = mlist.get(position);
        Glide.with(mContext).load(contact.image_url).into(holder.imvw_profile_00);
        holder.txvw_name_00.setText(contact.name);
        if (contact.status == ContactHolder.STATUS_ONLINE){
            holder.imvw_status_00.setImageResource(android.R.drawable.presence_online);
        }
        else {
            holder.imvw_status_00.setImageResource(android.R.drawable.presence_offline);
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ContactViewAdapter extends RecyclerView.ViewHolder{

        private CircleImageView imvw_profile_00;
        private ImageView       imvw_status_00;
        private TextView        txvw_name_00;
        public ContactViewAdapter(View itemView) {
            super(itemView);
            imvw_profile_00 = (CircleImageView) itemView.findViewById(R.id.imvw_profile_00);
            imvw_status_00  = (ImageView) itemView.findViewById(R.id.imvw_status_00);
            txvw_name_00    = (TextView) itemView.findViewById(R.id.txvw_name_00);

        }
    }
}
