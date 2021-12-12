package fr.steve.leroy.go4lunch.event;

import fr.steve.leroy.go4lunch.model.Workmate;

/**
 * Created by Steve LEROY on 05/12/2021.
 */
public class OpenDetailEvent {

    /**
     * Workmate to open
     */
    public Workmate workmate;


    /**
     * Constructor.
     *
     * @param workmate
     */
    public OpenDetailEvent(Workmate workmate) {
        this.workmate = workmate;
    }

}
