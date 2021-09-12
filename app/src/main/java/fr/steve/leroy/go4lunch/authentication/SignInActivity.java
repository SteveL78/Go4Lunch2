package fr.steve.leroy.go4lunch.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import fr.steve.leroy.go4lunch.MainActivity;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.UserProfileActivity;
import fr.steve.leroy.go4lunch.databinding.ActivitySigninBinding;

public class SignInActivity extends AppCompatActivity {

    public ActivitySigninBinding binding;

    private FirebaseAuth mAuth;

    private static final String TAG_FB = "FacebookLogin";
    private CallbackManager mCallbackManager;

    private static final String TAG_GOOGLE = "GOOGLE_SIGN_IN_TAG";
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        binding = ActivitySigninBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        checkUser();

        // Configure Facebook Sign In
        initFacebookAuthentication();

        // Google Sign In
        initGoogleAuthentication();
        binding.activitySigninGoogleLoginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        } );

    }

    private void checkUser() {
        //if user is already signed in then go to profile activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d( TAG_FB, "checkUser: Already logged in" );
            Log.d( TAG_GOOGLE, "checkUser: Already logged in" );
            startActivity( new Intent( SignInActivity.this, MainActivity.class ) );
            finish();
        }
    }

    // [START auth_with_facebook]
    private void initFacebookAuthentication() {
        mCallbackManager = CallbackManager.Factory.create();
        binding.activitySigninFbLoginButton.setReadPermissions( "email", "public_profile" );
        binding.activitySigninFbLoginButton.registerCallback( mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d( TAG_FB, "facebook:onSuccess:" + loginResult );
                handleFacebookAccessToken( loginResult.getAccessToken() );
                Intent intent = new Intent( SignInActivity.this, UserProfileActivity.class );
                startActivity( intent );
            }

            @Override
            public void onCancel() {
                Log.d( TAG_FB, "facebook:onCancel" );
            }

            @Override
            public void onError(FacebookException error) {
                Log.d( TAG_FB, "facebook:onError", error );
            }
        } );
    }



        // [START auth_with_facebook]
        private void handleFacebookAccessToken(AccessToken token) {
            Log.d( TAG_FB, "handleFacebookAccessToken:" + token );

            AuthCredential credential = FacebookAuthProvider.getCredential( token.getToken() );
            mAuth.signInWithCredential( credential )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d( TAG_FB, "signInWithCredential:success" );
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent( SignInActivity.this, UserProfileActivity.class );
                                startActivity( intent );
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w( TAG_FB, "signInWithCredential:failure", task.getException() );
                                Toast.makeText( SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        }
        // [END auth_with_facebook]


        // [START google_config_signin]
        private void initGoogleAuthentication() {
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                    .requestIdToken( getString( R.string.default_web_client_id ) )
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient( this, gso );
        }
        // [END google_config_signin]

        private void googleSignIn () {
            // begin google sign in
            Log.d( TAG_GOOGLE, "onClick: begin Google SignIn" );
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult( intent, RC_SIGN_IN ); // Now we need to handle result of intent
        }

    // [START onactivityresult]
        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult( requestCode, resultCode, data );

            mCallbackManager.onActivityResult( requestCode, resultCode, data );

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult( ApiException.class );
                    Log.d( TAG_GOOGLE, "firebaseAuthWithGoogle:" + account.getId() );
                    Log.d( TAG_FB, "firebaseAuthWithFacebook:" + account.getId() );

                    firebaseAuthWithGoogle( account.getIdToken() );

                    SharedPreferences.Editor editor = getApplicationContext()
                            .getSharedPreferences( "MyPrefs", MODE_PRIVATE )
                            .edit();
                    editor.putString( "userName", account.getDisplayName() );
                    editor.putString( "userEmail", account.getEmail() );
                    editor.putString( "userPhoto", account.getPhotoUrl().toString() );
                    editor.apply();

                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w( TAG_GOOGLE, "Google sign in failed", e );
                    Log.w( TAG_FB, "Facebook sign in failed", e );
                    Toast.makeText( SignInActivity.this, "Authentication failed" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            }
        }

        private void firebaseAuthWithGoogle (String idToken){
            AuthCredential credential = GoogleAuthProvider.getCredential( idToken, null );
            mAuth.signInWithCredential( credential )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d( TAG_GOOGLE, "signInWithCredential:success" );
                                FirebaseUser user = mAuth.getCurrentUser();
                                // get user info
                                String uid = user.getUid();
                                String email = user.getEmail();
                                Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                                startActivity( intent );
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w( TAG_GOOGLE, "signInWithCredential:failure", task.getException() );
                                Toast.makeText( SignInActivity.this, "Authentication failed", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        }


    }