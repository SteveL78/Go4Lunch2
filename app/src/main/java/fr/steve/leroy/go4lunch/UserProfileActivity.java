package fr.steve.leroy.go4lunch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import fr.steve.leroy.go4lunch.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_profile );

        binding = ActivityUserProfileBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences( "MyPrefs", MODE_PRIVATE );

        String userName = preferences.getString( "userName", "" );
        String userEmail = preferences.getString( "userEmail", "" );
        String userPhotoUrl = preferences.getString( "userPhoto", "" );

        binding.profileUserNameTv.setText( "Hello " + userName );
        binding.profileUserEmailTv.setText( "You are connected with the email address : " + userEmail );

        Glide.with( this ).load( userPhotoUrl ).into( binding.profileUserPhoto );

        binding.profileSignOutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        } );
    }
}