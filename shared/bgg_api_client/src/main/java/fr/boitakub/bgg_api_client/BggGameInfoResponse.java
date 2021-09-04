package fr.boitakub.bgg_api_client;

public interface BggGameInfoResponse {

    void onResponse(BggGameInfoResult result);

    void onFailure(Throwable t);

}
