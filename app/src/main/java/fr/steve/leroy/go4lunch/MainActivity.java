package fr.steve.leroy.go4lunch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.list.ListViewFragment;
import fr.steve.leroy.go4lunch.map.MapFragment;
import fr.steve.leroy.go4lunch.workmates.WorkmateFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;

    private Toolbar mToolbar;
    private NavController mNavController;
    private DrawerLayout mDrawerLayout;

    private NavigationView navigationView;
    private MenuItem mSearchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );


        this.configureToolbar();
        this.configureDrawer();

        // Bottom nav
        BottomNavigationView bottomNavigationView = findViewById( R.id.activity_main_bottom_navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener( navListener );        // Setting Map Fragment as main fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.activity_main_frame_layout, new MapFragment() )
//                .addToBackStack( null )
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.toolbar_search_menu, menu );

        mSearchItem = menu.findItem( R.id.search_button );
        return true;
    }

    // 1 - Configure Toolbar
    private void configureToolbar() {

        mToolbar = binding.mainToolbar;
        mToolbar.setTitle( getString( R.string.i_am_hungry ) );

        setSupportActionBar( binding.mainToolbar );

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder( R.id.map_view_item, R.id.list_view_item, R.id.workmates_item )
                .setOpenableLayout( binding.getRoot() )
                .build();

        //NavigationUI.setupWithNavController( mToolbar, mNavController, appBarConfiguration);
    }



    private void configureDrawer() {
        this.mDrawerLayout = (DrawerLayout) findViewById( R.id.activity_main_drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        mDrawerLayout.addDrawerListener( toggle );
        toggle.syncState();
    }


    // Listener Bottom Nav
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
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

            // Begin transaction
            getSupportFragmentManager().beginTransaction()
                    .replace( R.id.activity_main_frame_layout, selectedFragment )
                    .commit();
            return true;
        }
    };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}