package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.kiospos.lib.MasterView;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.adapter.CategoryAdapter;
import g.rezza.moch.kiospos.holder.CategoryHolder;

/**
 * Created by Rezza on 10/12/17.
 */

public class Category extends MasterView {

    private ListView        lsvw_category_31;
    private Button          bbtn_back_30;
    private CategoryAdapter adapter;
    private ArrayList<CategoryHolder> list;

    public Category(Context context) {
        super(context);
    }

    public Category(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_category, this, true);
        list                = new ArrayList<>();
        lsvw_category_31    = (ListView)    findViewById(R.id.lsvw_category_31);
        bbtn_back_30        = (Button)    findViewById(R.id.bbtn_back_30);
        adapter             = new CategoryAdapter(context,list);
        lsvw_category_31.setAdapter(adapter);

        bbtn_back_30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFinishListener != null){
                    mFinishListener.onBack(Category.this, mData);
                }
            }
        });

        buildData();
    }

    public void create(JSONObject jo){
        mData   = jo;
        disableLogo();
        adapter.notifyDataSetChanged();
        adapter.setOnSelectedItemListener(new CategoryAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(CategoryHolder holder, int position) {
                clearChecked(holder);
                if (mFinishListener != null){
                    try {
                        mData.put("Category",holder.pack());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mFinishListener.onNext(Category.this, mData);
                }
            }
        });
    }

    private void clearChecked(CategoryHolder cat){
        for (CategoryHolder category: list){
            if (!cat.id.equals(category.id)){
                category.checked = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void buildData(){
        {
            CategoryHolder ch = new CategoryHolder();
            ch.id       = "p79ka8mxpwc34";
            ch.value    = "PLATINUM";
            ch.price    = "750000";
            list.add(ch);
        }
        {
            CategoryHolder ch = new CategoryHolder();
            ch.id       = "pz13a8mxpwc3p";
            ch.value    = "GOLD";
            ch.price    = "500000";
            list.add(ch);
        }
        {
            CategoryHolder ch = new CategoryHolder();
            ch.id       = "pl1358mxpwc3r";
            ch.value    = "SILVER";
            ch.price    = "350000";
            list.add(ch);
        }
        adapter.notifyDataSetChanged();
    }
}
