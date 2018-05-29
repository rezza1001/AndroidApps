package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 10/12/17.
 */

public class EditextSearch extends MasterComponent implements TextWatcher {
    private EditText    edtx_default;
    private ImageView   imvw_icon_30;

    public EditextSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_editext_search, this, true);
        edtx_default    = (EditText)    findViewById(R.id.edtx_default_30);
        imvw_icon_30    = (ImageView)   findViewById(R.id.imvw_icon_30);

        edtx_default.addTextChangedListener(this);

        imvw_icon_30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPressListener != null){
                    mPressListener.onSearch(getValue());
                }
            }
        });
    }

    public EditextSearch(Context context) {
        super(context, null);
    }

    public void setTitle(String pText) {
        edtx_default.setHint(pText);
    }

    public void setValue(String pText) {
        edtx_default.setText(pText);
    }

    public String getValue() {
        String x = edtx_default.getText().toString();
        x = edtx_default.getText().toString();

        return x;
    }


    public void disable() {
        edtx_default.setEnabled(false);
    }

    public void enable() {
        edtx_default.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mListener != null) {
            mListener.after(s.toString());
        }

        if (!s.toString().isEmpty()){
            imvw_icon_30.setBackgroundResource(R.drawable.rounded_right_orange_full);
        }
        else {
            imvw_icon_30.setBackgroundResource(R.drawable.rounded_right_orange);
        }
    }

    public void setOnSearchListener(OnSearchListener pListener) {
        mPressListener = pListener;
    }

    private OnSearchListener mPressListener;

    public interface OnSearchListener {
        public void onSearch(String text);
    }
}
