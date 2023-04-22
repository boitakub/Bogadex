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
package fr.boitakub.bogadex.boardgame.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.BoardGameBggStatistic
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoardGameDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK
    lateinit var navHostController: NavHostController

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
        composeTestRule.setContent {
            MaterialTheme {
                BoardGameDetailScreen(
                    navController = navHostController,
                    BoardGame()
                )
            }
        }

        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }

    @Test
    fun boardGameDetailScreen_shouldDisplayData() {
        composeTestRule.setContent {
            MaterialTheme {
                BoardGameDetailScreen(
                    navController = navHostController,
                    BoardGame(
                        title = "BoardGame Title",
                        yearPublished = 2005,
                        minPlayer = 1,
                        maxPlayer = 4,
                        statistic = BoardGameBggStatistic(average = 3.06f),
                    ),
                )
            }
        }
        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("BoardGame Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Release Date: 2005").assertIsDisplayed()
    }
}
