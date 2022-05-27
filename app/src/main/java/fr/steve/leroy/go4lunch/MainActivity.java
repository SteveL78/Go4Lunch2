package fr.steve.leroy.go4lunch;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.notification.NotificationService;
import fr.steve.leroy.go4lunch.ui.map.MapFragment;
import fr.steve.leroy.go4lunch.ui.restaurant_details.RestaurantDetailActivity;
import fr.steve.leroy.go4lunch.ui.restaurant_list.ListViewFragment;
import fr.steve.leroy.go4lunch.ui.workmates_list.WorkmateFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final UserManager userManager = UserManager.getInstance();
    private ActivityMainBinding binding;

    private enum allFragments {MAPVIEW, LISTVIEW, WORKMATES};
    private allFragments currentView = allFragments.MAPVIEW;

    private ImageView profileImageView;
    private TextView usernameEditText;
    private TextView emailTextView;

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
        calendar.set( Calendar.HOUR_OF_DAY, 8 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        // If user hasn't chosen lunch spot before noon, set alarm for day after
        if (calendar.get( Calendar.HOUR_OF_DAY ) > 1) calendar.add( Calendar.DATE, 1 );
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
        PlacesClient placesClient = Places.createClient( this );
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
 /* MenuItem item = menu.findItem( R.id.search_menu );
 SearchView searchView = (SearchView) item.getActionView();
 searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
 @Override
 public boolean onQueryTextSubmit(String query) {
 return false;
 }

 @Override
 public boolean onQueryTextChange(String newText) {
 workmateListAdapter = new WorkmateListAdapter( );
 workmateListAdapter.getFilter().filter( newText );
 return false;
 }
 } );
 return true;
 }*/

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


                // placeDetailList.clear();
                //List<Place.Field> fields = Arrays.asList( Place.Field.ID, Place.Field.NAME );

                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
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
                currentView = allFragments.MAPVIEW;
                updateToolbarTitle( true );
                selectedFragment = new MapFragment();
                break;
            case R.id.list_view_item:
                currentView = allFragments.LISTVIEW;
                updateToolbarTitle( true );
                selectedFragment = new ListViewFragment();
                break;
            case R.id.workmates_item:
                currentView = allFragments.WORKMATES;
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

            // TODO : update username if changed user account


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