package fr.boitakub.bogadex.boardgame

import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.UserCollection
import kotlinx.coroutines.flow.Flow

interface BggServiceHelper {

    fun listCollection(listCollection: BggGameUserCollectionRequest?): Flow<UserCollection>
}
