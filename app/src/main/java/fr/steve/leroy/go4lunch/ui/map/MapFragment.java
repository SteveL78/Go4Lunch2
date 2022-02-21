package fr.steve.leroy.go4lunch.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.NearbySearch;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentMapBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FragmentMapBinding binding;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;
    //widgets
    private EditText mSearchText;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        isServicesOK();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        Log.d( TAG, "initMap: initializing map" );
        // Build the map, obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById( R.id.map );
        assert mapFragment != null;
        mapFragment.getMapAsync( this );

        mSearchText = binding.inputSearch;

        getLocationPermission();

        mainExecutor = ContextCompat.getMainExecutor( requireContext() );

        init();

    }


    private void init() {
        Log.d( TAG, "init: initializing" );

        mSearchText.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {

                    // execute our method for searching
                    geoLocate();
                }
                return false;
            }
        } );
        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d( TAG, "geolocate: geolocating" );

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder( getActivity() );
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName( searchString, 1 );
        } catch (IOException e) {
            Log.e( TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if (list.size() > 0) {
            Address address = list.get( 0 );

            Log.d( TAG, "geolocate: found a location: " + address.toString() );
            //  Toast.makeText( getActivity(), address.toString(), Toast.LENGTH_SHORT ).show();

            moveCamera( new LatLng( address.getLatitude(), address.getLongitude() ), DEFAULT_ZOOM,
                    address.getAddressLine( 0 ) );
        }

    }


    public boolean isServicesOK() {
        Log.d( TAG, "isServicesOK: checking google services version" );

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable( getActivity() );

        if (available == ConnectionResult.SUCCESS) {
            // Everything is fine and the user can make map request
            Log.d( TAG, "isServicesOK: Google Play Services is working" );
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError( available )) {
            // An error occured but we can resolve it
            Log.d( TAG, "isServicesOK: an error occured but we can fix it" );
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog( getActivity(), available, ERROR_DIALOG_REQUEST );
            dialog.show();
        } else {
            Toast.makeText( getActivity(), "You can't make map requests", Toast.LENGTH_SHORT ).show();
        }
        return false;
    }


    private void getDeviceLocation() {
        Log.d( TAG, "getDevicelocation: getting the devices current location" );

        fusedLocationClient = LocationServices.getFusedLocationProviderClient( getActivity() );

        try {
            if (mLocationPermissionsGranted) {

                Task location = fusedLocationClient.getLastLocation();
                location.addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        Log.d( TAG, "onComplete: found location" );
                        Location currentLocation = (Location) task.getResult();

                        if (currentLocation != null) {
                            moveCamera( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ),
                                    DEFAULT_ZOOM,
                                    "My location" );

                            initLocation( currentLocation );

                        }
                    } else {
                        Log.d( TAG, "onComplete: current location is null" );
                        Toast.makeText( getActivity(), "unable to get current location", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } catch (SecurityException e) {
            Log.e( TAG, "getDeviceLocation : SecurityException: " + e.getMessage() );
        }

    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng, zoom ) );

        if (!title.equals( "My location" )) {
            MarkerOptions options = new MarkerOptions()
                    .position( latLng )
                    .title( title );
            mMap.addMarker( options );
        }
        hideSoftKeyboard();

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText( getActivity(), "Map is ready", Toast.LENGTH_SHORT ).show();
        Log.d( TAG, "onMapReady: map is ready" );
        mMap = googleMap;

        googleMap.addMarker( new MarkerOptions()
                .position( new LatLng( 48.8566, 2.3522 ) )
                .title( "You are here" ) );

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
        }

        binding.fragmentMapFloatingActionBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        } );

    }


    private void initLocation(Location currentLocation) {

        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled( false );

        init();

        executor.execute( (() -> {
            com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() );
            PlacesSearchResult[] placesSearchResults = new NearbySearch().run( latLng ).results;
            mainExecutor.execute( (() -> {

                for (int i = 0; i < placesSearchResults.length; i++) {
                    double lat = placesSearchResults[i].geometry.location.lat;
                    double lng = placesSearchResults[i].geometry.location.lng;

                    mMap.addMarker( new MarkerOptions().position( new LatLng( lat, lng ) ) );

                }
            }) );
        }) );
    }

    private boolean getLocationPermission() {
        Log.d( TAG, "getLocationPermission: getting location permissions" );
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission( getActivity().getApplicationContext(),
                FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission( getActivity().getApplicationContext(),
                    COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                requestPermissions(
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE );
            }
        } else {
            requestPermissions(
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE );
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d( TAG, "onRequestPermissionsResult: called" );
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d( TAG, "onRequestPermissionsResult: permission failed" );
                            return;
                        }
                    }
                    Log.d( TAG, "onRequestPermissionsResult: permission granted" );
                    mLocationPermissionsGranted = true;
                    //Initialize our map
                    getDeviceLocation();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }

}