package fr.steve.leroy.go4lunch;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import fr.steve.leroy.go4lunch.databinding.FragmentMapBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {




    FragmentMapBinding binding;

    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;


    GoogleApiClient mGoogleApiClient;
    double currentLatitude, currentLongitude;
    Location myLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        isServicesOK();

        getLocationPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate( inflater, container, false );
        return binding.getRoot();

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


    private void initMap() {
        Log.d( TAG, "initMap: initializing map" );
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( (OnMapReadyCallback) getActivity() );
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText( getActivity(), "Map is ready", Toast.LENGTH_SHORT ).show();
        Log.d( TAG, "onMapReady: map is ready" );
        mMap = googleMap;
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
                ActivityCompat.requestPermissions( getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE );
            }
        }else {
            ActivityCompat.requestPermissions( getActivity(),
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
                    initMap();
                }
            }
        }
    }

}
