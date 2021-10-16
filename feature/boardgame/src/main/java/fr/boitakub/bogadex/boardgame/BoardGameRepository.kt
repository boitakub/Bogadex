package fr.boitakub.bogadex.boardgame

import fr.boitakub.architecture.Repository
import fr.boitakub.bgg.client.BggService
import javax.inject.Inject

class BoardGameRepository @Inject constructor(
    override val local: BoardGameDao,
    override val remote: BggService
) : Repository {

    fun loadBoardGameById(id: String) = local.getBoardGameWithId(id)
}
