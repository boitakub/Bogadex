package fr.boitakub.bogadex.boardgame

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.tickaroo.tikxml.TikXml
import fr.boitakub.bgg.client.BggGameInfoResult
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.FileReader.readStringFromFile
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateBoardGameIntentWorkerImplementationTest {
    private lateinit var context: Context

    @MockK
    lateinit var dao: BoardGameDao

    @MockK
    lateinit var service: BggService

    class UpdateBoardGameIntentWorkerFactory(private val dao: BoardGameDao, val service: BggService) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return UpdateBoardGameIntentWorker(appContext, workerParameters, service, dao, BoardGameMapper())
        }
    }

    private fun readStubData(bggId: String): BggGameInfoResult {
        val parser: TikXml = TikXml.Builder()
            .build()
        return parser.read(Buffer().writeUtf8(readStringFromFile("$bggId.xml")), BggGameInfoResult::class.java)
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun updateBoardGameIntentWorker_should_returnSuccess() {
        coEvery { service.boardGame(any()) } answers { readStubData("86246") }

        val worker = TestListenableWorkerBuilder<UpdateBoardGameIntentWorker>(context)
            .setWorkerFactory(UpdateBoardGameIntentWorkerFactory(dao, service))
            .build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))

            coVerify(exactly = 1) { service.boardGame(any()) }
            coVerify(exactly = 1) { dao.insertAllBoardGame(any()) }
        }
    }

    @Test
    fun updateBoardGameIntentWorker_shouldRetryOnce_OnError() {
        coEvery { service.boardGame(any()) } throws IllegalStateException()

        val worker = TestListenableWorkerBuilder<UpdateBoardGameIntentWorker>(context)
            .setWorkerFactory(UpdateBoardGameIntentWorkerFactory(dao, service))
            .build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.retry()))

            coVerify(exactly = 1) { service.boardGame(any()) }
            coVerify(exactly = 0) { dao.insertAllBoardGame(any()) }
        }
    }

    @Test
    fun updateBoardGameIntentWorker_shouldNotRetryTwice_OnTwosError() {
        coEvery { service.boardGame(any()) } throws IllegalStateException()

        val worker = TestListenableWorkerBuilder<UpdateBoardGameIntentWorker>(context)
            .setWorkerFactory(UpdateBoardGameIntentWorkerFactory(dao, service))
            .setRunAttemptCount(3)
            .build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.failure()))

            coVerify(exactly = 0) { service.boardGame(any()) }
            coVerify(exactly = 0) { dao.insertAllBoardGame(any()) }
        }
    }
}
