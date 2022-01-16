package fr.steve.leroy.go4lunch.list;

import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.FragmentListViewBinding;
import fr.steve.leroy.go4lunch.detail.RestaurantDetailActivity;
import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#} factory method to
 * create an instance of this fragment.
 */

public class ListViewFragment extends Fragment implements RestaurantListAdapter.OnRestaurantClickListener {

    private FragmentListViewBinding binding;
    private static final String TAG = ListViewFragment.class.getSimpleName();
    private List<PlacesSearchResult> mPlacesSearchResults;
    private RestaurantListAdapter adapter;
    private RestaurantListViewModel viewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private List<Workmate> workmateList;
    private Workmate workmate;
    private RecyclerView recyclerView;

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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( getActivity() );

        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener( task -> {
                if (task.isSuccessful()) {
                    Log.d( TAG, "onComplete: found location" );
                    currentLocation = (Location) task.getResult();

                    if (currentLocation != null) {
                        viewModel.getAllRestaurants( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ) );
                    }

                } else {
                    Log.d( TAG, "onComplete: current location is null" );
                    Toast.makeText( getActivity(), "unable to get current location", Toast.LENGTH_SHORT ).show();
                }
            } );

        } catch (SecurityException e) {
            Log.e( TAG, "getDeviceLocation : SecurityException: " + e.getMessage() );
        }
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
        viewModel.getPlacesSearchResult().observe( getViewLifecycleOwner(), this::initRestaurantList );
    }


    private void configureRecycleView() {
        mPlacesSearchResults = new ArrayList<>();
        adapter = new RestaurantListAdapter( mPlacesSearchResults, this );
        binding.fragmentRestaurantRecyclerview.setLayoutManager( new LinearLayoutManager( getActivity() ) );
        binding.fragmentRestaurantRecyclerview.setAdapter( adapter );
    }


    private void initRestaurantList(Pair<List<Workmate>, List<PlacesSearchResult>> result) {
        this.workmateList = result.first;
        this.mPlacesSearchResults = result.second;
        adapter.updateWithData( this.workmateList, this.mPlacesSearchResults, this.currentLocation );
        binding.swipeRefreshRestaurantLayout.setRefreshing( false );
    }


    private void configureSwipeRefreshLayout() {
        binding.swipeRefreshRestaurantLayout.setOnRefreshListener( this::setUpRestaurantList );
    }


    @Override
    public void onRestaurantClick(PlacesSearchResult result) {
        Intent intent = new Intent( getContext(), RestaurantDetailActivity.class );
        intent.putExtra( "placeId", result.placeId );
        startActivity( intent );
    }
}