/*
 * Copyright (c) 2022, Boitakub
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
package fr.boitakub.bogadex.scoregrid.ui

import android.content.Context
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringLineBinding
import fr.boitakub.bogadex.scoregrid.model.Rule
import fr.boitakub.bogadex.scoregrid.model.ScoreGrid

class ScoreGridScoringViewHolder(
    context: Context,
    val binding: ScoregridScoringLineBinding,
    private val rowType: ScoreGridRowType,
    val scoreGrid: ScoreGrid,
    val callback: ScoreGridScoringViewHolderCallback,
) :
    ScoreGridViewHolder(binding) {

    val adapter: ScoreGridPlayerAdapter

    interface ScoreGridScoringViewHolderCallback {
        fun onScroll(dx: Int, dy: Int)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            callback.onScroll(dx, dy)
        }
    }

    fun update(rule: Rule?, dx: Int, dy: Int) {
        when (rowType) {
            ScoreGridRowType.PLAYER_NAMES -> {
                // Update names label
                adapter.updatePlayerName()
            }
            ScoreGridRowType.PLAYER_RULE_SCORE -> {
                // Update rule score (for each player)
                adapter.updatePlayerScoreForRule(rule)
            }
            ScoreGridRowType.PLAYER_RESULTS -> {
                // Update results (for each player)
                adapter.updatePlayerResult(scoreGrid)
            }
        }
        scroll(dx, dy)
    }

    init {
        this.adapter = ScoreGridPlayerAdapter(rowType, scoreGrid.playerList, scoreGrid)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun scroll(dx: Int, dy: Int) {
        binding.recyclerView.removeOnScrollListener(scrollListener)
        binding.recyclerView.scrollBy(dx, dy)
        binding.recyclerView.addOnScrollListener(scrollListener)
    }
}
