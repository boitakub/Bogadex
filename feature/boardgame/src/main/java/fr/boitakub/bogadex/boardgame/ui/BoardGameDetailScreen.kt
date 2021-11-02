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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import fr.boitakub.bogadex.boardgame.R
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.common.ui.navigation.RatingBar

@Composable
fun BoardGameDetailScreen(
    viewModel: BoardGameDetailViewModel
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {

        MovieDetailHeader(viewModel)

        MovieDetailSummary(viewModel)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MovieDetailHeader(
    viewModel: BoardGameDetailViewModel
) {
    val boardGame: BoardGame? by viewModel.boardgame.collectAsState(initial = null)

    Column {

        Image(
            painter = rememberImagePainter(boardGame?.image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .height(280.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = boardGame?.title ?: "",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Release Date: ${boardGame?.yearPublished}",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        RatingBar(
            rating = (boardGame?.statistic?.average ?: 0f) / 2f,
            color = Color.Magenta,
            modifier = Modifier
                .height(15.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun MovieDetailSummary(
    viewModel: BoardGameDetailViewModel
) {
    val boardGame: BoardGame? by viewModel.boardgame.collectAsState(initial = null)

    Column {

        Spacer(modifier = Modifier.height(23.dp))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = boardGame?.description ?: "",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )
    }
}
