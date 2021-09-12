package fr.steve.leroy.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;
import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;

public class RestaurantDetailActivity extends AppCompatActivity {

    ActivityRestaurantDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivityRestaurantDetailBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }
}