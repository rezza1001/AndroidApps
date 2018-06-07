package g.rezza.moch.kiostixv3.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import g.rezza.moch.kiostixv3.R;
import g.rezza.moch.kiostixv3.adapter.ListEventAdapter;
import g.rezza.moch.kiostixv3.holder.EventsList;
import g.rezza.moch.kiostixv3.lib.HorizontalListView;

/**
 * Created by rezza on 11/02/18.
 */

public class ListEvents extends RelativeLayout {

    private static final String TAG = "ListEvents";
    private HorizontalListView  hrzl_events_00;
    private ListEventAdapter    mAdapter;
    private ArrayList<EventsList>
                                mHolders = new ArrayList<>();
    private TextView            txvw_title_00;
    private TextView            txvw_see_00;
    private TextView            txvw_nodata_00;
    private String id = "";

    public ListEvents(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_list_events, this, true);

        txvw_title_00   = (TextView) findViewById(R.id.txvw_title_00);
        txvw_see_00     = (TextView) findViewById(R.id.txvw_see_00);
        txvw_nodata_00  = (TextView) findViewById(R.id.txvw_nodata_00);
        hrzl_events_00  = (HorizontalListView) findViewById(R.id.hrzl_events_00);
        mAdapter        = new ListEventAdapter(context,mHolders);
        hrzl_events_00.setAdapter(mAdapter);

        hrzl_events_00.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (pListener != null){
                    pListener.onSelected( mAdapter.getItem(i));
                }
            }
        });


        txvw_see_00.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pSeeAll != null){
                    pSeeAll.onSeeAll(id, txvw_title_00.getText().toString());
                }
            }
        });

        hrzl_events_00.setOnTouch(new HorizontalListView.OnMoveListener() {
            @Override
            public void onMove(int action) {
                if (pTouch != null){
                        pTouch.OnTouch();
                    }
                }
        });


    }

    public void setTiltle(String title, String id){
        this.id = id;
        txvw_title_00.setText(title);
    }

    public void clear(){
        mHolders.clear();
    }

    public void add(String id, String name, String url, String venue){
        mHolders.add(new EventsList(id,name,url, venue));
    }

    public void notif(){
        Log.d(TAG,"notifyDataSetChanged");
        mAdapter.notifyDataSetChanged();
        if (mHolders.size() == 0){
            hrzl_events_00.setVisibility(GONE);
            txvw_nodata_00.setVisibility(VISIBLE);
        }
        else {
            hrzl_events_00.setVisibility(VISIBLE);
            txvw_nodata_00.setVisibility(GONE);
        }
    }

    private OnSelectedListener pListener;
    public void setOnSelectedListener(OnSelectedListener mListener){
        pListener = mListener;
    }
    public interface OnSelectedListener{
        public void onSelected(EventsList pHolder);
    }


    private OnSeeAllListerner pSeeAll;
    public void setOnSeeAllListerner(OnSeeAllListerner mListener){
        pSeeAll = mListener;
    }
    public interface OnSeeAllListerner{
        public void onSeeAll(String id, String title);
    }


    private OnTouchScrollListener pTouch;
    public void setOnTouch(OnTouchScrollListener mTouch){
        pTouch = mTouch;
    }
    public interface OnTouchScrollListener{
        public void OnTouch();
    }

}
