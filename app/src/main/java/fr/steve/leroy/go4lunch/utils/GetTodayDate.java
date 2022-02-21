package fr.steve.leroy.go4lunch.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Steve LEROY on 21/01/2022.
 */
public class GetTodayDate {

    public static String getTodayDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sfDate = new SimpleDateFormat( "dd-MM-yy", Locale.getDefault() );
        return sfDate.format( c.getTime() );
    }

}