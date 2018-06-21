package g.rezza.moch.mobilesales.view.Fragment.Store;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import g.rezza.moch.mobilesales.Database.ListStoreDB;
import g.rezza.moch.mobilesales.R;

public class StoreMapActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapvw_00;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.view_store_map, container, false);
        registerView(V, savedInstanceState);
        return V;
    }

    private void registerView(View view, Bundle savedInstanceState){
        mapvw_00 = (MapView) view.findViewById(R.id.mapvw_00);
        mapvw_00.onCreate(savedInstanceState);

    // Gets to GoogleMap from the MapView and does initialization stuff
        mapvw_00.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;

        ListStoreDB store = new ListStoreDB();
        ArrayList<ListStoreDB> stores = store.getData(getActivity());

        for (ListStoreDB data: stores){
            // Add a marker in Sydney and move the camera
            if (data.longitude.equalsIgnoreCase("0")){
                return;
            }
            LatLng initialLoc= googleMap.getCameraPosition().target;
            LatLng marker = new LatLng(Float.parseFloat(data.latitude), Float.parseFloat(data.longitude));
            googleMap.addMarker(new MarkerOptions().position(marker).title(data.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.ic_marekt))));

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 14.0f));
        }


    }

    @Override
    public void onResume() {
        mapvw_00.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapvw_00.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapvw_00.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapvw_00.onLowMemory();
    }

    private Bitmap resizeMarker(int resource){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(resource);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }
}
