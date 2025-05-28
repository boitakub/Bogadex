/*
 * Copyright (c) 2021-2025, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.architecture.Mapper
import fr.boitakub.bgg.client.BoardGameHelper
import fr.boitakub.bgg.client.Poll
import fr.boitakub.bgg.client.PollResult
import fr.boitakub.bgg.client.Statistics
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.BoardGameBggStatistic
import java.util.Date

class BoardGameMapper : Mapper<BoardGame, Iterable<fr.boitakub.bgg.client.BoardGame>> {

    companion object {
        const val BEST_AT_VALUE = "Best"
        const val RECOMMENDED_AT_VALUE = "Recommended"
        const val NOT_RECOMMENDED_AT_VALUE = "Not Recommended"
    }

    private fun map(source: fr.boitakub.bgg.client.BoardGame): BoardGame = BoardGame(
        source.id.toString(),
        BoardGameHelper.nameChooser(source.name),
        source.description,
        source.type.orEmpty(),
        source.yearPublished.value,
        source.minplayers.value,
        source.maxplayers.value,
        mapRecommendedPlayer(source.polls, source.maxplayers.value),
        source.playingtime.value,
        source.minplaytime.value,
        source.maxplaytime.value,
        source.minage.value,
        mapRecommendedAge(source.polls),
        source.image,
        source.thumbnail,
        Date(),
        map(source.statistics),
    )

    override fun map(source: Iterable<fr.boitakub.bgg.client.BoardGame>): BoardGame = map(source.first())

    private fun map(source: Statistics): BoardGameBggStatistic = BoardGameBggStatistic(
        source.ratings.usersrated.value,
        source.ratings.average.value,
        source.ratings.bayesaverage.value,
        source.ratings.numweights.value,
        source.ratings.averageweight.value,
        source.ratings.owned.value,
        source.ratings.wanting.value,
        source.ratings.wishing.value,
        source.ratings.trading.value,
    )

    private fun mapRecommendedPlayer(source: List<Poll>?, maxPlayers: Int, minVote: Int = 10): List<Int> {
        val intermediate = mutableMapOf<Int?, Int>()
        source?.forEach { poll ->
            if (poll.name == "suggested_numplayers" && poll.totalvotes > minVote) {
                poll.results?.forEach { pollResult ->
                    mapNumPlayers(pollResult, maxPlayers, intermediate)
                }
            }
        }
        return intermediate.toList().sortedByDescending { it.second }.map { it.first }.filterNotNull()
    }

    private fun mapNumPlayers(pollResult: PollResult, maxPlayers: Int, intermediate: MutableMap<Int?, Int>) {
        val numPlayers = pollResult.numplayers.ifPlus(maxPlayers)
        val bestScore = pollResult.results?.singleOrNull { it.value == BEST_AT_VALUE }?.numvotes
        val recommendedScore =
            pollResult.results?.singleOrNull { it.value == RECOMMENDED_AT_VALUE }?.numvotes
        val notRecommendedScore =
            pollResult.results?.singleOrNull { it.value == NOT_RECOMMENDED_AT_VALUE }?.numvotes

        var score = 0
        if (bestScore != null) {
            score += (bestScore * 5)
        }
        if (recommendedScore != null) {
            score += (recommendedScore * 3)
        }
        if (notRecommendedScore != null) {
            score += (notRecommendedScore * -1)
        }
        intermediate[numPlayers!!.toIntOrNull()] = score
    }

    private fun mapRecommendedAge(source: List<Poll>?, minVote: Int = 10): List<Int> {
        var table = listOf<Int>()
        source?.forEach { poll ->
            if (poll.name == "suggested_playerage" && poll.totalvotes > minVote) {
                table = mapPlayerAge(poll, table)
            }
        }
        return table
    }

    private fun mapPlayerAge(poll: Poll, table: List<Int>): List<Int> {
        var table1 = table
        poll.results?.forEach { result ->
            result.results?.let { results ->
                table1 = results
                    .sortedByDescending { it.numvotes }
                    .filter { !it.value.isNullOrBlank() }
                    .mapNotNull { tryParse(it.value) }
            }
        }
        return table1
    }

    private fun tryParse(stringValue: String?): Int? = stringValue?.toIntOrNull()
}

private fun String?.ifPlus(maxPlayers: Int): String? {
    if (this != null && this.contains("+")) {
        return maxPlayers.toString()
    }
    return this
}
