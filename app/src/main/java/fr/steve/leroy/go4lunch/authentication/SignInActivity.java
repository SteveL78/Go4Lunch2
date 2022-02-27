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
import fr.steve.leroy.go4lunch.firebase.WorkmateHelper;

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

    }


    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // Check if user is logged-in
    private void checkUserAlreadyLogged() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        }
    }

    private void createWorkmate() {
        if (getCurrentUser() != null) {
            WorkmateHelper.getWorkmate(getCurrentUser().getUid()).addOnCompleteListener( UserTask -> {
                        if (UserTask.isSuccessful()) {
                            if (!UserTask.getResult().exists()) {
                                String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
                                if (getCurrentUser().getDisplayName() != null) {
                                    String name = getCurrentUser().getDisplayName();
                                    String uid = getCurrentUser().getUid();
                                    WorkmateHelper.createWorkmate(uid, urlPicture, name).addOnFailureListener(onFailureListener());
                                } else {
                                    String name = getCurrentUser().getEmail();
                                    String uid = getCurrentUser().getUid();
                                    WorkmateHelper.createWorkmate(uid, urlPicture, name).addOnFailureListener(onFailureListener());
                                }
                            }
                        }
                    }
            );
        }
    }

    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText( getApplicationContext(), getString( R.string.error_unknown_error ), 1);
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
                createWorkmate();
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

}