package fr.boitakub.bogadex.boardgame

import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.UserCollection

interface BggServiceHelper {

    suspend fun listCollection(listCollection: BggGameUserCollectionRequest?): UserCollection
}
