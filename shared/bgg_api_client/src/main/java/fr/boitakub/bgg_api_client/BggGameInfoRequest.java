package fr.boitakub.bgg_api_client;

public class BggGameInfoRequest {

    private final String id;

    public BggGameInfoRequest(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

}
