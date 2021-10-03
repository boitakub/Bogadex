package fr.boitakub.bgg.client;

import java.io.IOException;

public class CollectionRequestQueuedException extends IOException {

    public CollectionRequestQueuedException() {
        super("The collection request has been queued you can retry request in a fex minutes.");
    }

}
