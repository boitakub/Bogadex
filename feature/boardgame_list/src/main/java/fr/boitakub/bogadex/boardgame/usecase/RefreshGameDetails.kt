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
package fr.boitakub.bogadex.boardgame.usecase

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.architecture.UseCase
import fr.boitakub.bogadex.boardgame.UpdateBoardGameIntentWorker
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshGameDetails @Inject constructor(@ApplicationContext val context: Context) :
    UseCase<Void?, CollectionItemWithDetails> {

    var scheduledRefresh = SCHEDULED_REFRESH_START

    override fun apply(input: CollectionItemWithDetails): Void? {
        if (input.details == null || input.details!!.isOutdated() && scheduledRefresh < SCHEDULED_REFRESH_MAX) {
            val data = Data.Builder()
            data.putString("bggId", input.item.bggId)

            val request = OneTimeWorkRequestBuilder<UpdateBoardGameIntentWorker>()
                .addTag("Sync")
                .setInputData(data.build())
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()

            scheduledRefresh++
            WorkManager.getInstance(context).enqueue(request)
        }
        return null
    }

    companion object {
        const val SCHEDULED_REFRESH_START = 0
        const val SCHEDULED_REFRESH_MAX = 20
    }
}
