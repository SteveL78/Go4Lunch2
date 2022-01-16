package fr.steve.leroy.go4lunch.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;
import java.util.List;

import bolts.Task;
import fr.steve.leroy.go4lunch.MainActivity;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivitySigninBinding;

public class SignInActivity extends AppCompatActivity {

    public ActivitySigninBinding binding;

    private static final int RC_SIGN_IN = 123;

    private Boolean doubleBackToExitPressedOnce = false;

    private FirebaseAuth mAuth;

    private static final String TAG_FB = "FacebookLogin";
    //  private CallbackManager mCallbackManager;

    private static final String TAG_GOOGLE = "GOOGLE_SIGN_IN_TAG";

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initBinding();

        checkUserAlreadyLogged();

        setupListeners();

        FacebookSdk.sdkInitialize( getApplicationContext() );
        AppEventsLogger.activateApp( this );

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        // Target specific email with login hint.
        provider.addCustomParameter("lang", "fr");






        // checkUserLogged();
/*
        initFirebaseAuth();

        initFacebookAuthentication();

        initGoogleAuthentication();
        binding.activitySigninGoogleLoginButton.setOnClickListener( v -> googleSignIn() );

        binding.activitySigninEmailLoginButton.setOnClickListener( v -> startEmailSignIn() );
*/
    }

    // Check if user is logged-in
    private void checkUserAlreadyLogged() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        }
    }


    // ------------------------------------------------------------------------
    // BINDING
    // ------------------------------------------------------------------------

    private void initBinding() {
        binding = ActivitySigninBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
    }


    // ------------------------------------------------------------------------
    // LISTENERS
    // ------------------------------------------------------------------------

    private void setupListeners() {
        binding.loginButton.setOnClickListener( view -> {
            starSignInActivity();
        } );
    }

    private void starSignInActivity() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build() );

        AuthMethodPickerLayout layout = new AuthMethodPickerLayout
                .Builder( R.layout.authentication_layout )
                .setGoogleButtonId( R.id.google_auth_btn )
                .setFacebookButtonId( R.id.fb_auth_btn )
                .setTwitterButtonId( R.id.twitter_auth_btn )
                .setEmailButtonId( R.id.email_auth_btn ).build();

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme( R.style.LoginTheme )
                        .setAuthMethodPickerLayout( layout )
                        .setAvailableProviders( providers )
                        .setIsSmartLockEnabled( false, true )
                        .build(),
                RC_SIGN_IN );

        // Animation
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        // Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn( requestCode, resultCode, data );
    }


    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            IdpResponse response = IdpResponse.fromResultIntent( data );

            if (resultCode == RESULT_OK) {
                //userManager.createUser();
                addUserIdToFirestoreDatabase();
                startMainActivity();
                showSnackBar( getString( R.string.connection_succeed ) );
            } else { // ERRORS

                // Show snack bar with a message
                if (response == null) {
                    showSnackBar( getString( R.string.error_authentication_canceled ) );
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar( getString( R.string.error_no_internet ) );
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar( getString( R.string.error_unknown_error ) );
                    }
                }
            }
        }
    }

    // Update Firestore database with current user information if this their first authentication
    private void addUserIdToFirestoreDatabase() {/*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        */
    }


    // ------------------------------------------------------------------------
    // START MAIN ACTIVITY
    // ------------------------------------------------------------------------

    private void startMainActivity() {
        startActivity( new Intent( SignInActivity.this, MainActivity.class ) );
        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }


    // ------------------------------------------------------------------------
    // SNACKBAR
    // ------------------------------------------------------------------------

    private void showSnackBar(String message) {
        Snackbar.make( binding.activitySignInCoordinatorLayout, message, Snackbar.LENGTH_SHORT ).show();
    }


    // ------------------------------------------------------------------------
    // FIREBASE AUTH / CHECK IF USER IS ALREADY SIGNED
    // ------------------------------------------------------------------------
/*
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    private void checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d( TAG_FB, "checkUser: Already logged in" );
            Log.d( TAG_GOOGLE, "checkUser: Already logged in" );
            startMainActivity();
            finish();
        }
    }
*/


    // ------------------------------------------------------------------------
    // AUTH WITH FACEBOOK
    // ------------------------------------------------------------------------
/*
    private void initFacebookAuthentication() {
        mCallbackManager = CallbackManager.Factory.create();
        binding.activitySigninFbLoginButton.setReadPermissions( "email", "public_profile" );
        binding.activitySigninFbLoginButton.registerCallback( mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d( TAG_FB, "facebook:onSuccess:" + loginResult );
                handleFacebookAccessToken( loginResult.getAccessToken() );
                Intent intent = new Intent( SignInActivity.this, MainActivity.class );
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
                            Intent intent = new Intent( SignInActivity.this, MainActivity.class );
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
*/

    // ------------------------------------------------------------------------
    // AUTH WITH GOOGLE
    // ------------------------------------------------------------------------
/*
    private void initGoogleAuthentication() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestIdToken( getString( R.string.default_web_client_id ) )
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient( this, gso );
    }
    // [END google_config_signin]

    private void googleSignIn() {
        // begin google sign in
        Log.d( TAG_GOOGLE, "onClick: begin Google SignIn" );
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult( intent, RC_SIGN_IN ); // Now we need to handle result of intent
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        this.handleResponseAfterSignIn( requestCode, resultCode, data );

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

    private void firebaseAuthWithGoogle(String idToken) {
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
*/


}