package fr.steve.leroy.go4lunch.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class ShowToast {

    public static void showToast(Context context, String string, int duration) {
        Toast.makeText( context, string, duration ).show();
    }

}