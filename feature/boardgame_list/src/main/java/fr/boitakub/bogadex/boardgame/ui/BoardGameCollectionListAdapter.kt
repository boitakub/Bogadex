package fr.boitakub.bogadex.boardgame.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import coil.load
import fr.boitakub.boardgame_list.databinding.BoardgameListItemBinding
import fr.boitakub.boardgame_list.databinding.BoardgameListItemGridBinding
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.common.ui.CommonListAdapter
import fr.boitakub.common.ui.CommonListViewHolder

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
    }

    companion object {
        const val SPAN_COUNT_ONE = 1
        const val SPAN_COUNT_THREE = 3

        const val VIEW_TYPE_SMALL = 1
        const val VIEW_TYPE_BIG = 2
    }
}

class BoardGameItemViewHolder(val binding: ViewBinding) : CommonListViewHolder(binding)
