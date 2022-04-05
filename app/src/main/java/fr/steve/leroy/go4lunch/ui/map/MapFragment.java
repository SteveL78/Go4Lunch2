package fr.steve.leroy.go4lunch.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.model.PlacesSearchResult;

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

    private FragmentMapBinding binding;

    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate( inflater, container, false );
        return binding.getRoot();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        isGooglePlayServicesAvailable();
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

        getLocationPermission();

        mainExecutor = ContextCompat.getMainExecutor( requireContext() );

    }


    public boolean isGooglePlayServicesAvailable() {
        Log.d( TAG, "isGooglePlayServicesAvailable: checking google services version" );

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable( getActivity() );

        if (available == ConnectionResult.SUCCESS) {
            // Everything is fine and the user can make map request
            Log.d( TAG, "isGooglePlayServicesAvailable: Google Play Services is working" );
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError( available )) {
            // An error occured but we can resolve it
            Log.d( TAG, "isGooglePlayServicesAvailable: an error occured but we can fix it" );
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog( getActivity(), available, ERROR_DIALOG_REQUEST );
            dialog.show();
        } else {
            Toast.makeText( getActivity(), "You can't make map requests", Toast.LENGTH_SHORT ).show();
        }
        return false;
    }


    private void getDeviceLocation() {
        Log.d( TAG, "getDeviceLocation: getting the devices current location" );

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient( getActivity() );

        try {
            if (mLocationPermissionsGranted) {

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener( getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    moveCamera( new LatLng( location.getLatitude(), location.getLongitude() ),
                                            "My location" );

                                    initLocation( location );
                                }
                            }
                        } );
            }
        } catch (SecurityException e) {
            Log.e( TAG, "getDeviceLocation : SecurityException: " + e.getMessage() );
        }
    }


    private void moveCamera(LatLng latLng, String title) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng, MapFragment.DEFAULT_ZOOM ) );

        if (!title.equals( "My location" )) {
            MarkerOptions options = new MarkerOptions()
                    .position( latLng )
                    .title( title );

            mMap.addMarker( options );
        }
        hideSoftKeyboard();
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable( getResources(), id, null );
        Bitmap bitmap = Bitmap.createBitmap( vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas( bitmap );
        vectorDrawable.setBounds( 0, 0, canvas.getWidth(), canvas.getHeight() );
        DrawableCompat.setTint( vectorDrawable, color );
        vectorDrawable.draw( canvas );
        return BitmapDescriptorFactory.fromBitmap( bitmap );
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText( getActivity(), "Map is ready", Toast.LENGTH_SHORT ).show();
        Log.d( TAG, "onMapReady: map is ready" );
        mMap = googleMap;

        googleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
        }

        binding.fragmentMapFloatingActionBtn.setOnClickListener( view -> getDeviceLocation() );

    }


    private void initLocation(Location currentLocation) {

        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled( true );
        moveCamera( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ), "The boss is here" );
        mMap.getUiSettings().setMyLocationButtonEnabled( false );

        executor.execute( (() -> {
            com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() );
            PlacesSearchResult[] placesSearchResults = new NearbySearch().run( latLng ).results;
            mainExecutor.execute( (() -> {

                for (int i = 0; i < placesSearchResults.length; i++) {
                    double lat = placesSearchResults[i].geometry.location.lat;
                    double lng = placesSearchResults[i].geometry.location.lng;

                    mMap.addMarker( new MarkerOptions().position( new LatLng( lat, lng ) )
                            .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_booked_restaurant_green ) ) );
                }
            }) );
        }) );
    }


    private void getLocationPermission() {
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