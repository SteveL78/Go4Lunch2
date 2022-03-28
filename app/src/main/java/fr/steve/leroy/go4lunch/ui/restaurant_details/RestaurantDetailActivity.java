package fr.steve.leroy.go4lunch.ui.restaurant_details;

import static fr.steve.leroy.go4lunch.utils.GetTodayDate.getTodayDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.PlaceDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.FetchDetail;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;
import fr.steve.leroy.go4lunch.firebase.BookingHelper;
import fr.steve.leroy.go4lunch.firebase.RestaurantHelper;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.repositories.UserRepository;
import fr.steve.leroy.go4lunch.utils.LikeButton;
import io.reactivex.rxjava3.disposables.Disposable;

public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private UserManager userManager = UserManager.getInstance();

    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private PlaceDetails mPlaceDetails;

    private Disposable disposable;

    private static final String RESTAURANT_PLACE_ID = "placeId";
    private Executor executor = Executors.newSingleThreadExecutor();
    private Executor mainExecutor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initView();
        configureButtonClickListener();
        configureRecycleView();
        getRestaurantPlaceId();
        setFabListener();

        this.mainExecutor = ContextCompat.getMainExecutor( this );
    }

    // ------------------------------------
    // INIT VIEW
    // ------------------------------------
    private void initView() {
        binding = ActivityRestaurantDetailBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }

    // ------------------------------------
    // CONFIGURE BUTTON CLICK LISTENER
    // ------------------------------------
    private void configureButtonClickListener() {
        binding.callBtn.setOnClickListener( this );
        binding.likeBtn.setOnClickListener( this );
        binding.websiteBtn.setOnClickListener( this );
    }

    /**
     * Launches an Intent.ACTION_DIAL intent after a click on the "Call" option button.
     * Updates "Like" button display.
     * Launches an Intent.ACTION_VIEW intent with a URI website after a click on the "WEBSITE" option button.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Call Button
        if (id == R.id.call_btn) {
            if (mPlaceDetails.formattedPhoneNumber != null && mPlaceDetails.formattedPhoneNumber.trim().length() > 0) {
                Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:" + mPlaceDetails.formattedPhoneNumber ) );// Initiates the Intent
                startActivity( intent );
            } else {
                Toast.makeText( RestaurantDetailActivity.this, "No phone number", Toast.LENGTH_SHORT ).show();
            }

            // Update "Like" button display
        } else if (id == R.id.like_btn) {
            if (binding.likeBtn.getText().equals( getResources().getString( R.string.detail_restaurant_like ) )) {
                binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds( null, getResources().getDrawable( R.drawable.ic_baseline_star_orange_24 ), null, null );
                LikeButton.likeRestaurant( mPlaceDetails, this, binding.likeBtn, getResources().getString( R.string.detail_restaurant_unlike ), getResources().getString( R.string.detail_restaurant_like ) );
            } else {
                binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds( null, getResources().getDrawable( R.drawable.ic_baseline_star_border_orange_24 ), null, null );
                LikeButton.dislikeRestaurant( mPlaceDetails, this, binding.likeBtn, getResources().getString( R.string.detail_restaurant_like ), getResources().getString( R.string.detail_restaurant_unlike ), getResources().getString( R.string.detail_restaurant_like ) );
                Toast.makeText( RestaurantDetailActivity.this, "Unlike this restaurant", Toast.LENGTH_SHORT ).show();
            }

            // Website button
        } else if (id == R.id.website_btn) {
            if (mPlaceDetails.website != null) {
                String website = mPlaceDetails.website.toString();
                Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( website ) ); // Initiates the Intent
                startActivity( browserIntent );
            } else {
                Toast.makeText( RestaurantDetailActivity.this, "No website", Toast.LENGTH_SHORT ).show();
            }
        }
    }


    // ------------------------------------
    // CONFIGURE RECYCLER VIEW
    // ------------------------------------
    private void configureRecycleView() {
        this.adapter = new RestaurantDetailAdapter( userList );
        this.binding.activityDetailRestaurantRv.setAdapter( adapter );
        this.binding.activityDetailRestaurantRv.setLayoutManager( new LinearLayoutManager( this ) );
    }


    // ------------------------------------
    // SET FAB LISTENER
    // ------------------------------------
    private void setFabListener() {
        this.binding.floatingActionButton.setOnClickListener( view -> bookThisRestaurant() );
    }

    private void bookThisRestaurant() {
        String uid = Objects.requireNonNull( getCurrentUser() ).getUid();
        String username = Objects.requireNonNull( getCurrentUser().getDisplayName() );
        String placeId = mPlaceDetails.placeId;
        String restaurantName = mPlaceDetails.name;
        checkBooked( uid, username, placeId, restaurantName );
    }


    @Nullable
    private FirebaseUser getCurrentUser() {
        return userManager.getCurrentUser();
    }

    private void checkBooked(String uid, String username, String placeId, String restaurantName) {
        BookingHelper.getBooking( uid).addOnCompleteListener( restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().size() == 1) {
                    //QueryDocumentSnapshot = restaurantTask.getResult().getDocuments().get( 0 );
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        if (Objects.equals( restaurant.getData().get( "placeId" ), placeId )) {
                            displayFloating( (R.drawable.ic_baseline_clear_orange_24) );
                            deleteBooking( uid );
                            Toast.makeText( RestaurantDetailActivity.this, R.string.cancel_booking, Toast.LENGTH_SHORT ).show();
                        } else {
                            displayFloating( (R.drawable.ic_baseline_check_circle_green_24) );
                            updateBooking( uid, username, placeId, restaurantName );
                            Toast.makeText( RestaurantDetailActivity.this, R.string.modify_booking, Toast.LENGTH_SHORT ).show();
                        }
                    }
                } else {
                    displayFloating( (R.drawable.ic_baseline_check_circle_green_24) );
                    createBooking( uid, username, placeId, restaurantName );
                    Toast.makeText( RestaurantDetailActivity.this, R.string.new_booking, Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void updateBooking(String uid, String username, String placeId, String restaurantName) {
        RestaurantHelper.updateBooking( uid, username, placeId, restaurantName );
        displayFloating( (R.drawable.ic_baseline_clear_orange_24) );
        Update_Booking_RecyclerView( mPlaceDetails.placeId );
    }

    private void createBooking(String uid, String username, String placeId, String restaurantName) {
        BookingHelper.createBooking( uid, username, placeId, restaurantName ).addOnFailureListener( onFailureListener() );
        displayFloating( (R.drawable.ic_baseline_clear_orange_24) );
        Update_Booking_RecyclerView( mPlaceDetails.placeId );
    }

    private void deleteBooking(String uid) {
        BookingHelper.updateBooking( uid );
        displayFloating( (R.drawable.ic_baseline_check_circle_green_24) );
        Update_Booking_RecyclerView( mPlaceDetails.placeId );
    }

    private void displayFloating(int icon) {
        // int colorUnbooking = ContextCompat.getColor(this, R.color.colorError);
        int color = ContextCompat.getColor( this, R.color.colorFab );
        Drawable mDrawable = Objects.requireNonNull( ContextCompat.getDrawable( getBaseContext(), icon ) ).mutate();
        binding.floatingActionButton.setImageDrawable( mDrawable );
        binding.floatingActionButton.getDrawable().setColorFilter( color, PorterDuff.Mode.SRC_IN );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void Update_Booking_RecyclerView(String placeId) {
        userList.clear();
        RestaurantHelper.getTodayBooking( placeId, getTodayDate() ).addOnCompleteListener( restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().isEmpty()) {
                    adapter.notifyDataSetChanged();
                } else {
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        UserRepository.getUser( Objects.requireNonNull( restaurant.getData().get( "userId" ) ).toString() ).addOnCompleteListener( workmateTask -> {
                            if (workmateTask.isSuccessful()) {
                                String uid = Objects.requireNonNull( workmateTask.getResult().getData().get( "uid" ) ).toString();
                                String name = Objects.requireNonNull( Objects.requireNonNull( workmateTask.getResult().getData() ).get( "name" ) ).toString();
                                String urlPicture = Objects.requireNonNull( workmateTask.getResult().getData().get( "urlPicture" ) ).toString();
                                User workmateToAdd = new User( uid, urlPicture, name );
                                userList.add( workmateToAdd );
                            }
                            adapter.notifyDataSetChanged();
                        } );
                    }
                }
            }
        } );
    }


    private void getRestaurantPlaceId() {
        if (getIntent().hasExtra( RESTAURANT_PLACE_ID )) {
            String placeId = getIntent().getStringExtra( RESTAURANT_PLACE_ID );
            getRestaurantDetail( placeId );
            initWorkmateList();
        }
    }

    private void initWorkmateList() {
        userList = new ArrayList<>();
        adapter = new RestaurantDetailAdapter( userList );
        binding.activityDetailRestaurantRv.setLayoutManager( new LinearLayoutManager( this ) );
        binding.activityDetailRestaurantRv.setAdapter( adapter );
    }

    private void getRestaurantDetail(String placeId) {
        executor.execute( (() -> {
            mPlaceDetails = new FetchDetail().run( placeId );

            mainExecutor.execute( (() -> {
                configureButtonClickListener();
                displayInfoRestaurant();
            }) );
        }) );
    }

    private void displayInfoRestaurant() {

        // Restaurant image
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

        // Display restaurant name
        binding.activityDetailRestaurantName.setText( mPlaceDetails.name );

        // Display rating bar
        float rating = (mPlaceDetails.rating / 5) * 3;
        binding.itemRestaurantListRatingbar.setRating( rating );

        //Display restaurant address
        binding.activityDetailRestaurantAddress.setText( mPlaceDetails.formattedAddress );
    }


    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText( getApplicationContext(), getString( R.string.error_unknown_error ), Toast.LENGTH_LONG ).show();
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