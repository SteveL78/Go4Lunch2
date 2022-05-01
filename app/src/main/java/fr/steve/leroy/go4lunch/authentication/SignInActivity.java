package fr.steve.leroy.go4lunch.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;
import java.util.List;

import fr.steve.leroy.go4lunch.MainActivity;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.databinding.ActivitySigninBinding;
import fr.steve.leroy.go4lunch.manager.UserManager;

public class SignInActivity extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();

    public ActivitySigninBinding binding;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        initBinding();

        checkUserAlreadyLogged();

        setupListeners();

        FacebookSdk.sdkInitialize( getApplicationContext() );
        AppEventsLogger.activateApp( getApplication() );

        OAuthProvider.Builder provider = OAuthProvider.newBuilder( "twitter.com" );
        // Target specific email with login hint.
        provider.addCustomParameter( "lang", "fr" );

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
    }

    // Update Login Button when the activity is resuming
    private void updateLoginButton() {
        binding.loginButton.setText( userManager.isCurrentUserLogged() ? getString( R.string.authentication_sign_out_btn ) : getString( R.string.button_login_text_not_logged ) );
    }


    // Check if user is logged-in
    private void checkUserAlreadyLogged() {
        if (UserManager.getInstance().isCurrentUserLogged()) {
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

        IdpResponse response = IdpResponse.fromResultIntent( data );

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                userManager.createUser();
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
    // SNACK BAR
    // ------------------------------------------------------------------------

    private void showSnackBar(String message) {
        Snackbar.make( binding.activitySignInCoordinatorLayout, message, Snackbar.LENGTH_SHORT ).show();
    }
}