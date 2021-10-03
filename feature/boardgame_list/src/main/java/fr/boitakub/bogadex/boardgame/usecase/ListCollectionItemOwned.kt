package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.architecture.UseCase
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListCollectionItemOwned @Inject constructor(
    private val repository: BoardGameCollectionRepository,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String>, ListCollection(repository) {
    override fun apply(input: String): Flow<List<CollectionItemWithDetails>> {
        return super.apply(input).map { it.filter { boardGame -> boardGame.item.status.own } }
    }
}
