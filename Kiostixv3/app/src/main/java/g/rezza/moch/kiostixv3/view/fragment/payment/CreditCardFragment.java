package g.rezza.moch.kiostixv3.view.fragment.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.component.EditextStandardBlack;

/**
 * Created by rezza on 09/02/18.
 */

public class CreditCardFragment extends Fragment {
    private static final String TAG = "CreditCardFragment";
    private EditextStandardBlack edtx_nocard_00;
    private EditextStandardBlack edtx_cvv_00;
    private EditextStandardBlack edtx_name_00;
    private EditText             edtx_year_00;
    private EditText             edtx_month_00;

    public static Fragment newInstance() {
        Fragment frag   = new CreditCardFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_fragment_payment_creditcard, container, false);
        edtx_nocard_00  = view.findViewById(R.id.edtx_nocard_00);
        edtx_cvv_00     = view.findViewById(R.id.edtx_cvv_00);
        edtx_name_00    = view.findViewById(R.id.edtx_name_00);
        edtx_year_00    = view.findViewById(R.id.edtx_year_00);
        edtx_month_00   = view.findViewById(R.id.edtx_month_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtx_nocard_00.setTitle(getResources().getString(R.string.credit_card_number));
        edtx_nocard_00.setInputType(InputType.TYPE_CLASS_PHONE);
        edtx_nocard_00.setMaxLength(19);
        edtx_nocard_00.setOnTextChangeListener(new EditextStandardBlack.TextChangeListener() {
            @Override
            public void onChange(String character, Editable editable) {
                edtx_nocard_00.removeTextChangedListener();
                edtx_nocard_00.setValue(toCardNumber(character));
                edtx_nocard_00.setSelection();
                edtx_nocard_00.addTextChenge();
            }
        });

        edtx_cvv_00.setTitle(getResources().getString(R.string.cvv));
        edtx_cvv_00.setInputType(InputType.TYPE_CLASS_NUMBER, InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        edtx_cvv_00.setMaxLength(3);

        edtx_name_00.setTitle(getResources().getString(R.string.cc_owner_name));
        edtx_name_00.setMaxLength(30);

    }

    private String toCardNumber(String number){
        number = number.replaceAll("-","");
        edtx_nocard_00.setTag(number);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i< number.length(); i++){
            if (i == 4){
                sb.append("-");
            }
            else if (i % 4 == 0 && i > 4){
                sb.append("-");
            }
            sb.append(number.charAt(i));
        }
        Log.d(TAG, sb.toString());
        return sb.toString();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public JSONObject getData(){
        String cc_no = edtx_nocard_00.getValue().replaceAll("-","");
        String cvv   = edtx_cvv_00.getValue();
        String name  = edtx_name_00.getValue();
        String year  = edtx_year_00.getText().toString();
        String month = edtx_month_00.getText().toString();

        JSONObject data = new JSONObject();
        String error_code = "00";

        if (cc_no.isEmpty()){
            error_code = "-1";
            Toast.makeText(getActivity(), getResources().getString(R.string.credit_card_number)+ " harus diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (cvv.isEmpty()){
            error_code = "-1";
            Toast.makeText(getActivity(), getResources().getString(R.string.cvv)+ " harus diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (name.isEmpty()){
            error_code = "-1";
            Toast.makeText(getActivity(), getResources().getString(R.string.cc_owner_name)+ " harus diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (year.isEmpty()){
            error_code = "-1";
            Toast.makeText(getActivity(), getResources().getString(R.string.expiry_date)+ " harus diisi !", Toast.LENGTH_SHORT).show();
        }
        else if (month.isEmpty()){
            error_code = "-1";
            Toast.makeText(getActivity(), getResources().getString(R.string.expiry_date)+ " harus diisi !", Toast.LENGTH_SHORT).show();
        }
        try {
            data.put("CARD_NUMBER", cc_no);
            data.put("CVV", cvv);
            data.put("NAME", name);
            data.put("YEAR", year);
            data.put("MONTH", month);
            data.put("MONTH", month);
            data.put("ERROR_CODE", error_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
