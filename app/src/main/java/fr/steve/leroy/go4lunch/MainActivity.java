package fr.steve.leroy.go4lunch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import fr.steve.leroy.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mBinding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( mBinding.getRoot() );

        mAuth = FirebaseAuth.getInstance();

        mBinding.logoutButton.setOnClickListener( v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity( new Intent( MainActivity.this, SignInActivity.class ) );
        } );
    }
}