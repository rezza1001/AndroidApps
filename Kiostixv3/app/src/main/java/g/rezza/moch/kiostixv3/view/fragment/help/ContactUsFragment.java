package g.rezza.moch.kiostixv3.view.fragment.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.database.CustomerDB;

/**
 * Created by rezza on 09/02/18.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactUsFragment";

    private LinearLayout lnly_subject_00;
    private LinearLayout lnly_subject_01;
    private LinearLayout lnly_subject_02;
    private LinearLayout lnly_subject_03;

    private TextView txvw_subject_01;
    private TextView txvw_subject_02;
    private TextView txvw_subject_03;

    private ImageView imvw_subject_01;
    private ImageView imvw_subject_02;
    private ImageView imvw_subject_03;

    private EditText edtx_name_00;
    private EditText edtx_email_00;
    private EditText edtx_phone_00;
    private EditText edtx_subject_00;
    private EditText edtx_note_00;

    private Button bbtn_action_00;

    public static Fragment newInstance() {
        Fragment frag   = new ContactUsFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_help_contactus, container, false);
        lnly_subject_00 = view.findViewById(R.id.lnly_subject_00);
        lnly_subject_01 = view.findViewById(R.id.lnly_subject_01);
        lnly_subject_02 = view.findViewById(R.id.lnly_subject_02);
        lnly_subject_03 = view.findViewById(R.id.lnly_subject_03);
        txvw_subject_01 = view.findViewById(R.id.txvw_subject_01);
        txvw_subject_02 = view.findViewById(R.id.txvw_subject_02);
        txvw_subject_03 = view.findViewById(R.id.txvw_subject_03);
        imvw_subject_01 = view.findViewById(R.id.imvw_subject_01);
        imvw_subject_02 = view.findViewById(R.id.imvw_subject_02);
        imvw_subject_03 = view.findViewById(R.id.imvw_subject_03);
        edtx_name_00    = view.findViewById(R.id.edtx_name_00);
        edtx_email_00   = view.findViewById(R.id.edtx_email_00);
        edtx_phone_00   = view.findViewById(R.id.edtx_phone_00);
        edtx_subject_00 = view.findViewById(R.id.edtx_subject_00);
        edtx_note_00    = view.findViewById(R.id.edtx_note_00);
        bbtn_action_00  = view.findViewById(R.id.bbtn_action_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lnly_subject_01.setOnClickListener(this);
        lnly_subject_02.setOnClickListener(this);
        lnly_subject_03.setOnClickListener(this);
        edtx_subject_00.setKeyListener(null);
        bbtn_action_00.setOnClickListener(this);

        lnly_subject_00.setVisibility(View.GONE);
        imvw_subject_01.setVisibility(View.GONE);
        imvw_subject_02.setVisibility(View.GONE);
        imvw_subject_03.setVisibility(View.GONE);
        edtx_subject_00.setTag("00");

        edtx_subject_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lnly_subject_00.getVisibility() == View.GONE){
                    lnly_subject_00.setVisibility(View.VISIBLE);
                }
                else {
                    lnly_subject_00.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CustomerDB customerDB = new CustomerDB();
        customerDB.getData(getActivity());
        if (!customerDB.id.isEmpty()){
            edtx_name_00.setText(customerDB.name);
            edtx_email_00.setText(customerDB.email);
            edtx_phone_00.setText(customerDB.phone);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (view == bbtn_action_00){
            send();
        }
        else {
            switchCategory(view);
        }
    }

    private void switchCategory(View view){
        imvw_subject_02.setVisibility(View.GONE);
        imvw_subject_03.setVisibility(View.GONE);
        imvw_subject_01.setVisibility(View.GONE);
        if (view == lnly_subject_01){
            imvw_subject_01.setVisibility(View.VISIBLE);
            edtx_subject_00.setText(txvw_subject_01.getText().toString());
            edtx_subject_00.setTag("01");
        }
        else if (view == lnly_subject_02){
            edtx_subject_00.setText(txvw_subject_02.getText().toString());
            imvw_subject_02.setVisibility(View.VISIBLE);
            edtx_subject_00.setTag("02");
        }
        else if (view == lnly_subject_03){
            edtx_subject_00.setText(txvw_subject_03.getText().toString());
            imvw_subject_03.setVisibility(View.VISIBLE);
            edtx_subject_00.setTag("03");
        }
        lnly_subject_00.setVisibility(View.GONE);
    }

    private void send(){
        String name     = edtx_name_00.getText().toString();
        String email    = edtx_email_00.getText().toString();
        String phone    = edtx_phone_00.getText().toString();
        String note     = edtx_note_00.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.name_required),Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.email_required),Toast.LENGTH_SHORT).show();
            return;
        }
        else if (phone.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.phone_required),Toast.LENGTH_SHORT).show();
            return;
        }
        else if (edtx_subject_00.getTag().equals("00")){
            Toast.makeText(getActivity(), getResources().getString(R.string.subject_required),Toast.LENGTH_SHORT).show();
            return;
        }
        else if (note.isEmpty()){
            Toast.makeText(getActivity(), getResources().getString(R.string.message_required),Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
