package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.bgg_api_client.BoardGameHelper
import fr.boitakub.bgg_api_client.Statistics
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.BoardGameBggStatistic
import fr.boitakub.clean_architecture.Mapper
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardGameMapper @Inject constructor() :
    Mapper<BoardGame, Iterable<fr.boitakub.bgg_api_client.BoardGame>> {
    private fun map(source: fr.boitakub.bgg_api_client.BoardGame): BoardGame =
        BoardGame(
            source.id.toString(),
            BoardGameHelper.nameChooser(source.name),
            source.description,
            source.yearPublished,
            source.minplayers,
            source.maxplayers,
            source.playingtime,
            source.minplaytime,
            source.maxplaytime,
            source.image,
            source.thumbnail,
            Date(),
            map(source.statistics)
        )

    override fun map(source: Iterable<fr.boitakub.bgg_api_client.BoardGame>): BoardGame =
        map(source.first())

    private fun map(source: Statistics): BoardGameBggStatistic {
        return BoardGameBggStatistic(
            source.usersrated.toIntOrNull() ?: 0,
            source.average.toFloatOrNull() ?: 0f,
            source.bayesaverage.toFloatOrNull() ?: 0f,
            source.numweights,
            source.averageweight.toFloat(),
            source.owned,
            source.wanting,
            source.wishing,
            source.trading
        )
    }
}
