package fr.steve.leroy.go4lunch.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.FetchDetail;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;
import fr.steve.leroy.go4lunch.model.Workmate;
import io.reactivex.rxjava3.disposables.Disposable;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailAdapter adapter;

    private RecyclerView recyclerView;
    private RestaurantDetailViewModel viewModel;
    private Disposable disposable;
    private List<Workmate> mWorkmateList;
    private PlacesSearchResult placesSearchResults;
    private Context context;
    public static int REQUEST_CALL = 100;
    private static final String RESTAURANT_PLACE_ID = "placeId";
    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;
    private PlaceDetails mPlaceDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initView();

        getRestaurantPlaceId();

        this.mainExecutor = ContextCompat.getMainExecutor( this );

        configureRecycleView();

        setFabListener();


    }

    private void setFabListener() {
        this.binding.floatingActionButton.setOnClickListener( view -> bookThisRestaurant( placesSearchResults ) );
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    private void bookThisRestaurant(PlacesSearchResult placesSearchResult) {
        String userId = Objects.requireNonNull( getCurrentUser() ).getUid();
        String restaurantId = placesSearchResults.placeId;
        String restaurantName = placesSearchResult.name;
        checkBooked( userId, restaurantId, restaurantName, true );
    }

    private void checkBooked(String userId, String restaurantId, String restaurantName, boolean b) {
      /*
        RestaurantsHelper.getBooking(userId, getTodayDate()).addOnCompleteListener(restaurantTask -> {

            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().size() == 1) {
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        if (Objects.equals(restaurant.getData().get("restaurantName"), restaurantName)) {
                            displayFloating((R.drawable.ic_clear_black_24dp), getResources().getColor(R.color.colorError));
                            if (tryingToBook) {
                                Booking_Firebase(userId, restaurantId, restaurantName, restaurant.getId(), false, false, true);
                                showToast(this, getResources().getString(R.string.cancel_booking), Toast.LENGTH_SHORT);
                            }
                        } else {
                            displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                            if (tryingToBook) {
                                Booking_Firebase(userId, restaurantId, restaurantName, restaurant.getId(), false, true, false);
                                showToast(this, getResources().getString(R.string.modify_booking), Toast.LENGTH_SHORT);
                            }
                        }
                    }
                } else {
                    displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                    if (tryingToBook) {
                        Booking_Firebase(userId, restaurantId, restaurantName, null, true, false, false);
                        showToast(this, getResources().getString(R.string.new_booking), Toast.LENGTH_SHORT);
                    }
                }
            }
        });*/
    }

    private void configureRecycleView() {
        mWorkmateList = new ArrayList<>();
        this.adapter = new RestaurantDetailAdapter( mWorkmateList );
        this.binding.activityDetailRestaurantRv.setAdapter( adapter );
        this.binding.activityDetailRestaurantRv.setLayoutManager( new LinearLayoutManager( this ) );
    }


    private void getRestaurantPlaceId() {
        if (getIntent().hasExtra( RESTAURANT_PLACE_ID )) {
            String placeId = getIntent().getStringExtra( RESTAURANT_PLACE_ID );
            getRestaurantDetail( placeId );

        }
    }

    private void getRestaurantDetail(String placeId) {
        executor.execute( (() -> {
            mPlaceDetails = new FetchDetail().run( placeId );

            mainExecutor.execute( (() -> {
                initListeners();
                displayInfoRestaurant();
            }) );
        }) );
    }

    private void displayInfoRestaurant() {

        //Restaurant image
        String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + mPlaceDetails.photos[0].photoReference
                + "&key="
                + this.getString( R.string.google_maps_API_key );

        Glide.with( this )
                .load( imageurl )
                .centerCrop()
                .into( binding.activityDetailRestaurantImg );


        /*
        if (mPlaceDetails.photos != null && mPlaceDetails.photos.length > 0) {

            Glide.with( this )
                    .load( mPlaceDetails.photos )
                    .centerCrop()
                    .into( activityBinding.activityDetailRestaurantImg );
        } else {
            activityBinding.activityDetailRestaurantImg.setImageResource( R.drawable.image_not_avalaible );
        }*/

        //Restaurant name
        binding.activityDetailRestaurantName.setText( mPlaceDetails.name );

        //Rating
        float rating = (mPlaceDetails.rating / 5) * 3;
        binding.itemRestaurantListRatingbar.setRating( rating );

        //Restaurant address
        binding.activityDetailRestaurantAddress.setText( mPlaceDetails.formattedAddress );

    }


    // ------------------------------------
    // INIT VIEW
    // ------------------------------------

    private void initView() {
        binding = ActivityRestaurantDetailBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }


    // ------------------------------------
    // INIT LISTENERS
    // ------------------------------------

    private void initListeners() {
        binding.activityDetailRestaurantCallBtn.setOnClickListener( v -> openDialer( mPlaceDetails.formattedPhoneNumber ) );
        binding.activityDetailRestaurantWebsiteBtn.setOnClickListener( v -> openWebsite( mPlaceDetails.website.toString() ) );

    }


    // ------------------------------------------------------------------------
    // BUTTONS
    // ------------------------------------------------------------------------

    private void openDialer(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.trim().length() > 0) {

            Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:" + phoneNumber ) );// Initiates the Intent
            startActivity( intent );

        } else {
            Toast.makeText( RestaurantDetailActivity.this, "No phone", Toast.LENGTH_SHORT ).show();
        }
    }


    private void openWebsite(String website) {
        if (website != null) {

            Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( website ) );
            startActivity( browserIntent );

        } else {
            Toast.makeText( RestaurantDetailActivity.this, "No website", Toast.LENGTH_SHORT ).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }


    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }


}




