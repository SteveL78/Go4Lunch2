package fr.steve.leroy.go4lunch.ui.restaurant_details;

import android.annotation.SuppressLint;
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
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.model.PlaceDetails;

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
    private List<String> userLike;
    private PlaceDetails mPlaceDetails;
    private Context context;
    public static int REQUEST_CALL = 100;
    private static final String RESTAURANT_PLACE_ID = "placeId";
    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;

    private boolean isFavorite = false;
    //private boolean isBooked = false;


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
        this.binding.floatingActionButton.setOnClickListener( view -> updateFabDisplay() );
        //this.binding.floatingActionButton.setOnClickListener( view -> bookThisRestaurant( mPlaceDetails ) );
    }

    private void updateFabDisplay() {

        // TODO : si oui réservé alors changer la couleur du FAB + ajouter les infos dans firebase (id, placeId, workmateId) + MAJ du RV avec le workmate


        //TODO : si non retirer ce restaurant de la liste des favoris + remettre bouton dans son état d'origine

    }


    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    private void bookThisRestaurant(PlaceDetails placeDetails) {
        String userId = Objects.requireNonNull( getCurrentUser() ).getUid();
        String restaurantId = placeDetails.placeId;
        String restaurantName = placeDetails.name;
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
        if (mPlaceDetails.photos != null && mPlaceDetails.photos.length > 0) {
            String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + mPlaceDetails.photos[0].photoReference
                    + "&key="
                    + this.getString( R.string.google_maps_API_key );

            Glide.with( this )
                    .load( imageurl )
                    .centerCrop()
                    .into( binding.activityDetailRestaurantImg );

        } else {
            Glide.with( binding.activityDetailRestaurantImg )
                    .load( R.drawable.image_not_avalaible )
                    .apply( new RequestOptions()

                            .format( DecodeFormat.PREFER_ARGB_8888 )
                            .override( Target.SIZE_ORIGINAL ) )
                    .into( binding.activityDetailRestaurantImg );
        }


        /*
        String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + mPlaceDetails.photos[0].photoReference
                + "&key="
                + this.getString( R.string.google_maps_API_key );

        Glide.with( this )
                .load( imageurl )
                .centerCrop()
                .into( binding.activityDetailRestaurantImg );

         */


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
        binding.callBtn.setOnClickListener( v -> openDialer( mPlaceDetails.formattedPhoneNumber ) );
        binding.websiteBtn.setOnClickListener( v -> openWebsite( mPlaceDetails.website.toString() ) );
        binding.likeBtn.setOnClickListener( view -> {
            updateLikeButtonDisplay();
        } );
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateLikeButtonDisplay() {
        if (binding.likeBtn.getText().equals( getResources().getString( R.string.detail_restaurant_like_btn ) )) {
            binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds( null, getResources().getDrawable( R.drawable.ic_baseline_star_orange_24 ), null, null );

            binding.likeBtn.setText( R.string.detail_restaurant_unlike_btn );
        } else {
            binding.likeBtn.setText( R.string.detail_restaurant_like_btn );
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