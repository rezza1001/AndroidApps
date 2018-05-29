package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import g.rezza.moch.kiospos.Activitty.DetailActivity;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.EditextSearch;
import g.rezza.moch.kiospos.component.GridViewComponent;
import g.rezza.moch.kiospos.holder.Dummy;
import g.rezza.moch.kiospos.holder.EventHolder;
import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.lib.MasterView;

/**
 * Created by Rezza on 11/10/17.
 */

public class AllEventGridView extends MasterView {
    public static final String TAG = "AllEventGridView";

    private GridViewComponent gvc_datas_30;
    private EditextSearch   edtx_seacrh_40;

    public AllEventGridView(Context context) {
        super(context);
    }

    public AllEventGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_all_event_grid, this, true);
    }

    public void create(EditextSearch view){
        edtx_seacrh_40 = view;
        creteLayout();
        createData();
    }


    private void creteLayout(){
        gvc_datas_30 = (GridViewComponent)   findViewById(R.id.gvc_datas_30);

        edtx_seacrh_40.setOnSearchListener(new EditextSearch.OnSearchListener() {
            @Override
            public void onSearch(String text) {
                searchComponent(text);
            }
        });

        edtx_seacrh_40.setOnChangeListener(new MasterComponent.ChangeListener() {
            @Override
            public void after(String s) {
                if (s.isEmpty()){
                    searchComponent("");
                }
            }
        });

        gvc_datas_30.setOnItemClickListener(new GridViewComponent.OnItemClickListener() {
            @Override
            public void onItemClick(EventHolder event) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    private void searchComponent(String text){
        gvc_datas_30.filter(text);

    }


    private void createData(){
        if (gvc_datas_30 != null){
            gvc_datas_30.create(Dummy.dataFeatured(getContext()));
            searchComponent("");
        }
        else {
            Log.d(TAG,"createData Is NULL" );
        }

    }
}
