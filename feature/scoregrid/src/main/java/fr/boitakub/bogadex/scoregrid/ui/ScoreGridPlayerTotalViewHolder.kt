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

import androidx.annotation.ColorInt
import fr.boitakub.bogadex.scoregrid.databinding.ScoregridScoringPlayerTotalItemBinding
import fr.boitakub.bogadex.scoregrid.model.NumericScore
import fr.boitakub.bogadex.scoregrid.model.Player
import fr.boitakub.bogadex.scoregrid.model.Rule
import fr.boitakub.bogadex.scoregrid.model.Score

class ScoreGridPlayerTotalViewHolder(val binding: ScoregridScoringPlayerTotalItemBinding) :
    ScoreGridViewHolder(binding) {

    fun update(scores: Map<Rule, Map<Player, Score<*>>>, player: Player) {
        var result = 0
        scores.forEach { result += (it.value[player] as NumericScore).value }

        binding.tvValue.text = result.toString()
    }

    fun seBackgroundColor(@ColorInt color: Int) {
        binding.tvValue.setBackgroundColor(color)
    }
}
