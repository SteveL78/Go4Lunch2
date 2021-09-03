package fr.steve.leroy.go4lunch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initView();





    }

    private void initView() {
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }
}