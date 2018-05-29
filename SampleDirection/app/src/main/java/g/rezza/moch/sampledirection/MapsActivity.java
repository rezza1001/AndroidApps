package g.rezza.moch.sampledirection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.model.Waypoint;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private String[] colors = {"#3f27f7", "#4cff5b", "#02566d"};
    private ImageView imvw_direction_00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imvw_direction_00 = (ImageView) findViewById(R.id.imvw_direction_00);
        imvw_direction_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, ParameterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String jsonMyObject = null;
        Bundle extras = data.getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("ROUTE");
        }
        DirectionList myObject = new Gson().fromJson(jsonMyObject, DirectionList.class);
        Log.d(TAG,"DATA "+ myObject.street);
        direcTo(myObject.route, myObject.latLng_from, myObject.latLng_to);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPemission();
//        direction();
    }

    private void requestPemission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            updateLocationUI();
        }

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {
                    updateLocationUI();

                }
            }
        }
        return true;
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "ENABLE MY LOCATION ");
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void direction(){
        final LatLng origin = new LatLng(-6.275729, 106.821024);
        final LatLng destination = new LatLng(-6.253757, 106.831069);
        GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        Log.d(TAG, "direction "+ status);
                        if(status.equals(RequestResult.OK)) {
                            mMap.addMarker(new MarkerOptions().position(origin));
                            mMap.addMarker(new MarkerOptions().position(destination));
                            Log.d(TAG, "direction.getRouteList().size() "+ direction.getRouteList().size());
                            for (int i = 0; i < direction.getRouteList().size(); i++) {
                                Route route = direction.getRouteList().get(i);
                                Leg leg = route.getLegList().get(0);
                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                String distance = distanceInfo.getText();
                                String duration = durationInfo.getText();

                                List<Step> steps = leg.getStepList();
                                getViaStreet(steps.get(2).getStartLocation().getLatitude(),steps.get(2).getStartLocation().getLongitude());

                                Log.d(TAG,"Rute Ke-"+i+ " distance :"+distance+" & Estimasi :"+
                                        duration);

                                String color = colors[i % colors.length];
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this,
                                        directionPositionList, 5, Color.parseColor(color)));
                            }
                            setCameraWithCoordinationBounds(direction.getRouteList().get(0));
                        } else if(status.equals(RequestResult.NOT_FOUND)) {
                            Log.e(TAG, "direction NOT_FOUND");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e(TAG, "direction NOT_FOUND");
                    }
                });
    }

    private void direcTo(Route route, LatLng from, LatLng to){
        mMap.clear();
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this,
                directionPositionList, 4, Color.parseColor("#100e3d")));
        mMap.addMarker(new MarkerOptions().position(from));
        mMap.addMarker(new MarkerOptions().position(to));
        setCameraWithCoordinationBounds(route);
    }


    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private void getViaStreet(double latitude, double longitude){
        Geocoder geocoder =  new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            Address address =geocoder.getFromLocation(latitude, longitude,1).get(0);
            String street = address.getThoroughfare();
            Log.d(TAG,"VIA : "+ street );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
