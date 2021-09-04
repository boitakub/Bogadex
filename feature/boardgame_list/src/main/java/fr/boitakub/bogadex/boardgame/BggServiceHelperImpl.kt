package fr.boitakub.bogadex.boardgame

import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.bgg_api_client.UserCollection
import javax.inject.Inject

class BggServiceHelperImpl @Inject constructor(private val service: BggService) : BggServiceHelper {

    override suspend fun listCollection(listCollection: BggGameUserCollectionRequest?): UserCollection? = service.userCollection(
        listCollection?.username()
    )
}
