package g.rezza.moch.grupia.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import g.rezza.moch.grupia.R;

/**
 * Created by rezza on 20/02/18.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private LinearLayout lnly_foto_00;
    private LinearLayout lnly_profile_00;
    private LinearLayout lnly_account_00;

    public static Fragment newInstance(int color) {
        Fragment frag   = new ProfileFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_profile_fragment, container, false);

        lnly_foto_00    = view.findViewById(R.id.lnly_foto_00);
        lnly_profile_00 = view.findViewById(R.id.lnly_profile_00);
        lnly_account_00 = view.findViewById(R.id.lnly_account_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnly_foto_00.setOnClickListener(this);
        lnly_profile_00.setOnClickListener(this);
        lnly_account_00.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
