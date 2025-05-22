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
package fr.boitakub.bogadex.boardgame.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import fr.boitakub.bogadex.boardgame.R
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.common.ui.navigation.RatingBar

@Composable
fun BoardGameDetailScreen(navController: NavHostController, boardGame: BoardGame? = BoardGame()) {
    Scaffold(
        topBar = { TopBar(navigator = navController, title = boardGame?.title ?: "") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
        ) {
            GameDetailHeader(boardGame)

            GameDetailSummary(boardGame)

            GameDetailLinks(boardGame)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TopBar(title: String, navigator: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            navigator.popBackStack()
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(fr.boitakub.bogadex.common.R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = fr.boitakub.bogadex.common.R.string.back),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun GameDetailHeader(boardGame: BoardGame?) {
    Column {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .height(280.dp),
            model = boardGame?.image,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Release Date: ${boardGame?.yearPublished}",
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        RatingBar(
            rating = (boardGame?.statistic?.average ?: 0f) / 2f,
            color = Color.Magenta,
            modifier = Modifier
                .height(15.dp)
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun GameDetailLinks(boardGame: BoardGame?) {
    val context = LocalContext.current
    val resources = context.resources

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 16.dp),
    ) {
        Button(
            modifier = Modifier.padding(4.dp),
            onClick = {
                try {
                    val openUrlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://boardgamegeek.com/boardgame/${boardGame?.bggId}"),
                    )
                    startActivity(context, openUrlIntent, null)
                } catch (e: ActivityNotFoundException) {
                    Log.d("Bogadex", "No application can handle this request.", e)
                }
            },
        ) {
            Text(resources.getString(fr.boitakub.bogadex.common.R.string.link_to_boardgamegeek))
        }
        Button(
            modifier = Modifier.padding(4.dp),
            onClick = {
                try {
                    val openUrlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://app.bgstatsapp.com/addPlay.html?gameId=${boardGame?.bggId}"),
                    )
                    startActivity(context, openUrlIntent, null)
                } catch (e: ActivityNotFoundException) {
                    Log.d("Bogadex", "No application can handle this request.", e)
                }
            },
        ) {
            Text(resources.getString(fr.boitakub.bogadex.common.R.string.link_to_bgstats))
        }
    }
}

@Composable
private fun GameDetailSummary(boardGame: BoardGame?) {
    Column {
        Spacer(modifier = Modifier.height(23.dp))

        Text(
            text = stringResource(R.string.description),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = boardGame?.description ?: "",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        )
    }
}
