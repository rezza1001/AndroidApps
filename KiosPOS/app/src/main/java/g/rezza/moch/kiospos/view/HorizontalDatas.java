package g.rezza.moch.kiospos.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.ArrayList;

import g.rezza.moch.kiospos.Activitty.DetailActivity;
import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.adapter.ItemDataAdapter;
import g.rezza.moch.kiospos.component.HorizontalListView;
import g.rezza.moch.kiospos.holder.EventHolder;
import g.rezza.moch.kiospos.lib.BackgroundTask;
import g.rezza.moch.kiospos.lib.MasterView;

/**
 * Created by Rezza on 11/9/17.
 */

public class HorizontalDatas extends MasterView {
    private static final String TAG = "HorizontalDatas";

    private TextView            txvw_seeall_11;
    private TextView            txvw_title_10;
    private HorizontalListView  hzlv_datas_10;
    private ItemDataAdapter     mItemAdapter;
    private ArrayList<EventHolder>
                                mListData;
    private ArrayList<EventHolder>
                                mListDataFilter;

    public HorizontalDatas(Context context) {
        super(context);
    }

    public HorizontalDatas(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_horizontal_datas, this, true);

        mListData       = new ArrayList<>();
        mListDataFilter = new ArrayList<>();
        txvw_title_10   = (TextView)                findViewById(R.id.txvw_title_10);
        txvw_seeall_11  = (TextView)                findViewById(R.id.txvw_seeall_11);
        hzlv_datas_10   = (HorizontalListView)      findViewById(R.id.hzlv_data_10);
        mItemAdapter    = new ItemDataAdapter(getContext(),mListDataFilter);
        hzlv_datas_10.setAdapter(mItemAdapter);
    }

    public void create(String title, ArrayList<EventHolder> pList){
        mListData.clear();
        txvw_title_10.setText(title);
        if (pList.size() > 0){
           for (EventHolder data: pList){
               mListData.add(data);
           }
        }

        hzlv_datas_10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventHolder event = (EventHolder) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), DetailActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void filter(String pFilter){
        new BackgroundTask(getContext(), true){

            @Override
            protected void onPreExecute() {
                mListDataFilter.clear();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object p_params) {
                String filter = (String) p_params;
                if (filter.isEmpty()){
                    for (EventHolder data: mListData){
                        onProgressUpdate(data);
                    }
                }
                else {
                    for (EventHolder data: mListData){
                        if (data.name.toLowerCase().contains(filter.toLowerCase())){
                            onProgressUpdate(data);
                        }
                    }
                }
                return super.doInBackground(p_params);
            }

            @Override
            protected void onProgressUpdate(Object p_value) {
                EventHolder data = (EventHolder) p_value;
                mListDataFilter.add(data);
                super.onProgressUpdate(p_value);
            }

            @Override
            protected void onPostExecute(Object p_result) {
                super.onPostExecute(p_result);
                mItemAdapter.notifyDataSetChanged();
                if (mFinishListener != null){
                    mFinishListener.onFinish();
                }
            }
        }.execute(pFilter);
    }

    public int getCountDataShow(){
       return mListDataFilter.size();
    }

    public OnFInishLoadData mFinishListener;
    public void setOnFinishListener(OnFInishLoadData pFinishListener){
        mFinishListener = pFinishListener;
    }
    public interface OnFInishLoadData{
        public void onFinish();
    }
}
