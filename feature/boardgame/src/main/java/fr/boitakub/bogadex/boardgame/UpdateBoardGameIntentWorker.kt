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
            if (BuildConfig.DEBUG) writeMockData(bggId, networkResult)
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
