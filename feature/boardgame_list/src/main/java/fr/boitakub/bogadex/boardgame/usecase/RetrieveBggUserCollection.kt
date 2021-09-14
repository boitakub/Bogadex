package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.clean_architecture.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrieveBggUserCollection @Inject constructor(
    private val repository: BoardGameCollectionRepository,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String> {
    override fun apply(input: String): Flow<List<CollectionItemWithDetails>> {
        return repository.get(input)
    }
}
