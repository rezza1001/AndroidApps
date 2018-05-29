package g.rezza.moch.grupia.view;

import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.SignInActivity;
import g.rezza.moch.grupia.adapter.ChatAdapter;
import g.rezza.moch.grupia.connection.firebase.FirebaseMessageService;
import g.rezza.moch.grupia.connection.firebase.MyReceiver;
import g.rezza.moch.grupia.lib.AudioPlayer;
import g.rezza.moch.grupia.listHolder.ChatLHolder;

/**
 * Created by rezza on 20/02/18.
 */

public class ChatFragment extends Fragment implements View.OnClickListener{

    private ListView        lsvw_chat_00;
    private ChatAdapter     mAdaper;
    private ArrayList<ChatLHolder>mHolder = new ArrayList<>();

    private RelativeLayout  rvly_send_00;
    private EditText        edtx_message_00;
    private MyReceiver      myReceiver;

    public static Fragment newInstance(int color) {
        Fragment frag   = new ChatFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_chat_fragment, container, false);
        lsvw_chat_00    = view.findViewById(R.id.lsvw_chat_00);
        mAdaper         = new ChatAdapter(getActivity(), mHolder);

        rvly_send_00    = view.findViewById(R.id.rvly_send_00);
        edtx_message_00 = view.findViewById(R.id.edtx_message_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lsvw_chat_00.setAdapter(mAdaper);

        rvly_send_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLHolder chat = new ChatLHolder();
                chat.sender_id = "1";
                chat.message   = edtx_message_00.getText().toString();
                mHolder.add(chat);
                mAdaper.notifyDataSetChanged();
                edtx_message_00.setText("");
                scrollMyListViewToBottom();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createDummy();
    }

    @Override
    public void onClick(View view) {

    }

    private void createDummy(){
        mHolder.clear();
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "1";
            chat.message   = "I can't. I already have plans";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "2";
            chat.message   = "She is going to be displaying her work all week. Would you like to go tomorrow night?";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "1";
            chat.message   = "Sure. I would love to";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "2";
            chat.message   = "I can introduce you to her. She always loves talking to people about art";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "1";
            chat.message   = "I don't know that much about it. I just really enjoy looking at it";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "2";
            chat.message   = "I don't know a lot about art either. I'm just going to support her tonight.";
            mHolder.add(chat);
        }
        {
            ChatLHolder chat = new ChatLHolder();
            chat.sender_id = "1";
            chat.message   = "You're a good friend. What is your interest?";
            mHolder.add(chat);
        }
        mAdaper.notifyDataSetChanged();
    }

    private void scrollMyListViewToBottom() {
        lsvw_chat_00.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lsvw_chat_00.setSelection(mAdaper.getCount() - 1);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FirebaseMessageService.MY_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);

        myReceiver.setOnReceiveListener(new MyReceiver.OnReceiveListener() {
            @Override
            public void onReceive(String body, JSONObject data, String title) {
                try {
                    JSONObject message = data.getJSONObject("body");
                    ChatLHolder chat = new ChatLHolder();
                    chat.sender_id = message.getString("form");
                    chat.message   = message.getString("message");
                    mHolder.add(chat);
                    new AudioPlayer().play(getActivity());
                    mAdaper.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG", "Data HERE !!!!!!!"+ data);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }


}
