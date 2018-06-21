package g.rezza.moch.clientdashboard;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import g.rezza.moch.clientdashboard.adapter.ViewPagerAdapter;
import g.rezza.moch.clientdashboard.component.PopupParameter;
import g.rezza.moch.clientdashboard.view.TransactionDetailView;
import g.rezza.moch.clientdashboard.view.TransactionGrafikView;

public class DetailEventActivity extends AppCompatActivity {

    private ViewPager               mViewPager;
    private TabLayout               tbvw_00;
    private ImageView               imvw_vliter_02;
    private TextView                txvw_parameter_02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        mViewPager          = (ViewPager)   findViewById(R.id.container);
        imvw_vliter_02      = (ImageView)   findViewById(R.id.imvw_vliter_02);
        txvw_parameter_02   = (TextView)    findViewById(R.id.txvw_parameter_02);
        tbvw_00             = (TabLayout) findViewById(R.id.tbvw_00);

        tbvw_00.setupWithViewPager(mViewPager);

        final PopupParameter parameter = new PopupParameter(DetailEventActivity.this);
        imvw_vliter_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameter.show();
            }
        });
        parameter.setOnPositifClickListener(new PopupParameter.OnPositifClickListener() {
            @Override
            public void onClick(int type, String value) {
                txvw_parameter_02.setText(value);
            }
        });

        setupViewPager(mViewPager);


//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransactionGrafikView(), "Chart");
        adapter.addFragment(new TransactionDetailView(), "Table");
        viewPager.setAdapter(adapter);
    }

}
