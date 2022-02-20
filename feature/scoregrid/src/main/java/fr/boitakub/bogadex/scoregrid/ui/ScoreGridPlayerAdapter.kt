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
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringNameItemBinding
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringPlayerScoreItemBinding
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringPlayerTotalItemBinding
import fr.boitakub.bogadex.scoregrid.model.Player
import fr.boitakub.bogadex.scoregrid.model.Rule
import fr.boitakub.bogadex.scoregrid.model.ScoreGrid

/**
 * This adapter will hold the line, describing how the score should be evaluated, for each item we must consider
 * that there is multiple players, so we have to instanciate other recycler view on each item. Each item of this class
 * should old an recyclerView.
 *
 * Those recyclerview should be synced horizontally to avoid mistakes
 */
class ScoreGridPlayerAdapter(val rowType: ScoreGridRowType, val playerList: List<Player>, val scoreGrid: ScoreGrid) :
    ScoreGridAdapter<ScoreGridViewHolder>() {

    companion object {
        const val VIEW_TYPE_EMPTY_LABEL = 0
        const val VIEW_TYPE_SCORING_LABEL = 1
        const val VIEW_TYPE_PLAYER_NAME = 2
        const val VIEW_TYPE_PLAYER_SCORE = 4
        const val VIEW_TYPE_PLAYER_TOTAL = 5
    }

    var rule: Rule? = null

    override fun getItemViewType(position: Int): Int {
        when (rowType) {
            ScoreGridRowType.PLAYER_NAMES -> {
                return when (position) {
                    0 -> {
                        VIEW_TYPE_EMPTY_LABEL
                    }
                    else -> {
                        VIEW_TYPE_PLAYER_NAME
                    }
                }
            }
            ScoreGridRowType.PLAYER_RULE_SCORE -> {
                return when (position) {
                    0 -> {
                        VIEW_TYPE_SCORING_LABEL
                    }
                    else -> {
                        VIEW_TYPE_PLAYER_SCORE
                    }
                }
            }
            ScoreGridRowType.PLAYER_RESULTS -> {
                return when (position) {
                    0 -> {
                        VIEW_TYPE_EMPTY_LABEL
                    }
                    else -> {
                        VIEW_TYPE_PLAYER_TOTAL
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PLAYER_SCORE -> {
                ScoreGridPlayerScoreViewHolder(ScoregridScoringPlayerScoreItemBinding.inflate(inflater, parent, false))
            }
            VIEW_TYPE_PLAYER_TOTAL -> {
                ScoreGridPlayerTotalViewHolder(ScoregridScoringPlayerTotalItemBinding.inflate(inflater, parent, false))
            }
            else -> {
                // In other case we need just a text field
                ScoreGridTextViewHolder(ScoregridScoringNameItemBinding.inflate(inflater, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: ScoreGridViewHolder, position: Int) {
        var player: Player? = null
        if (position - 1 >= 0 && position - 1 < playerList.size) {
            player = playerList[position - 1] // Skipping first column
        }
        when (rowType) {
            ScoreGridRowType.PLAYER_NAMES -> {
                if (player != null) {
                    (holder as ScoreGridTextViewHolder).setText(player.name)
                    (holder as ScoreGridTextViewHolder).seBackgroundColor(player.color)
                } else {
                    (holder as ScoreGridTextViewHolder).setText("Joueurs")
                }
            }
            ScoreGridRowType.PLAYER_RULE_SCORE -> {
                if (player != null) {
                    scoreGrid.let { (holder as ScoreGridPlayerScoreViewHolder).update(it.result, rule, player) }
                    (holder as ScoreGridPlayerScoreViewHolder).seBackgroundColor(player.color)
                } else {
                    rule?.let { (holder as ScoreGridTextViewHolder).setText(it.label) }
                }
            }
            ScoreGridRowType.PLAYER_RESULTS -> {
                if (player != null) {
                    scoreGrid.let { (holder as ScoreGridPlayerTotalViewHolder).update(it.result, player) }
                    (holder as ScoreGridPlayerTotalViewHolder).seBackgroundColor(player.color)
                } else {
                    (holder as ScoreGridTextViewHolder).setText("Total")
                }
            }
        }
    }

    /**
     * Columns size, should be the same base on players
     */
    override fun getItemCount(): Int {
        return playerList.size + 1 // The first column is for labels
    }

    fun updatePlayerName() {
        // No sure what to do here (maybe more useful with player in parameters)
    }

    fun updatePlayerScoreForRule(rule: Rule?) {
        this.rule = rule
        notifyDataSetChanged()
    }

    fun updatePlayerResult(scoreGrid: ScoreGrid) {
    }
}
