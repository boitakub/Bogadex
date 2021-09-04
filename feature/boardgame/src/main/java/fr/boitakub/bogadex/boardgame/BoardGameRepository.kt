package fr.boitakub.bogadex.boardgame

import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.clean_architecture.Repository
import javax.inject.Inject

class BoardGameRepository @Inject constructor(
    override val local: BoardGameDao,
    override val remote: BggService
) : Repository
