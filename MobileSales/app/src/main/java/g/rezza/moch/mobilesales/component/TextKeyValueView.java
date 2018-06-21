package g.rezza.moch.mobilesales.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.mobilesales.DataStatic.DataSpiner;
import g.rezza.moch.mobilesales.R;

/**
 * Created by rezza on 28/12/17.
 */

public class TextKeyValueView extends RelativeLayout {
    private RelativeLayout rvly_root;
    private RelativeLayout rvly_edit;
    private TextView text_00;
    private TextView text_01;
    private ImageView imvw_00;

    private String id;
    private int inputType = InputType.TYPE_CLASS_TEXT;

    public TextKeyValueView(Context context) {
        super(context);
    }


    public TextKeyValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createLayout();
        registerListener();
    }


    @SuppressLint("ResourceType")
    private void createLayout(){
        rvly_root = new RelativeLayout(getContext(), null);
        rvly_root.setBackgroundColor(getResources().getColor(R.color.colorWhite_00));
        int padding = getResources().getDimensionPixelSize(R.dimen.size_5dp);
        rvly_root.setPadding(padding,padding,padding,padding);

        LayoutParams root_param  = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(rvly_root, root_param);

        text_00 = new TextView(getContext(),null);
        text_00.setId(100);
        text_00.setText("Title");
        text_00.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_11sp));
        text_00.setTextAppearance(getContext(),R.style.TextView_Grey_Montserrat);
        rvly_root.addView(text_00);

        text_01 = new TextView(getContext(),null);
        text_01.setText("");
        text_01.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_12sp));
        text_01.setTextAppearance(getContext(),R.style.TextView_Black_Montserrat);

        LayoutParams text_01_param  = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        text_01_param.addRule(RelativeLayout.BELOW, text_00.getId());
        text_01_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        text_01_param.setMargins(0,0,getResources().getDimensionPixelSize(R.dimen.size_30dp),0);
        rvly_root.addView(text_01, text_01_param);

        rvly_edit = new RelativeLayout(getContext(), null);
        int padding3 = getResources().getDimensionPixelSize(R.dimen.size_3dp);
        rvly_edit.setPadding(padding3,padding3,padding3,padding3);
        LayoutParams edit_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        edit_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rvly_root.addView(rvly_edit, edit_param);

        imvw_00 = new ImageView(getContext(), null);
        imvw_00.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imvw_00.setImageResource(R.drawable.ic_pen_edit);
        int w = getResources().getDimensionPixelSize(R.dimen.size_25dp);
        int h = getResources().getDimensionPixelSize(R.dimen.size_25dp);
        LayoutParams imvw_param = new LayoutParams(w, h);
        rvly_edit.addView(imvw_00, imvw_param);

        View sparator = new View(getContext(), null);
        sparator.setBackgroundColor(Color.LTGRAY);
        int h1 = getResources().getDimensionPixelSize(R.dimen.size_1dp);
        LayoutParams sparator_param = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h1);
        sparator_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rvly_root.addView(sparator, sparator_param);

    }

    private void registerListener(){
        rvly_root.setOnClickListener(new OnClickListener() {
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
