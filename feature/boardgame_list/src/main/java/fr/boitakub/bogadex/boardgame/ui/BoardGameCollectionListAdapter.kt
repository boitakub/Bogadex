package fr.boitakub.bogadex.boardgame.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import coil.load
import fr.boitakub.boardgame_list.R
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
        holder.binding.root.setOnClickListener {
            val bundle = bundleOf("bggId" to game.item.bggId, "title" to game.item.title)
            holder.binding.root.findNavController()
                .navigate(R.id.navigation_boardgame_details, bundle)
        }
        if (holder.binding is BoardgameListItemBinding) {
            holder.binding.tvTitle.text = game.item.title
            holder.binding.ivCover.load(game.item.coverUrl)
            holder.binding.tvPlayers.text = holder.itemView.context.getString(
                R.string.players,
                game.details?.minPlayer,
                game.details?.maxPlayer
            )
            holder.binding.tvDuration.text = holder.itemView.context.getString(
                R.string.duration,
                game.details?.minPlayTime,
                game.details?.maxPlayTime
            )
            holder.binding.tvWeight.text = holder.itemView.context.getString(
                R.string.weight,
                game.details?.statistic?.averageWeight
            )
            holder.binding.pvRating.noOfSides = game.details?.statistic?.average?.toInt() ?: 0
            holder.binding.tvShapeRating.text = "${"%.1f".format(game.details?.statistic?.average)}"
        } else if (holder.binding is BoardgameListItemGridBinding) {
            holder.binding.tvTitle.text = game.item.title
            holder.binding.ivCover.load(game.details?.image)
        }
    }

    companion object {
        const val SPAN_COUNT_ONE = 1
        const val SPAN_COUNT_THREE = 2

        const val VIEW_TYPE_SMALL = 1
        const val VIEW_TYPE_BIG = 2
    }
}

class BoardGameItemViewHolder(val binding: ViewBinding) : CommonListViewHolder(binding)
