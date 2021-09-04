package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import java.util.Date

data class BoardGameBggStatistic(
    @ColumnInfo(name = "num_of_rating_votes") val numOfRatingVotes: Int = 0,
    @ColumnInfo(name = "average") val average: Double = 0.0,
    @ColumnInfo(name = "bayes_average") val bayesAverage: Double = 0.0,
    @ColumnInfo(name = "num_of_weight_votes") val numOfWeightVotes: Int = 0,
    @ColumnInfo(name = "average_weight") val averageWeight: Double = 0.0,
    @ColumnInfo(name = "num_of_owns") val numOfOwns: Int = 0,
    @ColumnInfo(name = "num_of_wanted") val numOfWanted: Int = 0,
    @ColumnInfo(name = "num_of_wished") val numOfWished: Int = 0,
    @ColumnInfo(name = "num_of_trading") val numOfTrading: Int = 0,
    @ColumnInfo(name = "creation_date") val creationDate: Date = Date()
)
