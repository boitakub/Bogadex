package fr.boitakub.bogadex.boardgame.usecase

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.bogadex.boardgame.UpdateBoardGameIntentWorker
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.clean_architecture.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshGameDetails @Inject constructor(@ApplicationContext val context: Context) :
    UseCase<Void?, CollectionItemWithDetails> {

    private val maxScheduledRefresh = 40
    var scheduledRefresh: Long = 0

    override fun apply(input: CollectionItemWithDetails): Void? {
        if (input.details == null || input.details!!.isOutdated() && scheduledRefresh < maxScheduledRefresh) {
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
}
