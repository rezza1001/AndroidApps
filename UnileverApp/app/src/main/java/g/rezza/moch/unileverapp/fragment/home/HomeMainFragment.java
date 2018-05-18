package g.rezza.moch.unileverapp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import g.rezza.moch.unileverapp.ProductActivity;
import g.rezza.moch.unileverapp.R;
import g.rezza.moch.unileverapp.adapter.MenuGridAdapter;
import g.rezza.moch.unileverapp.connection.PostManager;
import g.rezza.moch.unileverapp.database.BrandDB;
import g.rezza.moch.unileverapp.database.ChartDB;
import g.rezza.moch.unileverapp.database.ProductDB;
import g.rezza.moch.unileverapp.fragment.HomeFragment;
import g.rezza.moch.unileverapp.fragment.ProductOrderFragment;
import g.rezza.moch.unileverapp.fragment.SalesOrderFragment;
import g.rezza.moch.unileverapp.holder.KeyValueHolder;
import g.rezza.moch.unileverapp.holder.MenuGridHolder;
import g.rezza.moch.unileverapp.holder.ProductHolder;
import g.rezza.moch.unileverapp.lib.ErrorCode;
import g.rezza.moch.unileverapp.lib.ScrollableGridView;

/**
 * Created by rezza on 09/02/18.
 */

public class HomeMainFragment extends Fragment {
    private static final String TAG = "HomeMainFragment";

    private ScrollableGridView grvw_data_10;
    private ScrollableGridView grvw_special_10;
    private MenuGridAdapter adapter;
    private MenuGridAdapter adapter_special;
    private ArrayList<MenuGridHolder> list = new ArrayList<>();
    private ArrayList<MenuGridHolder> list_special = new ArrayList<>();

    public static Fragment newInstance() {
        Fragment frag   = new HomeMainFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_home_main, container, false);
        grvw_data_10    = view.findViewById(R.id.grvw_data_10);
        grvw_special_10 = view.findViewById(R.id.grvw_special_10);
        adapter         = new MenuGridAdapter(getActivity(), list);
        adapter_special = new MenuGridAdapter(getActivity(), list_special);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grvw_data_10.setAdapter(adapter);
        grvw_special_10.setAdapter(adapter_special);
        createData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void createData(){
        BrandDB brandDB = new BrandDB();
        HashMap<String, String> mapBrands = new HashMap<>();
        ArrayList<BrandDB> brands =  brandDB.get(getActivity());
       for (BrandDB brand: brands){
           MenuGridHolder menu = new MenuGridHolder();
           menu.id      = brand.id;
           menu.image   = "http://202.154.3.188/commerce/production/uploads/"+brand.image;
           menu.name    = brand.name;
           list.add(menu);
       }
        adapter.notifyDataSetChanged();

        // SPECIAL

        {
            MenuGridHolder menu = new MenuGridHolder();
            menu.id = "dove";
            menu.image = "https://www.unilever.co.id/id/Images/dove_tcm1310-408752_w198.png";
            list_special.add(menu);
        }
        {
            MenuGridHolder menu = new MenuGridHolder();
            menu.id = "axe";
            menu.image = "https://www.unilever.co.id/id/Images/axe_tcm1310-408738_w198.jpg";
            list_special.add(menu);
        }
        {
            MenuGridHolder menu = new MenuGridHolder();
            menu.id = "citra";
            menu.image = "https://www.unilever.co.id/id/Images/citra-280x280_tcm1310-467847_w198.jpg";
            list_special.add(menu);
        }
        adapter_special.notifyDataSetChanged();
    }

    private void registerListener(){
        adapter.setOnSelectedItemListener(new MenuGridAdapter.OnSelectedItemListener() {
            @Override
            public void selectedItem(MenuGridHolder holder, int position) {
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                Fragment fragment = HomeFragment.newInstance(HomeFragment.PRODUCT);
//                fragmentTransaction.replace(HomeMainFragment.this.getId(), fragment,"sproduct_order");
//                fragmentTransaction.detach(fragment);
//                fragmentTransaction.attach(fragment);
//                fragmentTransaction.commit();

                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("BRAND_ID", holder.id);
                intent.putExtra("BRAND", holder.name);
                getActivity().startActivity(intent);

            }
        });
    }

}
