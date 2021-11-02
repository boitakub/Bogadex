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
package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import java.util.Date

data class BoardGameBggStatistic(
    @ColumnInfo(name = "num_of_rating_votes") val numOfRatingVotes: Int = 0,
    @ColumnInfo(name = "average") val average: Float = 0f,
    @ColumnInfo(name = "bayes_average") val bayesAverage: Float = 0f,
    @ColumnInfo(name = "num_of_weight_votes") val numOfWeightVotes: Int = 0,
    @ColumnInfo(name = "average_weight") val averageWeight: Float = 0f,
    @ColumnInfo(name = "num_of_owns") val numOfOwns: Int = 0,
    @ColumnInfo(name = "num_of_wanted") val numOfWanted: Int = 0,
    @ColumnInfo(name = "num_of_wished") val numOfWished: Int = 0,
    @ColumnInfo(name = "num_of_trading") val numOfTrading: Int = 0,
    @ColumnInfo(name = "creation_date") val creationDate: Date = Date()
)
