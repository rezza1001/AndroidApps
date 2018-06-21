package g.rezza.moch.mobilesales.view.Fragment.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Activity.Product.EventDetailActivity;
import g.rezza.moch.mobilesales.Connection.postmanager.PostManager;
import g.rezza.moch.mobilesales.DataStatic.App;
import g.rezza.moch.mobilesales.DataStatic.ErrorCode;
import g.rezza.moch.mobilesales.Database.BookingDB;
import g.rezza.moch.mobilesales.R;
import g.rezza.moch.mobilesales.adapter.EventsAdapter;
import g.rezza.moch.mobilesales.holder.EventListHolder;
import g.rezza.moch.mobilesales.holder.KeyValueHolder;
import g.rezza.moch.mobilesales.lib.pagger.ScrollTabHolderFragment;

public class EventsListFragment extends ScrollTabHolderFragment{

	private static final String ARG_POSITION = "position";


	private int mPosition;
	private ListView 			lsvw_tickets_00;
	private ArrayList<EventListHolder> 	mListItems;
	private EventsAdapter		mAdapter;

	public static Fragment newInstance(int position) {
		EventsListFragment f = new EventsListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);
		mListItems = new ArrayList<EventListHolder>();
		loadData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.view_events_frg, null);
		lsvw_tickets_00 = (ListView) v.findViewById(R.id.lsvw_tickets_00);
		mAdapter		= new EventsAdapter(getActivity(), mListItems);

		View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, lsvw_tickets_00, false);
		placeHolderView.setBackgroundColor(0xFFFFFFFF);
		lsvw_tickets_00.addHeaderView(placeHolderView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lsvw_tickets_00.setOnScrollListener(new OnScroll());
		lsvw_tickets_00.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mAdapter.setOnSelectedItemListener(new EventsAdapter.OnSelectedItemListener() {
			@Override
			public void selectedItem(EventListHolder holder, int position) {
				Intent intent = new Intent(getActivity(), EventDetailActivity.class);
				BookingDB bookingDB = new BookingDB();
				bookingDB.event_image 	= holder.imageUrl;
				bookingDB.event_id   	= holder.id;
				bookingDB.event_name    = holder.name;
				bookingDB.insert(getActivity());
				startActivity(intent);
			}
		});
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && lsvw_tickets_00.getFirstVisiblePosition() >= 1) {
			return;
		}

		lsvw_tickets_00.setSelectionFromTop(1, scrollHeight);

	}
	
	public class OnScroll implements OnScrollListener{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (mScrollTabHolder != null)
				mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
		}
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
	}

	private void loadData(){
		PostManager post = new PostManager(getActivity());
		post.setApiUrl("list-product");
		ArrayList<KeyValueHolder> kvs = new ArrayList<>();
		kvs.add(new KeyValueHolder("category_id", App.KIOSTIX_EVENTS));
		post.setData(kvs);
		post.execute("POST");
		post.setOnReceiveListener(new PostManager.onReceiveListener() {
			@Override
			public void onReceive(JSONObject obj, int code) {
				if (code == ErrorCode.OK){
					try {
						JSONArray data = obj.getJSONArray("DATA");
						for (int i=0; i<data.length(); i++){
							JSONObject product = data.getJSONObject(i).getJSONObject("product_temp");
							EventListHolder event = new EventListHolder();
							event.id      = product.getString("code");
							event.name    = product.getString("name");
							event.genreID = product.getString("category");
							event.genreDesc = product.getString("category");
							event.imageUrl = product.getString("img_url").replaceAll("\\\\", "");
							mListItems.add(event);
						}

						mAdapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});


	}

}