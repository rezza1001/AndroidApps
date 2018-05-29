package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 10/13/17.
 */

public class TextKeyValue extends MasterComponent {

    private TextView    texvw_key_20;
    private TextView    texvw_value_21;
    public TextKeyValue(Context context) {
        super(context);
    }

    public TextKeyValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_text_keyvalue, this, true);

        texvw_key_20    = (TextView)    findViewById(R.id.texvw_key_20);
        texvw_value_21  = (TextView)    findViewById(R.id.texvw_value_21);
    }

    public void create(String key, String value){
        texvw_key_20.setText(formatText(16, key));
        texvw_value_21.setText(formatText(19, value));
    }

    private String formatText(int maxtext, String text){
        if (text.length() > maxtext){
            String value = "";
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<text.length(); i++){
                char chr = text.charAt(i);

                value = value + chr;

                if (i % maxtext == 0 && i != 0){

                    sb.append(value.trim());
                    sb.append("\n");
                    value = "";
                }
            }
            return sb.toString();
        }
        else {
            return text;
        }
    }
}
