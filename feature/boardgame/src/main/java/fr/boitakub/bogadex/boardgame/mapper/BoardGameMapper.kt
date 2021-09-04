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
            source.yearPublished,
            source.minplayers,
            source.maxplayers,
            source.playingtime,
            source.minplaytime,
            source.maxplaytime,
            "",
            Date(),
            map(source.statistics)
        )

    override fun map(source: Iterable<fr.boitakub.bgg_api_client.BoardGame>): BoardGame =
        map(source.first())

    private fun map(source: Statistics): BoardGameBggStatistic {
        return BoardGameBggStatistic(
            source.usersrated.toIntOrNull() ?: 0,
            source.average.toDoubleOrNull() ?: 0.0,
            source.bayesaverage.toDoubleOrNull() ?: 0.0,
            source.numweights,
            source.averageweight,
            source.owned,
            source.wanting,
            source.wishing,
            source.trading
        )
    }
}
