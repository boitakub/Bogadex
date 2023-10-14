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
package fr.boitakub.bogadex.tests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.tickaroo.tikxml.TikXml
import fr.boitakub.bgg.client.BggGameInfoResult
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper
import fr.boitakub.bogadex.boardgame.work.RetrieveMissingBoardGameWork
import fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile
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

class RetrieveMissingBoardGameWorkImplementationTest {
    private lateinit var context: Context

    @MockK
    lateinit var dao: BoardGameDao

    @MockK
    lateinit var service: BggService

    class RetrieveMissingBoardGameWorkFactory(private val dao: BoardGameDao, val service: BggService) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return RetrieveMissingBoardGameWork(
                appContext,
                workerParameters,
                service,
                dao,
                BoardGameMapper()
            )
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

        val worker = TestListenableWorkerBuilder<RetrieveMissingBoardGameWork>(context)
            .setWorkerFactory(RetrieveMissingBoardGameWorkFactory(dao, service))
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

        val worker = TestListenableWorkerBuilder<RetrieveMissingBoardGameWork>(context)
            .setWorkerFactory(RetrieveMissingBoardGameWorkFactory(dao, service))
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

        val worker = TestListenableWorkerBuilder<RetrieveMissingBoardGameWork>(context)
            .setWorkerFactory(RetrieveMissingBoardGameWorkFactory(dao, service))
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
