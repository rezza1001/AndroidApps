package g.rezza.moch.kiostixv3.component;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import g.rezza.moch.kiostixv3.R;

/**
 * Created by rezza on 13/01/18.
 */

public class SearchViw extends RelativeLayout {

    private ImageView imvw_clear_00;
    private ImageView imvw_back_00;
    private EditText edtx_seacrh_00;

    public SearchViw(Context context) {
        super(context, null);
    }

    public SearchViw(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_serach, this, true);

        imvw_clear_00   = (ImageView) findViewById(R.id.imvw_clear_00);
        imvw_back_00    = (ImageView) findViewById(R.id.imvw_back_00);
        edtx_seacrh_00  = (EditText) findViewById(R.id.edtx_seacrh_00);

        hideButtonClear();

        imvw_back_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(700);

                Animation RightSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.right_left);
                SearchViw.this.startAnimation(RightSwipe);
                handler2.sendEmptyMessageDelayed(1,500);
            }
        });

        imvw_clear_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                edtx_seacrh_00.setText("");
            }
        });

        edtx_seacrh_00.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideButtonClear();
                if (onSearchListenr != null){
                    onSearchListenr.onSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void createView(){

    }

    private void hideButtonClear(){
        if (edtx_seacrh_00.getText().toString().isEmpty()){
            imvw_clear_00.setVisibility(View.GONE);
        }
        else {
            imvw_clear_00.setVisibility(View.VISIBLE);
        }
    }

    private OnBackListener pBackListenr;
    public void setOnBackListener(OnBackListener onBackListener){
        pBackListenr = onBackListener;
    }
    public interface OnBackListener{
        public void onBack();
    }

    private OnSearchListenr onSearchListenr;
    public void setOnSearchListenr(OnSearchListenr onSearchListenr){
        this.onSearchListenr = onSearchListenr;
    }

    public interface OnSearchListenr{
        public void onSearch(String text);
    }

    public void show(){
        Animation RightSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.left_right);
        SearchViw.this.startAnimation(RightSwipe);
        SearchViw.this.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageAtTime(1,500);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
                Activity activity = (Activity)getContext();
            if (message.what == 1){
                edtx_seacrh_00.requestFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
            else {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(SearchViw.this.getWindowToken(),0);
            }

            return false;
        }
    });

    Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            SearchViw.this.setVisibility(View.GONE);
            edtx_seacrh_00.setText("");
            if (pBackListenr != null){
                pBackListenr.onBack();

            }
            return false;
        }
    });
}
