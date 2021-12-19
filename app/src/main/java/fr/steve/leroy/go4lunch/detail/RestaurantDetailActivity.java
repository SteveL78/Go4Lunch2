package fr.steve.leroy.go4lunch.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.FetchDetail;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;
import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.model.Workmate;
import io.reactivex.rxjava3.disposables.Disposable;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ActivityRestaurantDetailBinding activityBinding;
    private WorkmatesJoiningItemBinding workmatesBinding;

    private RestaurantDetailAdapter adapter;
    private RecyclerView recyclerView;
    private RestaurantDetailViewModel viewModel;
    private Disposable disposable;
    private List<Workmate> mWorkmateList;
    private PlacesSearchResult placesSearchResult;
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
                .into( activityBinding.activityDetailRestaurantImg );


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
        activityBinding.activityDetailRestaurantName.setText( mPlaceDetails.name );

        //Rating
        float rating = (mPlaceDetails.rating / 5) * 3;
        activityBinding.itemRestaurantListRatingbar.setRating( rating );

        //Restaurant address
        activityBinding.activityDetailRestaurantAddress.setText( mPlaceDetails.formattedAddress );

    }


    // ------------------------------------
    // INIT VIEW
    // ------------------------------------

    private void initView() {
        activityBinding = ActivityRestaurantDetailBinding.inflate( getLayoutInflater() );
        setContentView( activityBinding.getRoot() );
    }


    // ------------------------------------
    // INIT LISTENERS
    // ------------------------------------

    private void initListeners() {
        activityBinding.activityDetailRestaurantCallBtn.setOnClickListener( v -> openDialer( mPlaceDetails.formattedPhoneNumber ) );
        activityBinding.activityDetailRestaurantWebsiteBtn.setOnClickListener( v -> openWebsite( mPlaceDetails.website.toString() ) );

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




