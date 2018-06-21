package g.rezza.moch.mobilesales.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import g.rezza.moch.mobilesales.Activity.Product.TicketsActivity;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.component.MenuGridView;
import g.rezza.moch.mobilesales.lib.Master.ActivityWthHdr;

public class ProductActivity extends ActivityWthHdr {

    private MenuGridView menu_00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    protected void onPostLayout() {
        menu_00 = (MenuGridView) findViewById(R.id.menu_00);
        menu_00.reloadMenu();
        menu_00.addMenu(1,r.getString(R.string.ticket_events),getResources().getDrawable(R.drawable.ic_product_kiostix));
        menu_00.addMenu(-99,"",null);
    }

    @Override
    protected void registerListener() {
        menu_00.setOnSelectedListener(new MenuGridView.OnSelectedListener() {
            @Override
            public void onSelected(int menuID) {
                Intent intent = new Intent(ProductActivity.this, TicketsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitleHeader(r.getString(R.string.product));
        onPostLayout();
    }
}
