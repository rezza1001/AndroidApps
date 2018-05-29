package g.rezza.moch.kiospos.Activitty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.EditextSearch;
import g.rezza.moch.kiospos.view.AllEventGridView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_REGISTER_PRINTER = 1;
    public static final int REQUEST_PRINT = 2;

    private RelativeLayout      rvly_parent_10;
    private EditextSearch       edtx_seacrh_40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvly_parent_10      = (RelativeLayout)      findViewById(R.id.rvly_parent_10);
        edtx_seacrh_40      = (EditextSearch)       findViewById(R.id.edtx_seacrh_40);
        openMenu(0);
    }


    private void openMenu(int position){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rvly_parent_10.removeAllViews();

        switch (position){
            case 0:
                AllEventGridView alleventview = new AllEventGridView(MainActivity.this, null);
                rvly_parent_10.requestFocus();
                alleventview.create(edtx_seacrh_40);
                rvly_parent_10.addView(alleventview, lp);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        rvly_parent_10.requestFocus();
    }
}
