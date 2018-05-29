package g.rezza.moch.hrsystem.component;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.hrsystem.R;

/**
 * Created by rezza on 28/12/17.
 */

public class TextKeyValueView extends RelativeLayout {
    private RelativeLayout rvly_edit;
    private TextView text_00;
    private TextView text_01;

    private String id;
    private int inputType = InputType.TYPE_CLASS_TEXT;

    public TextKeyValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_text_kv, this, true);
        createLayout();
        registerListener();
    }


    private void createLayout(){
        rvly_edit = (RelativeLayout) findViewById(R.id.rvly_edit);
        text_00   = (TextView) findViewById(R.id.text_00) ;
        text_01   = (TextView) findViewById(R.id.text_01) ;


    }

    private void registerListener(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvly_edit.getVisibility() == GONE){
                    return;
                }
                if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                    PasswordEditDialog dialog =  new PasswordEditDialog(getContext());
                    dialog.create(getValue());
                    dialog.show();
                    dialog.setOnSubmitListener(new PasswordEditDialog.OnSubmitListener() {
                        @Override
                        public void onSubmit(String text) {
                            setValue(text);
                        }
                    });
                }
                else {
                    TextEditDialog dialog =  new TextEditDialog(getContext());
                    dialog.create();
                    dialog.setTitle(text_00.getText().toString());
                    dialog.setValue(getValue());
                    dialog.setTypeInput(inputType);
                    dialog.show();
                    dialog.setOnSubmitListener(new TextEditDialog.OnSubmitListener() {
                        @Override
                        public void onSubmit(String text) {
                            setValue(text);
                        }
                    });
                }

            }

        });


    }


    public void setTitle(String title){
        text_00.setText(title);
    }

    public void setValue(String value){
        text_01.setText(getStringNotNull(value));
    }

    public String getValue(){
        return text_01.getText().toString();
    }

    public void setID(String id){
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setReadOnly(boolean disable){
        if (disable) {
            rvly_edit.setVisibility(View.GONE);
        }else {
            rvly_edit.setVisibility(View.VISIBLE);
        }
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        text_01.setInputType(InputType.TYPE_CLASS_TEXT |inputType);
    }

    public interface OnEditListener{
        public void onEdit(int id, int inputType, String title, String value);
    }

    public String getStringNotNull(String data){
        try {
            if (data.equalsIgnoreCase("NULL")){
                return "";
            }
            else {
                return data;
            }
        }catch (Exception e){
            return "";
        }

    }
}
