package g.rezza.moch.hrsystem.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import g.rezza.moch.hrsystem.R;
/**
 * Created by rezza on 28/12/17.
 */

public class PasswordEditDialog extends Dialog {

    private RelativeLayout rvly_root;
    private LinearLayout lnly_body;
    private LinearLayout lnly_footer;
    private EditextStandardC edtx_00;
    private EditextStandardC edtx_01;
    private EditextStandardC edtx_02;
    private Resources r;
    private String current_pass;

    public PasswordEditDialog(@NonNull Context context) {
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
    public void create(String current){
        current_pass = current;
        r = getContext().getResources();
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
        edtx_01 = new EditextStandardC(getContext(), null);
        edtx_02 = new EditextStandardC(getContext(), null);
        edtx_00.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_01.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_02.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtx_00.setTitle(r.getString(R.string.current_password));
        edtx_01.setTitle(r.getString(R.string.n_password));
        edtx_02.setTitle(r.getString(R.string.confirmation));
        edtx_00.setMandatory(true);
        edtx_01.setMandatory(true);
        edtx_02.setMandatory(true);
        lnly_body.addView(edtx_00);
        lnly_body.addView(edtx_01);
        lnly_body.addView(edtx_02);


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
        ok.setTextAppearance(getContext(),R.style.text_arsenal_white_00);
        ok.setBackgroundResource(R.drawable.button_green);
        lnly_footer.addView(ok, lp);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation()){
                    return;
                }
                dismiss();
                if (mListener != null){
                    mListener.onSubmit(getValue());
                }
            }
        });

    }

    private boolean validation(){
        boolean valid  = false;
        if (!current_pass.equals(edtx_00.getValue())){
            Toast.makeText(getContext(), r.getString(R.string.current_password_not_match), Toast.LENGTH_SHORT).show();
        }
        else if (edtx_01.getValue().length() < 6){
            Toast.makeText(getContext(), r.getString(R.string.invalid_langth_password), Toast.LENGTH_SHORT).show();
        }
        else if (!edtx_01.getValue().equals(edtx_02.getValue())){
            Toast.makeText(getContext(), r.getString(R.string.confirm_password_not_match), Toast.LENGTH_SHORT).show();
        }
        else{
            valid = true;
        }
        return  valid;
    }


    public String getValue(){
        return edtx_01.getValue();

    }

    private OnSubmitListener mListener;
    public void setOnSubmitListener(OnSubmitListener onSubmitListener){
        mListener = onSubmitListener;
    }
    public interface OnSubmitListener{
        public void onSubmit(String text);
    }
}
