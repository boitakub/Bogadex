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
        const val SCHEDULED_REFRESH_MAX = 40
    }
}
