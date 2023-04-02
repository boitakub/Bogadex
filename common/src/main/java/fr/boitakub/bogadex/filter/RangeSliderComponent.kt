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

@Composable
fun RangeSliderComponent(
    @StringRes titleRes: Int,
    filter: FilterRange,
    state: FilterRangeOption,
    updateSelection: (FilterRangeOption) -> Unit = {},
) {
    val minValue = remember { mutableStateOf(state.minValue) }
    val maxValue = remember { mutableStateOf(state.maxValue) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = stringResource(id = titleRes) + " ${minValue.value} - ${maxValue.value} ")
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
