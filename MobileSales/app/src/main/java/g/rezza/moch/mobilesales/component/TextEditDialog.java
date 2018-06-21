package g.rezza.moch.mobilesales.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 28/12/17.
 */

public class TextEditDialog extends Dialog {

    private RelativeLayout rvly_root;
    private LinearLayout lnly_body;
    private LinearLayout lnly_footer;
    private EditextStandardC edtx_00;
    private SimpleDatePicker smpd_00;

    public TextEditDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT < 23) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);

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

        edtx_00 = new EditextStandardC(getContext(), null);
        lnly_body.addView(edtx_00);

        smpd_00 = new SimpleDatePicker(getContext(), null);
        lnly_body.addView(smpd_00);

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

    public void setTitle(String title){
        edtx_00.setTitle(title);
        smpd_00.setTitle(title);
    }

    public void setValue(String value){
        edtx_00.setValue(value);
        smpd_00.setValue(value);

    }

    public void setTypeInput(int inputType){
        if (inputType == InputType.TYPE_CLASS_DATETIME){
            edtx_00.setVisibility(View.GONE);
            smpd_00.setVisibility(View.VISIBLE);
        }
        else {
            edtx_00.setVisibility(View.VISIBLE);
            smpd_00.setVisibility(View.GONE);
        }
        edtx_00.setInputType(inputType);
    }

    public String getValue(){
        if (edtx_00.getVisibility() == View.GONE){
            return smpd_00.getValue();
        }
        else {
            return edtx_00.getValue();
        }

    }

    private OnSubmitListener mListener;
    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        mListener = onSubmitListener;
    }
    public interface OnSubmitListener{
        public void onSubmit(String text);
    }
}
