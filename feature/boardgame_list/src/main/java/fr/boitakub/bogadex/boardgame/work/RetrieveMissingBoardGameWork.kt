/*
 * Copyright (c) 2021-2023, Boitakub
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
package fr.boitakub.bogadex.boardgame.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tickaroo.tikxml.TikXml
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import fr.boitakub.bgg.client.BggGameInfoResult
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.BuildConfig
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper
import okio.buffer
import okio.sink
import java.io.File
import java.util.concurrent.CancellationException

@HiltWorker
class RetrieveMissingBoardGameWork @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    val service: BggService,
    val database: BoardGameDao,
    val mapper: BoardGameMapper,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val MAX_RETRIEVE_REQUEST_PER_SESSION = 30
    }

    override suspend fun doWork(): Result {
        Log.d("RetrieveMissingBoardGameWork", "Checking")
        if (runAttemptCount > 4) {
            return Result.failure()
        }
        Log.d("RetrieveMissingBoardGameWork", "Starting job")

        val result = try {
            val bggId = inputData.getString("bggId")
            val networkResult = service.boardGame(bggId)
            Log.d("RetrieveMissingBoardGameWork", "Retrieving : $bggId")
            val mappedValues = networkResult?.let { mapper.map(it.games) }
            if (BuildConfig.DEBUG) writeMockData(bggId, networkResult)
            mappedValues?.let { database.insertAllBoardGame(listOf(it)) }
            Result.success()
        } catch (error: IllegalStateException) {
            if (error is CancellationException) {
                Log.d("RetrieveMissingBoardGameWork", "Job was Cancelled....")
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

fun scheduleRetrieveMissingBoardGameWork(context: Context, bggId: String) {
    val data = Data.Builder()
    data.putString("bggId", bggId)

    // define constraints
    val myConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val refreshCpnWork = OneTimeWorkRequestBuilder<RetrieveMissingBoardGameWork>()
        .setConstraints(myConstraints)
        .setInputData(data.build())
        .addTag("RetrieveMissingBoardGameWork$bggId")
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "RetrieveMissingBoardGameWork$bggId",
        ExistingWorkPolicy.REPLACE,
        refreshCpnWork,
    )
}
