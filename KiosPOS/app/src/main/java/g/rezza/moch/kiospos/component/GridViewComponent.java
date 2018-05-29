package g.rezza.moch.kiospos.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import g.rezza.moch.kiospos.R;
import g.rezza.moch.kiospos.adapter.ItemDataAdapter;
import g.rezza.moch.kiospos.holder.EventHolder;
import g.rezza.moch.kiospos.lib.BackgroundTask;
import g.rezza.moch.kiospos.lib.MasterComponent;

/**
 * Created by Rezza on 11/13/17.
 */

public class GridViewComponent extends MasterComponent {
    private static final String TAG = "GridViewComponent";

    private GridView        grvw_datas_10;
    private ItemDataAdapter mItemAdapter;
    private ArrayList<EventHolder>
                            mListData;
    private ArrayList<EventHolder>
                            mListDataFilter;

    public GridViewComponent(Context context) {
        super(context);
    }

    public GridViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_grid, this, true);

        mListData       = new ArrayList<>();
        mListDataFilter = new ArrayList<>();
        grvw_datas_10   = (GridView)    findViewById(R.id.grvw_datas_10);
        mItemAdapter    = new ItemDataAdapter(getContext(),mListDataFilter);
        grvw_datas_10.setAdapter(mItemAdapter);
    }

    public void create(ArrayList<EventHolder> pList){
        mListData.clear();
        if (pList.size() > 0){
            for (EventHolder data: pList){
                mListData.add(data);
            }
        }

        grvw_datas_10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventHolder event = (EventHolder) adapterView.getItemAtPosition(i);
                if (mItemCickListener != null){
                    mItemCickListener.onItemClick(event);
                }
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
            }
        }.execute(pFilter);
    }

    OnItemClickListener mItemCickListener;
    public void setOnItemClickListener(OnItemClickListener pItemCickListener){
        mItemCickListener = pItemCickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(EventHolder event);
    }
}
