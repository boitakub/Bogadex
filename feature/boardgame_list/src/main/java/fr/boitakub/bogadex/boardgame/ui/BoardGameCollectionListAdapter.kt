/*
 * Copyright (c) 2021, Boitakub
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
import fr.boitakub.common.ui.Filter

internal class BoardGameCollectionListAdapter(layoutManager: GridLayoutManager) :
    CommonListAdapter<BoardGameItemViewHolder, CollectionItemWithDetails>(layoutManager) {

    open var isGridMode: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (isGridMode) {
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
            holder.binding.tvDescription.text = game.details?.description ?: ""
            holder.binding.ivCover.load(game.details?.image)
        }
    }

    companion object {
        const val SPAN_COUNT_ONE = 1

        const val VIEW_TYPE_SMALL = 1
        const val VIEW_TYPE_BIG = 2
    }

    override fun applyFilter(fullList: List<CollectionItemWithDetails>?, filters: Filter) {
        fullList?.let {
            setItems(
                it.filter { item ->
                    item.averageRating() >= filters.minRatingValue &&
                        item.averageRating() <= filters.maxRatingValue
                }
            )
        }
    }
}

class BoardGameItemViewHolder(val binding: ViewBinding) : CommonListViewHolder(binding)
