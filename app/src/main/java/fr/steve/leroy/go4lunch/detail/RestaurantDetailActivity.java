package fr.steve.leroy.go4lunch.detail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.steve.leroy.go4lunch.databinding.ActivityRestaurantDetailBinding;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ActivityRestaurantDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivityRestaurantDetailBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        //configureRecyclerView();

    }


}




