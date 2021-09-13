package fr.boitakub.bogadex.boardgame

import android.util.Log
import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.bgg_api_client.UserCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BggServiceHelperImpl @Inject constructor(private val service: BggService) : BggServiceHelper {

    override fun listCollection(listCollection: BggGameUserCollectionRequest?): Flow<UserCollection> {
        return flow {
            try {
                val networkResult = service.userCollection(listCollection?.username())
                emit(networkResult)
            } catch (throwable: Throwable) {
                Log.e("Erreur", throwable.message, throwable)
            }
        }
    }
}
