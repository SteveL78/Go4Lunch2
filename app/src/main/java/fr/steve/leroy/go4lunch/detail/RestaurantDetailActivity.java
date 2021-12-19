package fr.steve.leroy.go4lunch.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.steve.leroy.go4lunch.FetchDetail;
import fr.steve.leroy.go4lunch.NearbySearch;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;
import fr.steve.leroy.go4lunch.databinding.WorkmatesJoiningItemBinding;
import fr.steve.leroy.go4lunch.entities.RestaurantAPI;
import fr.steve.leroy.go4lunch.list.RestaurantListAdapter;
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

        this.mainExecutor = ContextCompat.getMainExecutor( this);

        // this.configureRecyclerView();

        //  updateUi( restaurantApi, placesSearchResult );

        //activityBinding.activityDetailRestaurantCallBtn.setOnClickListener( v -> makePhoneCall() );


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
        activityBinding.activityDetailRestaurantName.setText( mPlaceDetails.name );

        activityBinding.activityDetailRestaurantAddress.setText( mPlaceDetails.formattedAddress );

        float rating = (mPlaceDetails.rating / 5) * 3;
        activityBinding.itemRestaurantListRatingbar.setRating( rating );
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
        activityBinding.activityDetailRestaurantWebsiteBtn.setOnClickListener( v -> openWebsite( mPlaceDetails.website.toString() ));

    }

    private void openDialer(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.trim().length() > 0) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber ));// Initiates the Intent
            startActivity(intent);

        } else {
            Toast.makeText( RestaurantDetailActivity.this, "No phone", Toast.LENGTH_SHORT ).show();
        }
    }


    private void openWebsite(String website) {
        if (website != null) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
            startActivity(browserIntent);

        } else {
            Toast.makeText( RestaurantDetailActivity.this, "No website", Toast.LENGTH_SHORT ).show();
        }

    }








/*
    private void updatePhoto(RestaurantAPI restaurantApi, PlacesSearchResult placesSearchResult) {
      RequestManager glide = Glide.with(recyclerView);

        //Restaurant image
        if (placesSearchResult.photos != null && placesSearchResult.photos.length > 0) {

            Log.d( "photos", placesSearchResult.photos[0].toString() );

            String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + placesSearchResult.photos[0].photoReference + "&key=" + context.getString( R.string.google_maps_API_key );

            Glide.with( context )
                    .load( imageurl )
                    .centerCrop()
                    .into( activityBinding.activityDetailRestaurantImg );
        } else {
            activityBinding.activityDetailRestaurantImg.setImageResource( R.drawable.image_not_avalaible );
        }
    }
/*
        //Restaurant name
        activityBinding.activityDetailRestaurantName.setText( restaurantApi.getName() );

        //Restaurant address
        activityBinding.activityDetailRestaurantAddress.setText( restaurantApi.getVicinity() );

        //Phone
*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }








/*
    private void configureRecyclerView() {
        this.mWorkmateList = new ArrayList<>();
        this.adapter = new RestaurantDetailAdapter( this.mWorkmateList );
        this.recyclerView.setAdapter( this.adapter );
        this.recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
    }

 */


    // ------------------------------------------------------------------------
    // BUTTONS
    // ------------------------------------------------------------------------
// TODO : OnClick

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText( RestaurantDetailActivity.this, "Permission DENIED", Toast.LENGTH_SHORT ).show();
            }
        }
    }*/
/*
    private void makePhoneCall() {
        mPhoneNumber = restaurantApi.getPhoneNumber();

        if (mPhoneNumber.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission( RestaurantDetailActivity.this,
                    Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( RestaurantDetailActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL );

                Intent intent = new Intent( Intent.ACTION_CALL );
                intent.setData( Uri.parse( mPhoneNumber ) );
                startActivity( intent );

            } else {
                Toast.makeText( RestaurantDetailActivity.this, "Permission DENIED", Toast.LENGTH_SHORT ).show();
            }

        } else {
            Toast.makeText( RestaurantDetailActivity.this, "No phone", Toast.LENGTH_SHORT ).show();
        }

    }*/

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }


}




