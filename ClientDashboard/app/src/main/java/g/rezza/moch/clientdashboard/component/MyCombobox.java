package g.rezza.moch.clientdashboard.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

import g.rezza.moch.clientdashboard.R;

/**
 * Created by Rezza on 3/6/17.
 */

public class MyCombobox extends RelativeLayout{
    private Spinner spn_001;
    private String mTitle ="";


    public MyCombobox(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mycombobox,this,true);
        spn_001 = (Spinner) findViewById(R.id.spn_001);

        spn_001.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    mListener.after();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public MyCombobox(Context context) {
        super(context, null);
    }

    public void setChoosers(ArrayList<String> pList){
        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.component_mycomboboxadapter) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(R.id.text1)).setText("");
                    ((TextView)v.findViewById(R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.component_mycomboboxitem);
        for (int i=0; i<pList.size(); i++){
            adapter.add(pList.get(i));
        }

        if (mTitle != ""){
            adapter.add(mTitle);
        }
        else {
            adapter.add("Please Choose");
        }
        // attaching data adapter to spinner
        spn_001.setAdapter(adapter);
        spn_001.setSelection(adapter.getCount()); //display hint
    }

    public void setTitle(String pText){
        mTitle = pText;
    }

    public String getValue(){
        String x =  spn_001.getSelectedItem().toString();
        if (x.equals(mTitle)){
            x = "";
        }

        return x;
    }

    public int getValuePosition(){
        int x =  spn_001.getSelectedItemPosition()+1;
        return x ;
    }

    public void setEnabled(boolean enable){
        spn_001.setEnabled(enable);
    }

    public void setValue(int x){
        spn_001.setSelection(x);
    }


    public ChangeListener mListener;
    public void setOnChangeListener(ChangeListener pListener){
        mListener = pListener;
    }
    public interface ChangeListener{
        public void after();
    }
}
