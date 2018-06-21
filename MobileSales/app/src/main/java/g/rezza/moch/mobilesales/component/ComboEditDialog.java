package g.rezza.moch.mobilesales.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.holder.SpinerHolder;

/**
 * Created by rezza on 28/12/17.
 */

public class ComboEditDialog extends Dialog {

    private RelativeLayout rvly_root;
    private LinearLayout lnly_body;
    private LinearLayout lnly_footer;

    private SimpleSpinner spnr_00;

    public ComboEditDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @SuppressLint("ResourceType")
    public void create(){
        int dp5 = getContext().getResources().getDimensionPixelSize(R.dimen.size_5dp);
        rvly_root = new RelativeLayout(getContext(), null);
        rvly_root.setBackgroundColor(Color.WHITE);
        rvly_root.setPadding(dp5,dp5,dp5,dp5);
        RelativeLayout.LayoutParams lp_root = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(rvly_root, lp_root);

        createBody();
        createFooter();
    }

    @SuppressLint("ResourceType")
    private void createBody(){
        int dp10 = getContext().getResources().getDimensionPixelSize(R.dimen.size_10dp);

        lnly_body = new LinearLayout(getContext(), null);
        lnly_body.setId(12000002);
        lnly_body.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,dp10,0,0);
        rvly_root.addView(lnly_body, lp);

        spnr_00 = new SimpleSpinner(getContext(), null);
        lnly_body.addView(spnr_00);

//        ArrayList<String> x
//        spnr_00.setChoosers();

    }

    @SuppressLint("ResourceType")
    private void createFooter(){
        lnly_footer = new LinearLayout(getContext(), null);
        lnly_footer.setId(12000003);
        lnly_footer.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lnly_body.addView(lnly_footer,lp);

        Button ok = new Button(getContext(), null);
        ok.setText("Ok");
        ok.setTextAppearance(getContext(),R.style.TextView_White_Montserrat);
        ok.setBackgroundResource(R.drawable.button_round_green);
        lnly_footer.addView(ok, lp);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null){
                    mListener.onSubmit(getValue());
                }
            }
        });

    }

    public void setChooser(ArrayList<SpinerHolder> data, String value){
        spnr_00.setChoosers(data);
        spnr_00.setValue(value);
    }

    public void setTitle(String title){
        spnr_00.setTitle(title);
    }

    public void setValue(String value){
        spnr_00.setValue(value);
    }


    public SpinerHolder getValue(){
        return spnr_00.getValue();
    }

    private OnSubmitListener mListener;
    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        mListener = onSubmitListener;
    }
    public interface OnSubmitListener{
        public void onSubmit(SpinerHolder text);
    }
}
