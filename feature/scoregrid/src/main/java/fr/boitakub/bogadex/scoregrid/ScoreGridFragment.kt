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
package fr.boitakub.bogadex.scoregrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.scoregrid.ui.ScoreGridScoreAdapter
import fr.boitakub.bogadex.scoregrid.ui.ScoreGridScoringViewHolder
import fr.boitakub.common.databinding.CommonListFragmentBinding

@AndroidEntryPoint
class ScoreGridFragment :
    Fragment(),
    fr.boitakub.architecture.View<ScoreGridViewModel>,
    ScoreGridScoringViewHolder.ScoreGridScoringViewHolderCallback {

    override val presenter: ScoreGridViewModel by viewModels()
    lateinit var binding: CommonListFragmentBinding
    private lateinit var adapter: ScoreGridScoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommonListFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ScoreGridScoreAdapter(presenter.scoreGrid.value, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        return binding.root
    }

    private fun scrollAllRecyclerView(dx: Int, dy: Int) {
        for (i in 0 until binding.recyclerView.childCount) {
            val child = binding.recyclerView.getChildAt(i) as RecyclerView
            if (child !== binding.recyclerView) {
                scroll(child, dx, dy)
            }
        }
    }

    private fun scroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
        recyclerView.scrollBy(dx, dy)
    }

    override fun onScroll(dx: Int, dy: Int) {
        scrollAllRecyclerView(dx, dy)
    }
}
