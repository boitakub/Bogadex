package fr.boitakub.bgg_api_client;

public class BggGameUserCollectionRequest {

    private final String username;

    public BggGameUserCollectionRequest(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

}
