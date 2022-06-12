package fr.steve.leroy.go4lunch;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.notification.NotificationService;
import fr.steve.leroy.go4lunch.ui.map.MapFragment;
import fr.steve.leroy.go4lunch.ui.restaurant_details.RestaurantDetailActivity;
import fr.steve.leroy.go4lunch.ui.restaurant_list.ListViewFragment;
import fr.steve.leroy.go4lunch.ui.workmates_list.WorkmateFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private final UserManager userManager = UserManager.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private PlacesClient placesClient;

    private enum ALL_FRAGMENTS {MAPVIEW, LISTVIEW, WORKMATES}
    private ALL_FRAGMENTS currentView = ALL_FRAGMENTS.MAPVIEW;

    private ImageView profileImageView;
    private TextView usernameEditText;
    private TextView emailTextView;

    private androidx.appcompat.widget.SearchView searchView;
    private MenuItem mMenuItem;

    //FOR DESIGN
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initView();

        configureUI();

        updateMainFragment( R.id.map_view_item );

        initPlaceApiClient();

        setDailyNotification();


    }


    private void setDailyNotification() {

        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 10 );
        calendar.set( Calendar.MINUTE, 14 );
        calendar.set( Calendar.SECOND, 0 );
        // If user hasn't chosen lunch spot before noon, set alarm for day after
        if (calendar.get( Calendar.HOUR_OF_DAY ) > 2) calendar.add( Calendar.DATE, 1 );
        // The next alarm will therefore be at 12:00 the next day

        Intent resultIntent = new Intent( this, NotificationService.class );
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        if (alarmManager != null) {
            alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );
        }
    }


    private void initPlaceApiClient() {
        if (!Places.isInitialized())
            Places.initialize( getApplicationContext(), BuildConfig.ApiPlaceKey );
        placesClient = Places.createClient( this );
    }


    // ------------------------------------
    // INIT VIEW
    // ------------------------------------

    private void initView() {
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }


    // ------------------------------------
    // CONFIGURE UI NAVIGATION
    // ------------------------------------

    private void configureUI() {
        this.configureToolbar();
        this.configureBottomView();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }


    // ------------------------------------
    // TOOLBAR
    // ------------------------------------

    /**
     * Configure the Toolbar.
     */
    private void configureToolbar() {
        toolbar = binding.toolbar.mainToolbar;
        toolbar.setTitle( getString( R.string.i_am_hungry ) );
        setSupportActionBar( binding.toolbar.mainToolbar );
    }

    /**
     * Inflate the menu and add it to the Toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.toolbar_search_menu, menu );

        mMenuItem = menu.findItem( R.id.search_menu );
        searchView = (androidx.appcompat.widget.SearchView) mMenuItem.getActionView();
        searchView.setOnQueryTextListener( new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        } );
        return true;
    }


    /**
     * Handle actions on menu items.
     *
     * @param item Item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:

                // TODO : use enum and switch
                switch (currentView) {
                    case MAPVIEW:
                        configureAutocomplete();
                        break;
                    case LISTVIEW:
                        //No filtering
                        break;
                    case WORKMATES:
                        break;
                }
                return true;

            default:
                return super.onOptionsItemSelected( item );
        }
    }

    public void configureAutocomplete() {
/*
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363), //dummy lat/lng
                new LatLng(-33.858754, 151.229596));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                .setLocationRestriction(bounds)
                .setOrigin(new LatLng(-33.8749937,151.2041382))
                .setCountry("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                //.setQuery(queryText.getText().toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });


 */




        // Initialize place field list
        List<Place.Field> fieldList = Arrays.asList( Place.Field.ID,Place.Field.NAME );
        // Start the autocomplete intent.
        Intent autocompleteIntent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fieldList )
                .setTypeFilter( TypeFilter.ESTABLISHMENT )
                .setCountry( "FR" )
                //.setLocationBias(  )
                .build( MainActivity.this );
        // Start activity result
        startActivityForResult( autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE );



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        searchView.setIconified( true );
        mMenuItem.collapseActionView();
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent( data );
                Intent intent = new Intent( MainActivity.this, RestaurantDetailActivity.class );
                intent.putExtra( "placeId", place.getId() );
                startActivity( intent );

                Log.i( TAG, "PlaceSelected: " + place.getName() + ", " + place.getId() );
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent( data );
                Log.i( TAG, status.getStatusMessage() );
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText( this,"Search canceled", Toast.LENGTH_SHORT ).show() ;
            }
            return;
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    private void updateToolbarTitle(boolean status) {
        String title;
        if (status) title = getResources().getString( R.string.i_am_hungry );
        else title = getResources().getString( R.string.available_workmates );
        binding.toolbar.mainToolbar.setTitle( title );
    }

    // ------------------------------------
    // BOTTOM NAVIGATION VIEW
    // ------------------------------------

    private void configureBottomView() {
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener( item -> updateMainFragment( item.getItemId() ) );
    }


    @SuppressLint("NonConstantResourceId")
    private boolean updateMainFragment(int itemId) {
        Fragment selectedFragment = null;
        switch (itemId) {
            case R.id.map_view_item:
                currentView = ALL_FRAGMENTS.MAPVIEW;
                updateToolbarTitle( true );
                selectedFragment = new MapFragment();
                break;
            case R.id.list_view_item:
                currentView = ALL_FRAGMENTS.LISTVIEW;
                updateToolbarTitle( true );
                selectedFragment = new ListViewFragment();
                break;
            case R.id.workmates_item:
                currentView = ALL_FRAGMENTS.WORKMATES;
                updateToolbarTitle( false );
                selectedFragment = new WorkmateFragment();
                break;
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.activity_main_fragment_container, selectedFragment )
                .commit();
        return true;
    }


    // ------------------------------------
    // DRAWER
    // ------------------------------------

    /**
     * Close the NavigationDrawer or the Autocomplete bar with the button back
     */
    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.binding.activityMainDrawerLayout.isDrawerOpen( GravityCompat.START )) {
            // Close DrawerLayout if displayed
            this.binding.activityMainDrawerLayout.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }


    private void configureDrawerLayout() {
        DrawerLayout drawerLayout = binding.activityMainDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
    }


    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener( this );

        updateUserInfoInNavigationHeader();
    }


    // Update the Navigation View Header with user information
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateUserInfoInNavigationHeader() {
        binding.activityMainNavView.setNavigationItemSelectedListener( this );
        profileImageView = binding.activityMainNavView.getHeaderView( 0 ).findViewById( R.id.main_activity_nav_header_user_picture );
        usernameEditText = binding.activityMainNavView.getHeaderView( 0 ).findViewById( R.id.main_activity_nav_header_user_name );
        emailTextView = binding.activityMainNavView.getHeaderView( 0 ).findViewById( R.id.main_activity_nav_header_user_email );

        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();

            if (user.getPhotoUrl() != null) {
                setProfilePicture( user.getPhotoUrl() );
            } else {
                profileImageView.setImageDrawable( getResources()
                        .getDrawable( R.drawable.ic_anon_user ) );
            }
            setTextUserData( user );
        }
    }


    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with( this )
                .load( profilePictureUrl )
                .apply( RequestOptions.circleCropTransform() )
                .into( profileImageView );
    }

    private void setTextUserData(FirebaseUser user) {
        //Get email & username from User
        String email = TextUtils.isEmpty( user.getEmail() ) ? getString( R.string.info_no_email_found ) : user.getEmail();
        String username = TextUtils.isEmpty( user.getDisplayName() ) ? getString( R.string.info_no_username_found ) : user.getDisplayName();

        //Update views with data
        usernameEditText.setText( username );
        emailTextView.setText( email );
    }


    /**
     * Call when an item is selected. Display the associate activity.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_menu_your_lunch_btn:
                userManager.getUserData().addOnSuccessListener( new OnSuccessListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user.getPlaceId().isEmpty()) {
                            Toast.makeText( MainActivity.this, "No restaurant reserved", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( MainActivity.this, "You booked " + user.getRestaurantName(), Toast.LENGTH_SHORT ).show();
                            Intent intent = new Intent( MainActivity.this, RestaurantDetailActivity.class );
                            intent.putExtra( "placeId", user.getPlaceId() );
                            startActivity( intent );
                        }
                    }
                } );
                break;

            case R.id.drawer_menu_settings_btn:
                startActivity( new Intent( android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse( "package:" + BuildConfig.APPLICATION_ID ) ) ); // startActivityForResult( new Intent( Settings.ACTION_SETTINGS ), 0 );
                break;

            case R.id.drawer_menu_logout_btn:
                new AlertDialog.Builder( this )
                        .setTitle( R.string.popup_logout_title )
                        .setMessage( R.string.popup_logout_message )
                        .setPositiveButton( R.string.popup_logout_yes_btn, (dialogInterface, i) -> {

                            userManager.signOut( MainActivity.this )
                                    .addOnSuccessListener( aVoid -> {
                                        finish();
                                    } );
                        } )
                        .setNegativeButton( R.string.popup_logout_no_btn, null )
                        .show();
                break;

            default:
                break;
        }
        this.binding.activityMainDrawerLayout.closeDrawer( GravityCompat.START );
        return true;
    }
}