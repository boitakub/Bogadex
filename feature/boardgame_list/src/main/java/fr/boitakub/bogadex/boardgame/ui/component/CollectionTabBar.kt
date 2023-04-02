package fr.boitakub.bogadex.boardgame.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme

@Composable
fun CollectionTabBar(
    collectionAvailableList: List<CollectionType>,
    activeCollection: CollectionType,
    onCollectionChanged: (selectedCollection: CollectionType) -> Unit,
) {
    ScrollableTabRow(selectedTabIndex = collectionAvailableList.indexOf(activeCollection)) {
        collectionAvailableList
            .forEach { item ->
                LeadingIconTab(
                    selected = item == activeCollection,
                    onClick = {
                        onCollectionChanged(item)
                    },
                    icon = {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = item.iconRes),
                            contentDescription = stringResource(id = item.labelRes),
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(item.labelRes),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
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
        CollectionTabBar(
            collectionAvailableList = CollectionType.values().toList(),
            activeCollection = CollectionType.SOLO,
            onCollectionChanged = {},
        )
    }
}

// endregion
