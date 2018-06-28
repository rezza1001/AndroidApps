package g.rezza.moch.chatid.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.chatid.R;
import g.rezza.moch.chatid.holder.ChatRandomHolder;

public class ChatRandomAdapter extends RecyclerView.Adapter<ChatRandomAdapter.ChatRandomViewAdapter> {

    private ArrayList<ChatRandomHolder> mList = new ArrayList<>();

    public class ChatRandomViewAdapter extends RecyclerView.ViewHolder{

        RelativeLayout  rvly_left_00, rvly_right_00;
        TextView        txvw_l_time_00, txvw_r_time_00;
        TextView        txvw_l_message_00, txvw_r_message_00;

        public ChatRandomViewAdapter(View view) {
            super(view);
            rvly_left_00    = (RelativeLayout)  view.findViewById(R.id.rvly_left_00);
            rvly_right_00   = (RelativeLayout)  view.findViewById(R.id.rvly_right_00);
            txvw_l_time_00  = (TextView)        view.findViewById(R.id.txvw_l_time_00);
            txvw_l_message_00   = (TextView)     view.findViewById(R.id.txvw_l_message_00);
            txvw_r_time_00      = (TextView)     view.findViewById(R.id.txvw_r_time_00);
            txvw_r_message_00   = (TextView)     view.findViewById(R.id.txvw_r_message_00);
        }
    }

    public ChatRandomAdapter (ArrayList<ChatRandomHolder> pList){
        mList = pList;
    }

    @NonNull
    @Override
    public ChatRandomViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_random, parent, false);
        return new ChatRandomViewAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRandomViewAdapter holder, int position) {
        holder.rvly_left_00.setVisibility(View.GONE);
        holder.rvly_right_00.setVisibility(View.GONE);

        ChatRandomHolder chat = mList.get(position);
        if (!chat.receiver.equals("Rezza")){
            holder.rvly_left_00.setVisibility(View.VISIBLE);
            holder.txvw_l_message_00.setText(chat.message);
            holder.txvw_l_time_00.setText("30 Juni 2018 16:00:01");
        }
        else {
            holder.rvly_right_00.setVisibility(View.VISIBLE);
            holder.txvw_r_message_00.setText(chat.message);
            holder.txvw_r_time_00.setText("30 Juni 2018 16:00:02");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
