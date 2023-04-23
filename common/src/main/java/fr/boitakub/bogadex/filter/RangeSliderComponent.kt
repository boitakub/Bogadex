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
package fr.boitakub.bogadex.filter

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.boitakub.bogadex.common.R
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import java.text.DecimalFormat

@Composable
fun RangeSliderComponent(
    @StringRes titleRes: Int,
    filter: FilterRange,
    state: FilterRangeOption,
    updateSelection: (FilterRangeOption) -> Unit = {},
) {
    val minValue = remember { mutableStateOf(state.minValue) }
    val maxValue = remember { mutableStateOf(state.maxValue) }
    val formatter = DecimalFormat("#,###,##0.0")

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = titleRes) + " ${formatter.format(minValue.value).replace(".0", "")} - ${
            formatter.format(maxValue.value).replace(".0", "")
            } ",
        )
        RangeSlider(
            valueRange = filter.minValueRange..filter.maxValueRange,
            value = minValue.value..maxValue.value,
            steps = filter.steps,
            onValueChange = {
                minValue.value = it.start
                maxValue.value = it.endInclusive
                updateSelection(state.copy(minValue = it.start, maxValue = it.endInclusive))
            },
        )
    }
}

// region Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun RangeSliderComponentPreview() {
    BogadexTheme {
        RangeSliderComponent(
            titleRes = R.string.rs_min_rating,
            filter = FilterRange(
                minValueRange = 0.0f,
                maxValueRange = 100.0f,
                steps = 10,
            ),
            state = FilterRangeOption(
                minValue = 2.0f,
                maxValue = 8.0f,
            ),
        )
    }
}

// endregion
