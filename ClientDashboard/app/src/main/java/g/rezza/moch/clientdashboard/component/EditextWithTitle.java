package g.rezza.moch.clientdashboard.component;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import g.rezza.moch.clientdashboard.R;

/**
 * Created by Rezza on 9/20/17.
 */

public class EditextWithTitle extends RelativeLayout implements TextWatcher, TextView.OnEditorActionListener{
    public static final String TAG = "RZ Editext WT Title";

    public static final int TYPE_NORMAL     = 1;
    public static final int TYPE_PASSWORD   = 2;
    public static final int TYPE_EMAIL      = 3;

    private EditText edtx_normal;
    private EditText edtx_password;
    private EditText edtx_email;
    private TextView txvw_normal;

    private int type = 1;
    public EditextWithTitle(Context context) {
        super(context);
    }

    public EditextWithTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_editext_with_title, this, true);

        edtx_normal     = (EditText) findViewById(R.id.edtx_normal);
        edtx_password   = (EditText) findViewById(R.id.edtx_password);
        edtx_email      = (EditText) findViewById(R.id.edtx_email);
        txvw_normal     = (TextView) findViewById(R.id.txvw_normal);

        edtx_normal.setVisibility(View.GONE);
        edtx_password.setVisibility(View.GONE);
        edtx_email.setVisibility(View.GONE);

        edtx_normal.addTextChangedListener(this);
        edtx_password.addTextChangedListener(this);
        edtx_email.addTextChangedListener(this);
    }

    public void create(int type){
        Log.d(TAG,"Create");
        this.type = type;
        txvw_normal.setVisibility(View.INVISIBLE);

        switch (this.type){
            case TYPE_NORMAL:
                edtx_normal.setVisibility(View.VISIBLE);
                break;
            case TYPE_PASSWORD:
                edtx_password.setVisibility(View.VISIBLE);
                break;
            case TYPE_EMAIL:
                edtx_email.setVisibility(View.VISIBLE);
                break;
        }
        edtx_password.setText("");
        edtx_normal.setText("");
        edtx_email.setText("");
    }

    public void setTitleColor(int color){
        txvw_normal.setTextColor(color);
        edtx_password.setHintTextColor(color);
        edtx_email.setHintTextColor(color);
        edtx_email.setHintTextColor(color);
    }

    public void setTextColor(int color){
        edtx_password.setTextColor(color);
        edtx_email.setTextColor(color);
        edtx_email.setTextColor(color);
    }

    public void setBackground(int resource){
        edtx_normal.setBackgroundResource(resource);
        edtx_password.setBackgroundResource(resource);
        edtx_email.setBackgroundResource(resource);
    }

    public void setTitle(String title){
        txvw_normal.setText(title);
        edtx_normal.setHint(title);
        edtx_password.setHint(title);
        edtx_email.setHint(title);
    }

    public String getValue(){
        switch (this.type){
            case TYPE_NORMAL:
                return edtx_normal.getText().toString();
            case TYPE_PASSWORD:
                return edtx_password.getText().toString();
            case TYPE_EMAIL:
                return edtx_email.getText().toString();
            default:
                return edtx_normal.getText().toString();
        }
    }

    public void setValue(String mValue){
        edtx_normal.setText(mValue);
        edtx_password.setText(mValue);
        edtx_email.setText(mValue);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()){
            txvw_normal.setVisibility(View.INVISIBLE);
        }
        else {
            txvw_normal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
