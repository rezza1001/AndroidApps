package g.rezza.moch.chatid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import g.rezza.moch.chatid.R;
import g.rezza.moch.chatid.adapter.ChatAdapter;
import g.rezza.moch.chatid.adapter.ContactAdapter;
import g.rezza.moch.chatid.holder.ChatListHolder;
import g.rezza.moch.chatid.holder.ChatListHolder;

/**
 * Created by rezza on 09/02/18.
 */

public class ChatFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    private ArrayList<ChatListHolder> mList = new ArrayList<>();
    private ChatAdapter adapter;
    private RecyclerView rcvw_contact_00;

    public static Fragment newInstance() {
        Fragment frag   = new ChatFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_chat, container, false);
        rcvw_contact_00 = view.findViewById(R.id.rcvw_contact_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ChatAdapter(getActivity(), mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rcvw_contact_00.setLayoutManager(mLayoutManager);
        rcvw_contact_00.setItemAnimator(new DefaultItemAnimator());
        rcvw_contact_00.setAdapter(adapter);

        loadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loadData(){
        {
            ChatListHolder contact = new ChatListHolder();
            contact.sender = "Abdul Rojak";
            contact.message = "Ok siap jek.";
            contact.image_url = "https://pps.whatsapp.net/v/t61.11540-24/31425443_1663863123689502_5838587157886795776_n.jpg?oe=5B359A25&oh=9ecb39871418e81b7cefef45727cf898";
            contact.status = ChatListHolder.STATUS_ONLINE;
            mList.add(contact);
        }
        {
            ChatListHolder contact = new ChatListHolder();
            contact.sender = "Abdul Basit";
            contact.message = "Source nya udah di kirim ya tadi";
            contact.image_url = "https://pps.whatsapp.net/v/t61.11540-24/33894148_278384449597868_1498614218146971648_n.jpg?oe=5B35CEB8&oh=b6a0a5f5bb535885065cd6b021eb3253";
            contact.status = ChatListHolder.STATUS_ONLINE;
            mList.add(contact);
        }
        {
            ChatListHolder contact = new ChatListHolder();
            contact.sender = "Wiriadinata";
            contact.message = "Source nya udah di kirim ya tadi";
            contact.image_url = "https://pps.whatsapp.net/v/t61.11540-24/32799766_2076086279301544_3286803880238645248_n.jpg?oe=5B35CEB8&oh=58f027cd09551da32fcd10f01d9868a1";
            contact.status = ChatListHolder.STATUS_OFLINE;
            mList.add(contact);
        }
        {
            ChatListHolder contact = new ChatListHolder();
            contact.sender = "Jazynta L";
            contact.message = "Udah di test ya tadi.";
            contact.image_url = "https://pps.whatsapp.net/v/t61.11540-24/31571732_210274519752049_7429914335381553152_n.jpg?oe=5B35CEB8&oh=cffadc381a8dbbfe90066ea05556d61f";
            contact.status = ChatListHolder.STATUS_ONLINE;
            mList.add(contact);
        }
        {
            ChatListHolder contact = new ChatListHolder();
            contact.sender = "Salim";
            contact.message = "API udah gue kirim ya.";
            contact.image_url = "https://pps.whatsapp.net/v/t61.11540-24/33888391_206591766835391_1976432495217868800_n.jpg?oe=5B35CEB8&oh=c4f59cbc0228e87b7bded145e259c3a1";
            contact.status = ChatListHolder.STATUS_ONLINE;
            mList.add(contact);
        }

        adapter.notifyDataSetChanged();
    }

}
