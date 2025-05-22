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
package fr.boitakub.bogadex.tests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bgg.client.UserCollection
import fr.boitakub.bgg.client.XmlSanitizingInterceptor
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.BoardGameListDao
import fr.boitakub.bogadex.boardgame.mapper.BoardGameStatusMapper
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.InputStreamReader

class RetrieveBoardGameCollectionImplementationTest {
    private lateinit var context: Context

    @MockK
    lateinit var dao: BoardGameListDao

    @MockK
    lateinit var service: BggService

    private fun readStubData(bggId: String): UserCollection {
        val xml = XML()
        val raw = readStringFromFile("$bggId.xml")
        val sanitized = XmlSanitizingInterceptor().sanitizeXml(raw)
        return xml.decodeFromString(sanitized)
    }

    private fun readStringFromFile(fileName: String): String {
        val inputStream =
            InstrumentationRegistry.getInstrumentation().context.assets.open(
                "data/$fileName",
            )
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun retrieveCollection_should_returnSuccess() {
        coEvery { service.userCollection(any()) } answers { readStubData("collection") }
        coEvery { dao.collectionWithDetailsFlow() } answers { emptyFlow() }
        coEvery { dao.insertAllCollectionItem(any()) } returns Unit

        val boardGameCollectionRepository = BoardGameCollectionRepository(
            context,
            dao,
            service,
            CollectionMapper(BoardGameStatusMapper()),
        )

        runBlocking {
            val userCollection = boardGameCollectionRepository.getRemote("user")

            assertThat(userCollection.totalitems, `is`("574"))
            assertThat(userCollection.games.size, `is`(55))
        }
    }
}
