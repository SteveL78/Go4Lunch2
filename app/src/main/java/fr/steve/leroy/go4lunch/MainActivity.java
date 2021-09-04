package fr.steve.leroy.go4lunch;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // Bottom nav
        BottomNavigationView bottomNavigationView = findViewById( R.id.activity_main_bottom_navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener( navListener );        // Setting Map Fragment as main fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.activity_main_frame_layout, new MapFragment() )
                .commit();



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
                    selectedFragment = new WorkmatesFragment();
                    break;
            }

            // Begin transaction
            getSupportFragmentManager().beginTransaction()
                    .replace( R.id.activity_main_frame_layout, selectedFragment )
                    .commit();
            return true;
        }
    };


}