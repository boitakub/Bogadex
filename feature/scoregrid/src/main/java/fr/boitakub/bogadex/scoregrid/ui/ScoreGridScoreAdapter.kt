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

import android.view.LayoutInflater
import android.view.ViewGroup
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringLineBinding
import fr.boitakub.bogadex.scoregrid.model.Rule
import fr.boitakub.bogadex.scoregrid.model.ScoreGrid

/**
 * This adapter will hold the main column, describing how the score should be evaluated, for each item we must consider
 * that there is multiple players, so we have to instanciate other recycler view on each item. Each item of this class
 * should old an recyclerView.
 *
 * Those recyclerview should be synced horizontally to avoid mistakes
 */
class ScoreGridScoreAdapter(
    val scoreGrid: ScoreGrid,
    val callback: ScoreGridScoringViewHolder.ScoreGridScoringViewHolderCallback
) :
    ScoreGridAdapter<ScoreGridScoringViewHolder>(),
    ScoreGridScoringViewHolder.ScoreGridScoringViewHolderCallback {

    private val ruleList: List<Rule> = scoreGrid.result.keys.toList()
    private var dx: Int = 0
    private var dy: Int = 0

    override fun getItemViewType(position: Int): Int {
        when {
            position == 0 -> {
                return ScoreGridRowType.PLAYER_NAMES.value
            }
            position > ruleList.size -> {
                return ScoreGridRowType.PLAYER_RESULTS.value
            }
            else -> {
                return ScoreGridRowType.PLAYER_RULE_SCORE.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreGridScoringViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScoregridScoringLineBinding.inflate(inflater, parent, false)

        return when (viewType) {
            ScoreGridRowType.PLAYER_NAMES.value -> {
                ScoreGridScoringViewHolder(
                    parent.context,
                    binding,
                    ScoreGridRowType.PLAYER_NAMES,
                    scoreGrid,
                    callback = this,
                )
            }
            ScoreGridRowType.PLAYER_RESULTS.value -> {
                ScoreGridScoringViewHolder(
                    parent.context,
                    binding,
                    ScoreGridRowType.PLAYER_RESULTS,
                    scoreGrid,
                    callback = this,
                )
            }
            else -> {
                ScoreGridScoringViewHolder(
                    parent.context,
                    binding,
                    ScoreGridRowType.PLAYER_RULE_SCORE,
                    scoreGrid,
                    callback = this,
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ScoreGridScoringViewHolder, position: Int) {
        // Skipping first line
        if (position - 1 >= 0 && position - 1 < ruleList.size) {
            holder.update(ruleList[position - 1], dx, dy)
        } else {
            holder.update(null, dx, dy)
        }
    }

    override fun getItemCount(): Int {
        return ruleList.size + 2 // Header ("Players") and Footer ("Total")
    }

    override fun onScroll(dx: Int, dy: Int) {
        this.dx = dx
        this.dy = dy
        callback.onScroll(dx, dy)
    }
}
