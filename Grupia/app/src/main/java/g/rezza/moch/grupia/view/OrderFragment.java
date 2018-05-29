package g.rezza.moch.grupia.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;
import g.rezza.moch.grupia.adapter.OrderAdapter;
import g.rezza.moch.grupia.listHolder.OrderLHolder;

/**
 * Created by rezza on 20/02/18.
 */

public class OrderFragment extends Fragment implements View.OnClickListener{

    private GridView grvw_datas_10;
    private OrderAdapter adapter;
    private ArrayList<OrderLHolder> mFilter = new ArrayList<>();
    private ArrayList<OrderLHolder> mholder = new ArrayList<>();

    public static Fragment newInstance(int color) {
        Fragment frag   = new OrderFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.view_order_fragment, container, false);
        grvw_datas_10   = view.findViewById(R.id.grvw_datas_10);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter         = new OrderAdapter(getActivity(), mFilter);
        grvw_datas_10.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
        filter("");
    }

    @Override
    public void onClick(View view) {

    }

    public void requestData(){
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "100000";
            order.title = "T-Shirt Polo";
            order.imageUrl = "http://fancy-tshirts.com/wp-content/uploads/2012/12/DJ-House-Party-Custom-T-shirt-Design.png";
            mholder.add(order);
        }
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "200000";
            order.title = "T-Shirt Armani";
            order.imageUrl = "http://www.retroshirtz.com/assets/img/photos/photo-t-shirts.png";
            mholder.add(order);
        }
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "180000";
            order.title = "T-Shirt Prada White";
            order.imageUrl = "https://cdn.shopify.com/s/files/1/2047/8323/products/white_prada_product_345x345@2x.png?v=1510559077";
            mholder.add(order);
        }
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "180000";
            order.title = "T-Shirt Prada Playstation";
            order.imageUrl = "https://superteeshops.com/wp-content/uploads/2017/12/Playstation-Prada-T-Shirt.png";
            mholder.add(order);
        }
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "170000";
            order.title = "T-Shirt D&G";
            order.imageUrl = "http://www.t-shirt-trends.com/products/3/31775862.png";
            mholder.add(order);
        }
        {
            OrderLHolder order = new OrderLHolder();
            order.id = "1";
            order.price = "173000";
            order.title = "T-Shirt Chanel White";
            order.imageUrl = "https://cdn.shopify.com/s/files/1/2389/7311/products/chanel_white_tee_shirt_480x480.png?v=1509339192";
            mholder.add(order);
        }

    }

    private void filter(String value){
        mFilter.clear();
        if (value.isEmpty()){
            for (OrderLHolder order: mholder){
                mFilter.add(order);
            }
        }
        else {
            for (OrderLHolder order: mholder){
                if (value.toUpperCase().equalsIgnoreCase(order.title.toUpperCase())){
                    mFilter.add(order);
                }

            }

        }
        adapter.notifyDataSetChanged();
    }
}
