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
package fr.boitakub.bogadex

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.work.scheduleRetrieveMissingBoardGameWork
import kotlinx.coroutines.CancellationException

class UpdateExistingBoardGameWork(
    val context: Context,
    val workerParams: WorkerParameters,
    val service: BggService,
    val database: BoardGameDao,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val MAX_GAME_TO_REFRESH_PER_CYCLE = 30
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > 4) {
            return Result.failure()
        }
        Log.d("UpdateExistingBoardGameWork", "Starting job")

        val result = try {
            database.getBoardGameByUpdateDate().filter { item -> item.isOutdated() }.shuffled()
                .take(MAX_GAME_TO_REFRESH_PER_CYCLE)
                .forEach { itemToRefresh ->
                    Log.d("UpdateExistingBoardGameWork", "Schedule retrieving of : " + itemToRefresh.bggId)
                    scheduleRetrieveMissingBoardGameWork(context, itemToRefresh.bggId)
                }
            Result.success()
        } catch (error: IllegalStateException) {
            if (error is CancellationException) {
                Log.d("UpdateExistingBoardGameWork", "Job was Cancelled....")
            }
            Result.retry()
        }
        return result
    }
}
