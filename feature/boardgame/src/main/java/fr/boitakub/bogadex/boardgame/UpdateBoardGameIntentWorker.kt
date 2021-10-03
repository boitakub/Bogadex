package fr.boitakub.bogadex.boardgame

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tickaroo.tikxml.TikXml
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import fr.boitakub.bgg.client.BggGameInfoResult
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper
import kotlinx.coroutines.CancellationException
import okio.buffer
import okio.sink
import java.io.File

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

        val result = try {
            val bggId = inputData.getString("bggId")
            val networkResult = service.boardGame(bggId)
            writeMockData(bggId, networkResult)
            val mappedValues = networkResult?.let { mapper.map(it.games) }
            mappedValues?.let { database.insertAllBoardGame(listOf(it)) }
            Result.success()
        } catch (error: IllegalStateException) {
            if (error is CancellationException) {
                Log.d("UpdateBoardGameIntentWorker", "Job was Cancelled....")
            }
            Result.retry()
        }
        return result
    }

    private fun writeMockData(bggId: String?, result: BggGameInfoResult?) {
        val parser: TikXml = TikXml.Builder()
            .build()
        val file = File(context.filesDir?.path + "/" + File.separator + bggId + ".xml")
        file.sink().buffer().use { sink ->
            parser.write(sink, result)
        }
    }
}
