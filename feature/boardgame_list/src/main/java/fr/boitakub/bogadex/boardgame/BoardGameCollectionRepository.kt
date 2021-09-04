package fr.boitakub.bogadex.boardgame

import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.UserCollection
import fr.boitakub.clean_architecture.Repository
import javax.inject.Inject

class BoardGameCollectionRepository @Inject constructor(
    override val local: BoardGameListDao,
    override val remote: BggServiceHelper
) : Repository {

    suspend fun listCollection(listCollection: BggGameUserCollectionRequest?): UserCollection? =
        remote.listCollection(listCollection)
}
