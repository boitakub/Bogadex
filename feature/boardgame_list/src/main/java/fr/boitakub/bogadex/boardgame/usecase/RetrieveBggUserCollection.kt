package fr.boitakub.bogadex.boardgame.usecase

import android.content.Context
import com.tickaroo.tikxml.TikXml
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.UserCollection
import fr.boitakub.bogadex.boardgame.BggServiceHelper
import fr.boitakub.bogadex.boardgame.BoardGameListDao
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.clean_architecture.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

class RetrieveBggUserCollection @Inject constructor(
    private val service: BggServiceHelper,
    private val database: BoardGameListDao,
    private val mapper: CollectionMapper,
    @ApplicationContext val context: Context,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String> {
    override fun apply(input: String): Flow<List<CollectionItemWithDetails>> {
        return database.getCollectionWithDetails()
            .onEach {
                service.listCollection(BggGameUserCollectionRequest(input)).onEach {
                    database.updateCollection(mapper.map(it).boardgames)
                    writeMockData(input, it)
                }
            }
            .combine(service.listCollection(BggGameUserCollectionRequest(input))) { local, remote ->
                if (local.isEmpty()) {
                    mapper.map(remote).boardgames.map { CollectionItemWithDetails(it, BoardGame()) }
                } else {
                    local
                }
            }
    }

    private fun writeMockData(input: String?, result: UserCollection?) {
        val parser: TikXml = TikXml.Builder()
            .build()
        val file = File(context.filesDir?.path + "/" + File.separator + input + ".xml")
        file.sink().buffer().use { sink ->
            parser.write(sink, result)
        }
    }
}
