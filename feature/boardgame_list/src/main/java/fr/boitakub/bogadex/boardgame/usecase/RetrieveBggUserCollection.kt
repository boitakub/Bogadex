package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bogadex.boardgame.BggServiceHelper
import fr.boitakub.bogadex.boardgame.BoardGameListDao
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.clean_architecture.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrieveBggUserCollection @Inject constructor(
    private val service: BggServiceHelper,
    private val database: BoardGameListDao,
    private val mapper: CollectionMapper
) : UseCase<Flow<List<CollectionItem>>, String> {
    override fun apply(input: String): Flow<List<CollectionItem>> {
        return flow {
            emit(database.getAll())
            val networkResult = service.listCollection(BggGameUserCollectionRequest(input))
            val mappedValues = networkResult?.let { mapper.map(it) }
            mappedValues?.let { database.updateCollection(it.boardgames) }
            emit(database.getAll())
        }
    }
}
