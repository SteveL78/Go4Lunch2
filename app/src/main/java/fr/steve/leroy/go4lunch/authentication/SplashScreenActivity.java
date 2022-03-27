package fr.steve.leroy.go4lunch.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.steve.leroy.go4lunch.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( getApplicationContext(), SignInActivity.class );
                startActivity( intent );
                finish();
            }
        }, 800 );
    }

}