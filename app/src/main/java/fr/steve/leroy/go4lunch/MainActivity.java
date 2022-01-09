package fr.steve.leroy.go4lunch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.model.PlaceDetails;

import java.util.List;

import fr.steve.leroy.go4lunch.authentication.SignInActivity;
import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.detail.RestaurantDetailActivity;
import fr.steve.leroy.go4lunch.list.ListViewFragment;
import fr.steve.leroy.go4lunch.map.MapFragment;
import fr.steve.leroy.go4lunch.workmates.WorkmateFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private List<PlaceDetails> placeDetailList;

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

    }


    // ------------------------------------
    // TOOLBAR
    // ------------------------------------

    // 1 - Configure Toolbar
    private void configureToolbar() {
        toolbar = binding.toolbar.mainToolbar;
        toolbar.setTitle( getString( R.string.i_am_hungry ) );
        setSupportActionBar( binding.toolbar.mainToolbar );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate( R.menu.toolbar_search_menu, menu );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu item
        switch (item.getItemId()) {
            case R.id.search_menu:


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
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }


    // ------------------------------------
    // BOTTOM NAVIGATION VIEW
    // ------------------------------------

    private void configureBottomView() {
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener( item -> updateMainFragment( item.getItemId() ) );
    }


    private boolean updateMainFragment(int itemId) {
        Fragment selectedFragment = null;
        switch (itemId) {
            case R.id.map_view_item:
                selectedFragment = new MapFragment();
                break;
            case R.id.list_view_item:
                selectedFragment = new ListViewFragment();
                break;
            case R.id.workmates_item:
                selectedFragment = new WorkmateFragment();
                break;
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.activity_main_frame_layout, selectedFragment )
                .commit();
        return true;
    }


    // ------------------------------------
    // DRAWER
    // ------------------------------------

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen( GravityCompat.START )) {
            this.drawerLayout.closeDrawer( GravityCompat.START );
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

        updateNavigationHeader();
    }

    private void updateNavigationHeader() {

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences( "MyPrefs", MODE_PRIVATE );

        String userName = preferences.getString( "userName", "" );
        String userEmail = preferences.getString( "userEmail", "" );
        String userPhotoUrl = preferences.getString( "userPhoto", "" );

        View headerView = binding.activityMainNavView.getHeaderView( 0 );
        TextView userNameHeader = headerView.findViewById( R.id.main_activity_nav_header_user_name );
        userNameHeader.setText( userName );

        TextView userMailHeader = headerView.findViewById( R.id.main_activity_nav_header_user_email );
        userMailHeader.setText( userEmail );

        Glide.with( this )
                .load( userPhotoUrl )
                .centerCrop()
                .circleCrop()
                .into( (ImageView) headerView.findViewById( R.id.main_activity_nav_header_user_picture ) );

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Intent intent = null;
        switch (item.getItemId()) {
            case R.id.drawer_menu_your_lunch_btn:
                Intent intent = new Intent( this, RestaurantDetailActivity.class );
                startActivity( intent );
                break;

            case R.id.drawer_menu_settings_btn:
                startActivityForResult( new Intent( Settings.ACTION_SETTINGS ), 0 );
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