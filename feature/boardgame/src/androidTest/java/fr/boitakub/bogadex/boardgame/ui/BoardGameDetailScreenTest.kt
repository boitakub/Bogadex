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
package fr.boitakub.bogadex.boardgame.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.BoardGameRepository
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.BoardGameBggStatistic
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoardGameDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK
    lateinit var dao: BoardGameDao

    @MockK
    lateinit var service: BggService

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun boardGameDetailScreen_shouldDisplayEmptyScreen() {
        coEvery { dao.getBoardGameWithId(any()) } answers { BoardGame() }

        composeTestRule.setContent {
            MaterialTheme {
                BoardGameDetailScreen(BoardGameDetailViewModel(BoardGameRepository(dao, service)))
            }
        }

        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun boardGameDetailScreen_shouldDisplayData() {
        every { dao.getBoardGameWithId("123") } answers {
            BoardGame(
                title = "BoardGame Title",
                yearPublished = 2005,
                minPlayer = 1,
                maxPlayer = 4,
                statistic = BoardGameBggStatistic(average = 3.06f)
            )
        }

        val viewModel = BoardGameDetailViewModel(BoardGameRepository(dao, service))
        composeTestRule.setContent {
            MaterialTheme {
                BoardGameDetailScreen(viewModel)
            }
        }
        viewModel.load("123")

        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("BoardGame Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Release Date: 2005").assertIsDisplayed()
    }
}
