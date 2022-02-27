package fr.steve.leroy.go4lunch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.model.PlaceDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.steve.leroy.go4lunch.authentication.SignInActivity;
import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.firebase.WorkmateHelper;
import fr.steve.leroy.go4lunch.ui.map.MapFragment;
import fr.steve.leroy.go4lunch.ui.restaurant_details.RestaurantDetailActivity;
import fr.steve.leroy.go4lunch.ui.restaurant_list.ListViewFragment;
import fr.steve.leroy.go4lunch.ui.workmates_list.WorkmateFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private List<PlaceDetails> placeDetailList = new ArrayList<>();
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private PlacesClient placesClient;

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // Configure all views
        this.configureToolbar();
        this.configureBottomView();
        this.configureDrawerLayout();
        this.configureNavigationView();

        updateMainFragment( R.id.map_view_item );

        initPlaceApiClient();



    }


    @Nullable
    private FirebaseUser getCurrentUser() {
        return WorkmateHelper.getCurrentWorkmate();
    }


    private void initPlaceApiClient() {
        if (!Places.isInitialized())
            Places.initialize( getApplicationContext(), BuildConfig.ApiPlaceKey );
        placesClient = Places.createClient(this);
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

/*
        final MenuItem searchItem = menu.findItem(R.id.search_menu);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

 */

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        // Here is where we are going to implement the filter logic
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    /**
     * Handle actions on menu items.
     * @param item Item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:

                placeDetailList.clear();
                List<Place.Field> fields = Arrays.asList( Place.Field.ID, Place.Field.NAME );

                return true;
            default:
                return super.onOptionsItemSelected( item );
        }


        /*
        switch (item.getItemId()) {
            case R.id.search_menu:

                placeDetailList.clear();



                /*
                List<Place.Field> fields = Arrays.asList( Place.Field.ID, Place.Field.NAME );
                // Define the region
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng( getLocation().getLatitude() - 0.01, getLocation().getLongitude() - 0.01 ),
                        new LatLng( getLocation().getLatitude() + 0.01, getLocation().getLongitude() + 0.01 ) );
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields )
                        .setLocationBias( bounds )
                        .setTypeFilter( TypeFilter.ESTABLISHMENT )
                        .build( this );
                startActivityForResult( intent, AUTOCOMPLETE_REQUEST_CODE );
                */


                //onSearchCalled();



/*
                SearchManager manager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);

                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint( "Search restaurants" );
*/



/*
                MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
                searchView = (SearchView) myActionMenuItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // Toast like print
                        UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                        if( ! searchView.isIconified()) {
                            searchView.setIconified(true);
                        }
                        myActionMenuItem.collapseActionView();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                        return false;
                    }
                });
                return true;
*/

                /*
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint( "Search restaurants" );

                SearchManager mSearchManager = (SearchManager) requireContext().getSystemService( Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(mSearchManager.getSearchableInfo(((MainActivity) requireContext()).getComponentName()));
                searchView.setIconifiedByDefault( false );
                searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        if(s.length() >2){


                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                } );
*/

                // Toast.makeText( this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG ).show();
              /*  return true;
            default:
                return super.onOptionsItemSelected( item );
        }
        */
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });


       /* if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                Toast.makeText(MainActivity.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getAddress();
                // do query with address

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(MainActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                //Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        */
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
                updateToolbarTitle( true );
                selectedFragment = new MapFragment();
                break;
            case R.id.list_view_item:
                updateToolbarTitle( true );
                selectedFragment = new ListViewFragment();
                break;
            case R.id.workmates_item:
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
        this.drawerLayout = binding.activityMainDrawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
    }


    private void configureNavigationView() {
        this.navigationView = binding.activityMainNavView;
        navigationView.setNavigationItemSelectedListener( this );

        updateUserInfoInNavigationHeader();
    }


    // Update the Navigation View Header with user information
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateUserInfoInNavigationHeader() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View headerView = binding.activityMainNavView.getHeaderView( 0 );
        TextView userNameHeader = headerView.findViewById( R.id.main_activity_nav_header_user_name );
        TextView userMailHeader = headerView.findViewById( R.id.main_activity_nav_header_user_email );
        ImageView userAvatar = headerView.findViewById( R.id.main_activity_nav_header_user_picture );

        if (user != null) {
            try {
                userNameHeader.setText( user.getDisplayName() );
                userMailHeader.setText( user.getEmail() );
                if (user.getPhotoUrl() != null) {
                    Glide.with( this )
                            .load( user.getPhotoUrl() )
                            .apply( RequestOptions.circleCropTransform() )
                            .into( userAvatar );

                } else userAvatar.setImageDrawable( getResources()
                        .getDrawable( R.drawable.ic_anon_user ) );
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }


    /**
     * Call when an item is selected. Display the associate activity.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_menu_your_lunch_btn:
                // Intent utilisé temporairement pour tests
                Intent intent = new Intent( this, RestaurantDetailActivity.class );
                startActivity( intent );
                break;

            case R.id.drawer_menu_settings_btn:
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));               // startActivityForResult( new Intent( Settings.ACTION_SETTINGS ), 0 );
                break;

            case R.id.drawer_menu_logout_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder( this );
                builder.setTitle( "Logout" );
                builder.setMessage( "Are you sure you want to logout?" );
                builder.setPositiveButton( "YES", (dialog, which) -> {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent1 = new Intent( getApplicationContext(), SignInActivity.class );
                    intent1.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity( intent1 );
                } );
                builder.setNegativeButton( "NO", (dialog, which) -> dialog.dismiss() );
                builder.show();
                break;

            default:
                break;
        }
        this.binding.activityMainDrawerLayout.closeDrawer( GravityCompat.START );
        return true;
    }


}