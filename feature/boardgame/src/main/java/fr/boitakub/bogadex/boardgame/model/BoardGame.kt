/*
 * Copyright (c) 2021-2022, Boitakub
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
package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.boitakub.architecture.BusinessModel
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Entity(tableName = "boardgame")
data class BoardGame(
    @PrimaryKey @ColumnInfo(name = "bgg_id") var bggId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String? = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "year_published") var yearPublished: Int = 0,
    @ColumnInfo(name = "min_player") var minPlayer: Int = 0,
    @ColumnInfo(name = "max_player") var maxPlayer: Int = 0,
    @ColumnInfo(name = "recommended_player", defaultValue = "") var recommendedPlayers: List<Int> = listOf(),
    @ColumnInfo(name = "play_time") var playTime: Int = 0,
    @ColumnInfo(name = "min_play_time") var minPlayTime: Int = 0,
    @ColumnInfo(name = "max_play_time") var maxPlayTime: Int = 0,
    @ColumnInfo(name = "min_age") var minAge: Int? = 0,
    @ColumnInfo(name = "recommended_age", defaultValue = "") var recommendedAges: List<Int> = listOf(),
    @ColumnInfo(name = "image") var image: String? = "",
    @ColumnInfo(name = "thumbnail") var thumbnail: String? = "",
    @ColumnInfo(name = "update_date") var updateDate: Date = Date(),
    @Embedded var statistic: BoardGameBggStatistic = BoardGameBggStatistic(),
) : BusinessModel {

    fun isOutdated(): Boolean {
        val diffInMillies: Long = abs(Date().time - updateDate.time)
        val diff: Long = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        return (minPlayer == 0 || diff > MAX_DIFF_IN_DAYS)
    }

    companion object {
        const val MAX_DIFF_IN_DAYS = 7
    }
}
