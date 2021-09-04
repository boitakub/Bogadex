package fr.boitakub.bogadex.boardgame

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper

@HiltWorker
class UpdateBoardGameIntentWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    val service: BggService,
    val database: BoardGameDao,
    val mapper: BoardGameMapper
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        if (runAttemptCount > 2) {
            return Result.failure()
        }

        return try {
            val bggId = inputData.getString("bggId")
            val networkResult = service.boardGame(bggId)
            val mappedValues = networkResult?.let { mapper.map(it.games) }
            mappedValues?.let { database.insertAllBoardGame(listOf(it)) }
            Result.success()
        } catch (error: Throwable) {
            Result.retry()
        }
    }
}
