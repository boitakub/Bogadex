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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BoardGameListItem(
    item: CollectionItemWithDetails,
    onClick: (CollectionItemWithDetails) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onClick(item)
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.TopStart) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                    .size(50.dp),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = item.item.coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.2f,
                )
                AsyncImage(
                    modifier = Modifier.padding(4.dp),
                    model = item.item.coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }
            if (item.details != null && item.details?.type == "boardgameexpansion") {
                Icon(
                    modifier = Modifier.padding(4.dp).size(16.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_puzzle_piece_solid),
                    contentDescription = stringResource(id = fr.boitakub.bogadex.common.R.string.expansion_icon),
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Column(
            modifier = Modifier.weight(1.0f),
        ) {
            item.item.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            GameInfoSection(item)
        }
        RatingSection(item)
    }
}

@Composable
private fun GameInfoSection(item: CollectionItemWithDetails) {
    Row(
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
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.5.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
        item.details?.recommendedPlayers?.let { values ->
            if (values.isNotEmpty()) {
                BoardGameRecommendInfo(
                    iconRes = R.drawable.ic_people_group_duotone,
                    recommendedValue = values[0],
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.5.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
        item.details?.let {
            BoardGameInfo(
                titleRes = R.string.players,
                iconRes = R.drawable.ic_watch_duotone,
                minValue = it.minPlayTime,
                maxValue = it.maxPlayTime,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.5.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Composable
private fun RatingSection(item: CollectionItemWithDetails) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BoardGameWeightInfo(
            titleRes = R.string.weight,
            iconRes = R.drawable.ic_weight,
            value = item.details?.statistic?.averageWeight,
            tintColor = getWeightColor(averageWeight = item.details?.statistic?.averageWeight),
        )
        item.details?.statistic?.let {
            Surface(
                modifier = Modifier.padding(horizontal = 8.dp),
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
    }
}

@Composable
fun getWeightColor(averageWeight: Float?, isLight: Boolean = isSystemInDarkTheme()): Color {
    if (averageWeight == null) {
        return MaterialTheme.colorScheme.secondary
    }
    return when (averageWeight) {
        in 0F..1.2F -> colorResource(R.color.weight_very_easy)
        in 1.2F..2.4F -> colorResource(R.color.weight_easy)
        in 2.4F..3.0F -> colorResource(R.color.weight_normal)
        in 3.0F..3.9F -> colorResource(R.color.weight_hard)
        in 3.9F..5F -> if (isLight) colorResource(android.R.color.white) else colorResource(android.R.color.black)
        else -> if (isLight) colorResource(android.R.color.white) else colorResource(android.R.color.black)
    }
}

class PolyShape(private val sides: Int, private val radius: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(path = Path().apply { polygon(sides, radius, size.center) })
    }
}

fun Path.polygon(sides: Int, radius: Float, center: Offset) {
    val angle = 2.0 * Math.PI / sides
    moveTo(
        x = center.x + (radius * cos(0.0)).toFloat(),
        y = center.y + (radius * sin(0.0)).toFloat(),
    )
    for (i in 1 until sides) {
        lineTo(
            x = center.x + (radius * cos(angle * i)).toFloat(),
            y = center.y + (radius * sin(angle * i)).toFloat(),
        )
    }
    close()
}
