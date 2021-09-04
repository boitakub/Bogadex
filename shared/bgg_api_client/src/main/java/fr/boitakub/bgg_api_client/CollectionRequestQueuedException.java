package fr.boitakub.bgg_api_client;

import java.io.IOException;

public class CollectionRequestQueuedException extends IOException {

    public CollectionRequestQueuedException() {
        super("The collection request has been queued you can retry request in a fex minutes.");
    }

}
