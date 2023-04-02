package fr.boitakub.bogadex.common.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.boitakub.bogadex.common.R
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme

@Composable
fun LoadingComponent() {
    Column(
        modifier = Modifier.defaultMinSize(320.dp, 200.dp).padding(16.dp).wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.loading),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )
    }
}

//region Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LoadingViewLight() {
    BogadexTheme {
        LoadingComponent()
    }
}

//endregion
