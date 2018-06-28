package g.rezza.moch.chatid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.chatid.adapter.ChatRandomAdapter;
import g.rezza.moch.chatid.holder.ChatRandomHolder;
import g.rezza.moch.chatid.lib.Mylib;

public class RandomChatActivity extends AppCompatActivity {

    private RecyclerView        rcvw_chat_00;
    private ChatRandomAdapter   chatRandomAdapter;
    private ArrayList<ChatRandomHolder> mlist = new ArrayList<>();

    private EditText        edtx_message_00;
    private RelativeLayout  rvly_send_00;
    private ImageView       imvw_end_00;
    private RelativeLayout  rvly_notif_00;
    private TextView        txvw_no_00;
    private TextView        txvw_yes_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_chat);


        rcvw_chat_00        = (RecyclerView)    findViewById(R.id.rcvw_chat_00);
        edtx_message_00     = (EditText)        findViewById(R.id.edtx_message_00);
        rvly_send_00        = (RelativeLayout)  findViewById(R.id.rvly_send_00);
        rvly_notif_00       = (RelativeLayout)  findViewById(R.id.rvly_notif_00);
        imvw_end_00         = (ImageView)       findViewById(R.id.imvw_end_00);
        txvw_no_00          = (TextView)        findViewById(R.id.txvw_no_00);
        txvw_yes_00         = (TextView)        findViewById(R.id.txvw_yes_00);

        chatRandomAdapter   = new ChatRandomAdapter(mlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvw_chat_00.setLayoutManager(mLayoutManager);
        rcvw_chat_00.setItemAnimator(new DefaultItemAnimator());
        rcvw_chat_00.setAdapter(chatRandomAdapter);

        rvly_notif_00.bringToFront();
        rvly_notif_00.setVisibility(View.GONE);

        createData();
        initListener();
    }

    private void initListener(){
        imvw_end_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mylib.hideSoftKeyboard(RandomChatActivity.this);
                onBackPressed();
            }
        });

        txvw_no_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(RandomChatActivity.this, R.anim.fade_out);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        rvly_notif_00.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                rvly_notif_00.startAnimation(anim);
            }
        });

        txvw_yes_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RandomChatActivity.this, MainActivity.class));
                RandomChatActivity.this.finish();
            }
        });
        rvly_notif_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void createData(){
        {
            ChatRandomHolder holder = new ChatRandomHolder();
            holder.receiver = "Rezza";
            holder.message  = "In this tutorial we are going to learn how to render a simple RecyclerView with a custom layout In this tutorial we are going to learn how to render a simple RecyclerView with a custom layout";
            mlist.add(holder);
        }

        {
            ChatRandomHolder holder = new ChatRandomHolder();
            holder.receiver = "XYZ";
            holder.message  = "We’ll also learn writing a adapter class";
            mlist.add(holder);
        }

        {
            ChatRandomHolder holder = new ChatRandomHolder();
            holder.receiver = "XYZ";
            holder.message  = "We’ll also learn writing a adapter class";
            mlist.add(holder);
        }
        {
            ChatRandomHolder holder = new ChatRandomHolder();
            holder.receiver = "XYZ";
            holder.message  = "We’ll also learn writing a adapter class";
            mlist.add(holder);
        }
        {
            ChatRandomHolder holder = new ChatRandomHolder();
            holder.receiver = "Rezza";
            holder.message  = "adding list divider and row click listener";
            mlist.add(holder);
        }

        chatRandomAdapter.notifyDataSetChanged();

        rvly_send_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatRandomHolder holder = new ChatRandomHolder();
                holder.receiver = "Rezza";
                holder.message  = edtx_message_00.getText().toString();
                mlist.add(holder);
                chatRandomAdapter.notifyDataSetChanged();

                edtx_message_00.setText("");
                rcvw_chat_00.scrollToPosition(chatRandomAdapter.getItemCount()-1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (rvly_notif_00.getVisibility() == View.VISIBLE){
            return;
        }
        Animation anim = AnimationUtils.loadAnimation(RandomChatActivity.this, R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rvly_notif_00.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rvly_notif_00.startAnimation(anim);
    }
}
