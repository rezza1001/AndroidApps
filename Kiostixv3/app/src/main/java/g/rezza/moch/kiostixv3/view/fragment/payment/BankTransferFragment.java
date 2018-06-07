package g.rezza.moch.kiostixv3.view.fragment.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.HomeActivity;
import g.rezza.moch.kiostixv3.database.PaymentDB;

/**
 * Created by rezza on 09/02/18.
 */

public class BankTransferFragment extends Fragment {
    private static final String TAG = "BankTransfer";
    private TextView    txvw_body_00;


    public static Fragment newInstance(String id) {
        Fragment frag   = new BankTransferFragment();
        Bundle args     = new Bundle();
        args.putString("PAYMENT_ID", id);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_fragment_payment_bank_tf, container, false);
        txvw_body_00    = view.findViewById(R.id.txvw_body_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        String payment_id = bundle.getString("PAYMENT_ID");
        Log.d(TAG,"PAYMENT ID "+payment_id);

        PaymentDB paymentDB = new PaymentDB();
        paymentDB.getData(getActivity(),payment_id);
        Log.d(TAG,"PAYMENT NAME "+paymentDB.name);

        txvw_body_00.setText(Html.fromHtml(paymentDB.description));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }




}
