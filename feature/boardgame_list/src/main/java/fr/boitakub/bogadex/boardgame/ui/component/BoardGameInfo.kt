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

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme

@Composable
fun BoardGameInfo(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    minValue: Int,
    maxValue: Int,
    tintColor: Color = MaterialTheme.colorScheme.secondary,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = stringResource(id = titleRes),
            tint = tintColor,
        )
        Text(
            text = stringResource(id = titleRes, minValue ?: "", maxValue ?: ""),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun BoardGameRecommendInfo(
    @DrawableRes iconRes: Int,
    recommendedValue: Int,
    tintColor: Color = MaterialTheme.colorScheme.secondary,
) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        border = BorderStroke(2.dp, tintColor),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.wrapContentSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = stringResource(id = R.string.recommended_players),
                tint = tintColor,
            )
            Text(
                text = recommendedValue.toString(),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun BoardGameWeightInfo(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    iconSize: Dp = 20.dp,
    value: Float? = null,
    tintColor: Color = MaterialTheme.colorScheme.secondary,
) {
    Surface(
        border = BorderStroke(2.dp, tintColor),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = stringResource(id = titleRes),
                tint = tintColor,
            )
            Text(
                text = stringResource(id = titleRes, value ?: 0.0f),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = tintColor,
            )
        }
    }
}

// region Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewBrowseTabBar() {
    BogadexTheme {
        BoardGameInfo(
            titleRes = R.string.players,
            iconRes = R.drawable.ic_people_group_duotone,
            minValue = 1,
            maxValue = 10,
        )
    }
}

// endregion
