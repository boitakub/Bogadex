package fr.boitakub.bgg_api_client;

public class BggSearchRequest {

    private final String query;

    public BggSearchRequest(String query) {
        this.query = query;
    }

    public String query() {
        return query;
    }

}
