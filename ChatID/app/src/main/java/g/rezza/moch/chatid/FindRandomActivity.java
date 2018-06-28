package g.rezza.moch.chatid;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FindRandomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_random);

        mHandler.sendEmptyMessageDelayed(1,3000);
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            startActivity(new Intent(FindRandomActivity.this, RandomChatActivity.class));
            FindRandomActivity.this.finish();
            return false;
        }
    });
}
