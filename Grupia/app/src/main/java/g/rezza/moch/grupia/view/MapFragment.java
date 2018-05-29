package g.rezza.moch.grupia.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import g.rezza.moch.grupia.R;

/**
 * Created by rezza on 20/02/18.
 */

public class MapFragment extends Fragment  implements OnMapReadyCallback {

    private MapView mapvw_00;

    private ArrayList<LatLng> DUMY = new ArrayList<>();

    public static Fragment newInstance(int color) {
        Fragment frag   = new MapFragment();
        Bundle args     = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.view_map_fragment, container, false);
        mapvw_00    = (MapView) view.findViewById(R.id.mapvw_00);
        mapvw_00.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapvw_00.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DUMY.add(new LatLng(-6.278358, 106.838393));
        DUMY.add(new LatLng(-6.267852, 106.778439));
        DUMY.add(new LatLng(-6.344584, 106.837223));
        DUMY.add(new LatLng(-6.217713, 106.841384));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng initialLoc= googleMap.getCameraPosition().target;
        for (LatLng latLng: DUMY){
            LatLng marker = latLng;
            googleMap.addMarker(new MarkerOptions().position(marker).title("HRV-Club")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.mipmap.ic_hrv))));

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 12.0f));
        }
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                View v = getLayoutInflater().inflate(R.layout.info_title, null);
//
//
//                final TextView info1 = (TextView) v.findViewById(R.id.info1);
//                TextView info2 = (TextView) v.findViewById(R.id.info2);
//                TextView info3 = (TextView) v.findViewById(R.id.info3);
//
//                info1.setText("Fecha: " );
//                info3.setText("Longitud: " );
//
//                info2.setText("Ubicacion: ");
//
//                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                    public void onInfoWindowClick(Marker marker)
//                    {
//                        Fragment fragmento;
//                        fragmento = new HistorialFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("title",info1.getText().toString());
//                        fragmento.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.content_principal, fragmento)
//                                .commit();
//                    }
//                });
//
//                return v;
//            }
//        });

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
        int height = 200;
        int width = 200;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(resource);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }
}
