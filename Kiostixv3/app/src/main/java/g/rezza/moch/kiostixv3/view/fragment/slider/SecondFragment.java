package g.rezza.moch.kiostixv3.view.fragment.slider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.activity.HomeActivity;

/**
 * Created by rezza on 09/02/18.
 */

public class SecondFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    private Button bbtn_skip_00;

    public static Fragment newInstance() {
        Fragment frag   = new SecondFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_fragment_second, container, false);
        bbtn_skip_00    = view.findViewById(R.id.bbtn_skip_00);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bbtn_skip_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }




}
