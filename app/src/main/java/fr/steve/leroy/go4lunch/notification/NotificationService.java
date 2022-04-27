package fr.steve.leroy.go4lunch.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentSnapshot;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;

import fr.steve.leroy.go4lunch.MainActivity;
import fr.steve.leroy.go4lunch.R;
import fr.steve.leroy.go4lunch.firebase.BookingHelper;
import fr.steve.leroy.go4lunch.manager.UserManager;
import fr.steve.leroy.go4lunch.model.User;
import fr.steve.leroy.go4lunch.repositories.UserRepository;

/**
 * Created by Steve LEROY on 10/04/2022.
 */
public class NotificationService extends BroadcastReceiver {

    private Context context;
    private final UserManager userManager = UserManager.getInstance();
    private User user;

    private String notificationBody;

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "NotificationService : ";

    private PendingIntent pendingIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals( "android.intent.action.BOOT_COMPLETED" )) {
            Intent serviceIntent = new Intent( context, MainActivity.class );
            context.startService( serviceIntent );
        } else {
            fetchUserData( context );
        }
    }


    private void fetchUserData(Context context) {
        UserRepository.getUser( userManager.getCurrentUser().getUid() ).addOnSuccessListener( documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject( User.class );
                if (user != null && !user.getRestaurantName().equals( "" )) {
                    fetchOtherUsersEatingHere( context );
                }
            }
        } );
    }


    private void fetchOtherUsersEatingHere(Context context) {
        ArrayList<User> usersEatingHere = new ArrayList<>();
        BookingHelper.getWorkmatesEatingHere( user.getPlaceId() ).addOnSuccessListener( queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> listOfUsersEatingHereToo = new ArrayList<>( queryDocumentSnapshots.getDocuments() );
                if (listOfUsersEatingHereToo.size() != 0) {
                    for (DocumentSnapshot documentSnapshot : listOfUsersEatingHereToo) {
                        User workmateTemp = documentSnapshot.toObject( User.class );
                        if (workmateTemp != null && !user.getUid().equals( workmateTemp.getUid() )) {
                            usersEatingHere.add( workmateTemp );
                        }
                    }
                }
            }
            if (usersEatingHere.size() != 0) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                do {
                    if (i == usersEatingHere.size() - 1) {
                        builder.append( usersEatingHere.get( i ).getUsername() ).append( "." ).toString();
                    } else {
                        builder.append( usersEatingHere.get( i ).getUsername() ).append( ", " ).toString();
                    }
                    i++;
                } while (i != usersEatingHere.size());
                notificationBody = context.getString( R.string.notification_text_with ) + user.getRestaurantName() + " as " + builder;
            } else {
                notificationBody = context.getString( R.string.notification_text ) + user.getRestaurantName();
            }
            this.sendVisualNotification( context );
        } );

            /*
            if (usersEatingHere.size() != 0) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                do {
                    if (i == usersEatingHere.size() - 1) {
                        builder.append( usersEatingHere.get( i ).getUsername() ).append( "." ).toString();
                    } else {
                        builder.append( usersEatingHere.get( i ).getUsername() ).append( ", " ).toString();
                    }
                    i++;
                } while (i != usersEatingHere.size());
                notificationBody = context.getString( R.string.notification_text_with ) + user.getRestaurantName() + " as " + builder;
            } else {
                notificationBody = context.getString( R.string.notification_text ) + user.getRestaurantName();
            }
            this.sendVisualNotification( context );
        } );

             */
    }


    private void sendVisualNotification(Context context) {

        // Create a Channel (Android 8)
        String channelId = context.getString( R.string.default_notification_channel_id );

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( context, channelId )
                .setSmallIcon( R.drawable.ic_logo_go4lunch )
                .setContentTitle( context.getString( R.string.notification_content ) )
                .setAutoCancel( true )
                .setPriority( NotificationCompat.PRIORITY_HIGH )
                .setSound( RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION ) )
                .setContentIntent( pendingIntent )
                .setContentText( notificationBody );


        NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );

        // Support Version >= Android 8
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification message";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel( channelId, channelName, importance );
            notificationManager.createNotificationChannel( mChannel );
        }

        // Show notification
        notificationManager.notify( NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build() );

        // Delete the booking once the notification has been sent
        //BookingHelper.deleteBooking( userManager.getCurrentUser().getUid() );

    }
}

/*
            StringBuilder builder = new StringBuilder( context.getString( R.string.notification_text_workmates ) );
            int i;
            for (i = 0; i <= usersEatingHere.size(); i++) {
                if (i == usersEatingHere.size() - 1) {
                    notificationBody = builder.append( usersEatingHere.get( i ).getUsername() ).append( "." );
                } else {
                    builder.append( usersEatingHere.get( i ).getUsername() ).append( ", " );
                }
                i++;
            }
            this.sendVisualNotification( context );
        } );


    }
 */