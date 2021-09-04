package fr.boitakub.bgg_api_client;

public interface BggSearchResponse {

    void onResponse(BggSearchResult result);

    void onFailure(Throwable t);
}
