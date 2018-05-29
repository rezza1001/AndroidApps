package g.rezza.moch.sampledirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParameterActivity extends AppCompatActivity {
    private static final String TAG = "ParameterActivity";

    private LocationManager mLocationManager;
    private TextView txvw_current_00;
    private Button bbtn_route_00;
    private DestAdapter adapter;
    private ListView    lsvw_dest_00;
    private ArrayList<DirectionList> mList = new ArrayList<>();

    private LatLng latFrom;
    private LatLng latTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(12.0f);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        lsvw_dest_00     = (ListView) findViewById(R.id.lsvw_dest_00);
        adapter          = new DestAdapter(this, mList);
        lsvw_dest_00.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                50, mLocationListener);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                Log.i(TAG, "Place Selected: " + place.getName());
                latTo = place.getLatLng();
                Log.i(TAG, "TEST " +formatPlaceDetails(getResources(), place.getName(), place.getId(),
                        place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "onError: Status = " + status.toString());
            }
        });

        txvw_current_00 = (TextView) findViewById(R.id.txvw_current_00);
        bbtn_route_00   = (Button)   findViewById(R.id.bbtn_route_00);
        bbtn_route_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction(latFrom, latTo);
            }
        });

        adapter.setOnSelectedListener(new DestAdapter.OnSelectedListener() {
            @Override
            public void onSelected(DirectionList holder, int position) {
                Log.d(TAG, "Selected "+ holder.route);
                Intent intent = new Intent();

                intent.putExtra("ROUTE", new Gson().toJson(holder));
                setResult(0,intent);
                ParameterActivity.this.finish();
            }
        });



        PlaceAutocompleteFragment autocompleteFragment_from = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment_from);
        ((EditText)autocompleteFragment_from.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(12.0f);

        autocompleteFragment_from.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                latFrom = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "onError: Status = " + status.toString());
            }
        });
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Geocoder geocoder =  new Geocoder(ParameterActivity.this, Locale.getDefault());
            Address address = null;
            try {
                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1).get(0);
                String street = address.getThoroughfare();
                txvw_current_00.setText(street);
                latFrom = new LatLng(address.getLatitude(),  address.getLongitude());;
                Log.d(TAG,"Current : "+ address.getLongitude()+" | " + address.getLatitude());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    private void direction(final LatLng origin, final LatLng destination){
        Log.d(TAG, "From : "+ origin);
        Log.d(TAG, "To : "+ destination);
        mList.clear();
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
                            Log.d(TAG, "direction.getRouteList().size() "+ direction.getRouteList().size());
                            for (int i = 0; i < direction.getRouteList().size(); i++) {
                                Route route = direction.getRouteList().get(i);
                                Leg leg = route.getLegList().get(0);
                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                String distance = distanceInfo.getText();
                                String duration = durationInfo.getText();

                                List<Step> steps = leg.getStepList();
                                String via = getViaStreet(steps.get(2).getStartLocation().getLatitude(),steps.get(2).getStartLocation().getLongitude());
                                DirectionList direct = new DirectionList();
                                direct.dest = distance;
                                direct.durasi = duration;
                                direct.street = via;
                                direct.latLng_from = new LatLng(leg.getStartLocation().getLatitude(),leg.getStartLocation().getLongitude());
                                direct.latLng_to =  new LatLng(leg.getEndLocation().getLatitude(),leg.getEndLocation().getLongitude());
                                direct.route = route;
                                mList.add(direct);
                                Log.d(TAG,"Rute Ke-"+(i+1)+ " "+ via+" distance :"+distance+" & Estimasi :"+
                                        duration);

                            }
                            adapter.notifyDataSetChanged();
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

    private String getViaStreet(double latitude, double longitude){
        Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
        String street = "";
        try {
            Address address =geocoder.getFromLocation(latitude, longitude,1).get(0);
             street = address.getThoroughfare();
            Log.d(TAG,"VIA : "+ street );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return street;
    }
}
