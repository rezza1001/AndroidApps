package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.component.EditextSearch;
import g.rezza.moch.kiospos.holder.Dummy;
import g.rezza.moch.kiospos.lib.MasterComponent;
import g.rezza.moch.kiospos.lib.MasterView;

/**
 * Created by Rezza on 11/10/17.
 */

public class AllEventView extends MasterView {

    private HorizontalDatas hzlv_datas_30;
    private HorizontalDatas hzlv_datas_31;
    private HorizontalDatas hzlv_datas_32;
    private EditextSearch   edtx_seacrh_40;

    public AllEventView(Context context) {
        super(context);
    }

    public AllEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_all_event, this, true);
    }

    public void create(EditextSearch view){
        edtx_seacrh_40 = view;
        creteLayout();
        createData();
    }


    private void creteLayout(){
        hzlv_datas_30 = (HorizontalDatas)   findViewById(R.id.hzlv_datas_30);
        hzlv_datas_31 = (HorizontalDatas)   findViewById(R.id.hzlv_datas_31);
        hzlv_datas_32 = (HorizontalDatas)   findViewById(R.id.hzlv_datas_32);

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
    }

    private void searchComponent(String text){
        hzlv_datas_30.filter(text);
        hzlv_datas_31.filter(text);
        hzlv_datas_32.filter(text);

        hzlv_datas_30.setOnFinishListener(new HorizontalDatas.OnFInishLoadData() {
            @Override
            public void onFinish() {
                showHideHorizontalList(hzlv_datas_30);
            }
        });

        showHideHorizontalList(hzlv_datas_30);
        hzlv_datas_31.setOnFinishListener(new HorizontalDatas.OnFInishLoadData() {
            @Override
            public void onFinish() {
                showHideHorizontalList(hzlv_datas_31);
            }
        });

        hzlv_datas_32.setOnFinishListener(new HorizontalDatas.OnFInishLoadData() {
            @Override
            public void onFinish() {
                showHideHorizontalList(hzlv_datas_32);
            }
        });

    }
    private void showHideHorizontalList(HorizontalDatas view){
        if (view.getCountDataShow() == 0){
            view.setVisibility(View.GONE);
        }
        else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void createData(){
        hzlv_datas_30.create("FEATURED EVENTS", Dummy.dataFeatured(getContext()));
        hzlv_datas_31.create("FAMILY AND ATTRACTION", Dummy.dataFamilys(getContext()));
        hzlv_datas_32.create("FREE EVENTS", Dummy.dataFree(getContext()));
        searchComponent("");
    }
}
