package fr.boitakub.bogadex.boardgame.usecase

import android.content.Context
import com.tickaroo.tikxml.TikXml
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.bgg_api_client.BggGameUserCollectionRequest
import fr.boitakub.bgg_api_client.UserCollection
import fr.boitakub.bogadex.boardgame.BggServiceHelper
import fr.boitakub.bogadex.boardgame.BoardGameListDao
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.clean_architecture.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

class RetrieveBggUserCollection @Inject constructor(
    private val service: BggServiceHelper,
    private val database: BoardGameListDao,
    private val mapper: CollectionMapper,
    @ApplicationContext val context: Context,
) : UseCase<Flow<List<CollectionItem>>, String> {
    override fun apply(input: String): Flow<List<CollectionItem>> {
        return flow {
            emit(database.getAll())
            val networkResult = service.listCollection(BggGameUserCollectionRequest(input))
            writeMockData(input, networkResult)
            val mappedValues = networkResult?.let { mapper.map(it) }
            mappedValues?.let { database.updateCollection(it.boardgames) }
            emit(database.getAll())
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
