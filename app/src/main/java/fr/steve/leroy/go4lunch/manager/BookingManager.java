package fr.steve.leroy.go4lunch.manager;

/**
 * Created by Steve LEROY on 20/03/2022.
 */
public class BookingManager {

    private static volatile BookingManager instance;
    private BookingManager bookingRepository;

    private BookingManager() {
        bookingRepository = BookingManager.getInstance();
    }

    public static BookingManager getInstance() {
        BookingManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (BookingManager.class) {
            if (instance == null) {
                instance = new BookingManager();
            }
            return instance;
        }
    }


}
