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
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BoardGameInfo(
                    titleRes = R.string.players,
                    iconRes = R.drawable.ic_people_group_duotone,
                    minValue = item.details?.minPlayer,
                    maxValue = item.details?.minPlayer,
                )
                item.details?.recommendedPlayers?.let { values ->
                    if (values.isNotEmpty()) {
                        BoardGameRecommendInfo(
                            iconRes = R.drawable.ic_people_group_duotone,
                            recommendedValue = values[0],
                        )
                    }
                }
                BoardGameInfo(
                    titleRes = R.string.players,
                    iconRes = R.drawable.ic_watch_duotone,
                    minValue = item.details?.minPlayTime,
                    maxValue = item.details?.maxPlayTime,
                )
                BoardGameWeightInfo(
                    titleRes = R.string.weight,
                    iconRes = R.drawable.ic_weight,
                    value = item.details?.statistic?.averageWeight,
                    iconSize = 16.dp,
                    tintColor = getWeightColor(averageWeight = item.details?.statistic?.averageWeight),
                )
            }
        }
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
