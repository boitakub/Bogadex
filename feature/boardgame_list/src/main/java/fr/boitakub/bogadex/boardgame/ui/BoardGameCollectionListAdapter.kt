package fr.boitakub.bogadex.boardgame.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.load
import fr.boitakub.boardgame_list.databinding.BoardgameListItemBinding
import fr.boitakub.boardgame_list.databinding.BoardgameListItemGridBinding
import fr.boitakub.bogadex.boardgame.UpdateBoardGameIntentWorker
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.common.ui.CommonListAdapter
import fr.boitakub.common.ui.CommonListViewHolder
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

internal class BoardGameCollectionListAdapter(layoutManager: GridLayoutManager) :
    CommonListAdapter<BoardGameItemViewHolder, CollectionItemWithDetails>(layoutManager) {

    override fun getItemViewType(position: Int): Int {
        val spanCount: Int = layoutManager.spanCount
        return if (spanCount == SPAN_COUNT_ONE) {
            VIEW_TYPE_BIG
        } else {
            VIEW_TYPE_SMALL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardGameItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding: ViewBinding = BoardgameListItemBinding.inflate(inflater, parent, false)
        if (viewType == VIEW_TYPE_SMALL) {
            binding = BoardgameListItemGridBinding.inflate(inflater, parent, false)
        }
        return BoardGameItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardGameItemViewHolder, position: Int) {

        val game = itemList[position]
        if (holder.binding is BoardgameListItemBinding) {
            holder.binding.tvTitle.text = game.item.title
            holder.binding.ivCover.load(game.item.coverUrl)
            holder.binding.tvPlayers.text =
                "De " + game.details?.minPlayer.toString() + " à " + game.details?.maxPlayer.toString() + " joueurs"
            holder.binding.tvDuration.text =
                "Entre " + game.details?.minPlayTime.toString() + " et " + game.details?.maxPlayTime.toString() + "minutes"
            holder.binding.tvWeight.text = "Complexité moyenne de " + String.format(
                "%.2f",
                game.details?.statistic?.averageWeight
            ) + "/5"
            holder.binding.tvRating.text = "Note moyenne de " + String.format(
                "%.2f",
                game.details?.statistic?.average
            ) + "/10"
        } else if (holder.binding is BoardgameListItemGridBinding) {
            holder.binding.tvTitle.text = game.item.title
            holder.binding.ivCover.load(game.item.coverUrl)
        }

        if (isOutdated(game)) {
            val data = Data.Builder()
            data.putString("bggId", game.item.bggId)

            val request = OneTimeWorkRequestBuilder<UpdateBoardGameIntentWorker>()
                .addTag("Sync")
                .setInputData(data.build())
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()

            WorkManager.getInstance(holder.binding.root.context).enqueue(request)
        }
    }

    private fun isOutdated(game: CollectionItemWithDetails): Boolean {
        val diffInMillies: Long = abs(Date().time - game.item.updateDate.time)
        val diff: Long = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        if (game.details?.minPlayer == 0) {
            return true
        } else if (diff > 15) {
            return true
        }
        return false
    }

    companion object {
        const val SPAN_COUNT_ONE = 1
        const val SPAN_COUNT_THREE = 3

        const val VIEW_TYPE_SMALL = 1
        const val VIEW_TYPE_BIG = 2
    }
}

class BoardGameItemViewHolder(val binding: ViewBinding) : CommonListViewHolder(binding)
