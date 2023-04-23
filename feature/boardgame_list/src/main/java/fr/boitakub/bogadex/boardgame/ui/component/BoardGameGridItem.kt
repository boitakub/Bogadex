/*
 * Copyright (c) 2023, Boitakub
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
package fr.boitakub.bogadex.boardgame.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails

@Composable
fun BoardGameGridItem(
    item: CollectionItemWithDetails,
    onClick: (CollectionItemWithDetails) -> Unit,
) {
    BogadexCard {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(onClick = {
                    onClick(item)
                }),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item.item.title?.let {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        text = it,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            item.details?.let {
                AsyncImage(
                    modifier = Modifier.padding(16.dp).size(200.dp),
                    model = it.image,
                    contentDescription = null,
                )
            }
            item.details?.statistic?.let {
                Surface(
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = PolyShape(it.average.toInt(), 80.0f),
                    shadowElevation = 6.dp,
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "%.1f".format(it.average),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            GameInfoSection(item)
        }
    }
}

@Composable
private fun GameInfoSection(item: CollectionItemWithDetails) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item.details?.let {
            BoardGameInfo(
                titleRes = R.string.players,
                iconRes = R.drawable.ic_people_group_duotone,
                minValue = it.minPlayer,
                maxValue = it.maxPlayer,
            )
        }
        item.details?.recommendedPlayers?.let { values ->
            if (values.isNotEmpty()) {
                BoardGameRecommendInfo(
                    iconRes = R.drawable.ic_people_group_duotone,
                    recommendedValue = values[0],
                )
            }
        }
        item.details?.let {
            BoardGameInfo(
                titleRes = R.string.players,
                iconRes = R.drawable.ic_watch_duotone,
                minValue = it.minPlayTime,
                maxValue = it.maxPlayTime,
            )
        }
        BoardGameWeightInfo(
            titleRes = R.string.weight,
            iconRes = R.drawable.ic_weight,
            value = item.details?.statistic?.averageWeight,
            iconSize = 16.dp,
            tintColor = getWeightColor(averageWeight = item.details?.statistic?.averageWeight),
        )
    }
}

@Composable
fun BogadexCard(
    modifier: Modifier = Modifier.wrapContentSize(),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp,
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
    ) {
        content()
    }
}
