package fr.boitakub.bgg_api_client;

public interface BggUserCollectionResponse {

    void onResponse(UserCollection result);

    void onQueuedRequest(CollectionRequestQueuedException exception);

    void onFailure(Throwable t);

}
