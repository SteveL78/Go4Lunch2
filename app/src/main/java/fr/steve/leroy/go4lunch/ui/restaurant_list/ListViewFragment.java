package fr.steve.leroy.go4lunch.ui.restaurant_list;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentListViewBinding;
import fr.steve.leroy.go4lunch.model.Workmate;
import fr.steve.leroy.go4lunch.ui.restaurant_details.RestaurantDetailActivity;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#} factory method to
 * create an instance of this fragment.
 */

public class ListViewFragment extends Fragment implements RestaurantListRecyclerViewAdapter.OnRestaurantClickListener {

    private FragmentListViewBinding binding;
    private static final String TAG = ListViewFragment.class.getSimpleName();
    private final int REQUEST_FINE_LOCATION = 1234;

    private Disposable disposable;
    private List<PlacesSearchResult> placeSearchResult;
    private RestaurantListRecyclerViewAdapter adapter;
    private RestaurantListViewModel viewModel;
    private Location currentLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private Boolean mLocationPermissionsGranted = false;


    public ListViewFragment() { /* Required empty public constructor */ }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        viewModel = new ViewModelProvider( requireActivity() ).get( RestaurantListViewModel.class );
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_list_view, container, false );
        configureBinding( view );
        initViewModel();
        configureSwipeRefreshLayout();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        configureRecycleView();

        updateGPS();
    }

    private void updateGPS() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient( getActivity() );

        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLocationPermissionsGranted = true;
                            viewModel.getAllRestaurants( new com.google.maps.model.LatLng( location.getLatitude(), location.getLongitude() ) );

                        } else {
                            Log.d( TAG, "onComplete: current location is null" );
                            Toast.makeText( getActivity(), "unable to get current location", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }


    private void configureBinding(View view) {
        binding = FragmentListViewBinding.bind( view );
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider( this ).get( RestaurantListViewModel.class );
        viewModel.init( ContextCompat.getMainExecutor( requireContext() ) );
        setUpRestaurantList();
    }


    private void setUpRestaurantList() {
        viewModel.getPlacesSearchResults().observe( getViewLifecycleOwner(), this::initRestaurantList );
    }


    private void configureRecycleView() {
        this.placeSearchResult = new ArrayList<>();
        this.adapter = new RestaurantListRecyclerViewAdapter( this.placeSearchResult, this, currentLocation );
        this.binding.fragmentRestaurantRecyclerview.setAdapter( adapter );
        this.binding.fragmentRestaurantRecyclerview.setLayoutManager( new LinearLayoutManager( getActivity() ) );
    }


    private void initRestaurantList(Pair<List<Workmate>, List<PlacesSearchResult>> result) {
        List<Workmate> workmateList = result.first;
        this.placeSearchResult = result.second;
        this.adapter.updateWithData( workmateList, this.placeSearchResult, this.currentLocation );
        this.binding.swipeRefreshRestaurantLayout.setRefreshing( false );
    }


    private void configureSwipeRefreshLayout() {
        this.binding.swipeRefreshRestaurantLayout.setOnRefreshListener( this::setUpRestaurantList );
    }


    @Override
    public void onRestaurantClick(PlacesSearchResult result) {
        Intent intent = new Intent( getContext(), RestaurantDetailActivity.class );
        intent.putExtra( "placeId", result.placeId );
        startActivity( intent );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed())
            this.disposable.dispose();
    }

}