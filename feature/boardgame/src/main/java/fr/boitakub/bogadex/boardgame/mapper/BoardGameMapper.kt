/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.architecture.Mapper
import fr.boitakub.bgg.client.BoardGameHelper
import fr.boitakub.bgg.client.Statistics
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.BoardGameBggStatistic
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardGameMapper @Inject constructor() :
    Mapper<BoardGame, Iterable<fr.boitakub.bgg.client.BoardGame>> {
    private fun map(source: fr.boitakub.bgg.client.BoardGame): BoardGame =
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

    override fun map(source: Iterable<fr.boitakub.bgg.client.BoardGame>): BoardGame =
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
