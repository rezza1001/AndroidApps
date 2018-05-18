package g.rezza.moch.unileverapp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.SignInActivity;
import g.rezza.moch.unileverapp.component.TextViewProfile;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.OrderDB;
import g.rezza.moch.unileverapp.database.OutletDB;
import g.rezza.moch.unileverapp.lib.LoadingScreen;

/**
 * Created by rezza on 09/02/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private TextView txvw_name_00;
    private TextViewProfile txpf_outletid_00;
    private TextViewProfile txpf_username_00;
    private TextViewProfile txpf_o_address_00;
    private TextViewProfile txpf_o_city_00;
    private TextViewProfile txpf_o_phone_00;
    private TextViewProfile txpf_o_email_00;
    private TextViewProfile txpf_o_term_00;
    private RelativeLayout  rvly_logout_00;
    private LoadingScreen   loadingScreen;
    private OutletDB outletDB;

    public static Fragment newInstance() {
        Fragment frag   = new ProfileFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_home_profile, container, false);
        txvw_name_00    = view.findViewById(R.id.txvw_name_00);
        txpf_outletid_00    = view.findViewById(R.id.txpf_outletid_00);
        txpf_username_00    = view.findViewById(R.id.txpf_username_00);
        txpf_o_address_00    = view.findViewById(R.id.txpf_o_address_00);
        txpf_o_city_00    = view.findViewById(R.id.txpf_o_city_00);
        txpf_o_phone_00    = view.findViewById(R.id.txpf_o_phone_00);
        txpf_o_email_00    = view.findViewById(R.id.txpf_o_email_00);
        txpf_o_term_00    = view.findViewById(R.id.txpf_o_term_00);
        rvly_logout_00    = view.findViewById(R.id.rvly_logout_00);
        loadingScreen     = new LoadingScreen(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txpf_outletid_00.setTitle("Outlet ID");
        txpf_username_00.setTitle(getResources().getString(R.string.username));
        txpf_o_address_00.setTitle(getResources().getString(R.string.address));
        txpf_o_city_00.setTitle(getResources().getString(R.string.city));
        txpf_o_phone_00.setTitle(getResources().getString(R.string.phone));
        txpf_o_email_00.setTitle(getResources().getString(R.string.email));
        txpf_o_term_00.setTitle(getResources().getString(R.string.term_of_payment));

        txpf_o_phone_00.setImage(R.drawable.ic_phone_android);
        txpf_o_email_00.setImage(R.drawable.ic_mail_outline);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void initData() {
        outletDB = new OutletDB();
        outletDB.getMine(getActivity());

        txvw_name_00.setText(outletDB.outlet_name);
        txpf_outletid_00.setValue(outletDB.outlet_id);
        txpf_username_00.setValue(outletDB.username);
        txpf_o_address_00.setValue(outletDB.outlet_address);
    }

    private void initListener(){
        rvly_logout_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingScreen.setMessage("Please Wait...");
                loadingScreen.show();
                handler.sendEmptyMessageDelayed(-99, 1000);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case -99:
                    logout();
                    break;
            }
            return false;
        }
    });

    private void logout(){
        loadingScreen.dimiss();
        OutletDB outletDB = new OutletDB();
        outletDB.clearData(getActivity());

        ChartDB chartDB = new ChartDB();
        chartDB.clearData(getActivity());

        OrderDB orderDB = new OrderDB();
        orderDB.clearData(getActivity());

        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);

        getActivity().finish();
    }

}
