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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionViewModel.Companion.provideFactory
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemSolo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.common.databinding.CommonListFragmentBinding
import fr.boitakub.common.ui.application.AppViewModel
import fr.boitakub.common.ui.application.ApplicationState
import javax.inject.Inject

@AndroidEntryPoint
class BoardGameCollectionFragment :
    Fragment(),
    fr.boitakub.architecture.View<BoardGameCollectionViewModel> {

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @Inject
    lateinit var imageLoaderViewModelFactory: BoardGameCollectionViewModel.BoardGameCollectionViewModelFactory

    lateinit var binding: CommonListFragmentBinding
    private val appViewModel: AppViewModel by activityViewModels()
    override val presenter: BoardGameCollectionViewModel by viewModels {
        provideFactory(
            imageLoaderViewModelFactory,
            getCollection(arguments?.getString("filter"))
        )
    }
    private lateinit var adapter: BoardGameCollectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommonListFragmentBinding.inflate(inflater, container, false)
        adapter =
            BoardGameCollectionListAdapter((binding.recyclerView.layoutManager as GridLayoutManager))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            appViewModel.applicationState.asLiveData().observe(
                viewLifecycleOwner,
                {
                    applyApplicationChanges(it)
                }
            )
        }

        presenter.gameList.observe(
            viewLifecycleOwner,
            {
                adapter.setItems(it)
            }
        )

        presenter.errorMessage.observe(
            viewLifecycleOwner,
            {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        )

        presenter.loading.observe(
            viewLifecycleOwner,
            {
                if (it) {
                    binding.pbLoading.visibility = View.VISIBLE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            }
        )
    }

    private fun applyApplicationChanges(data: ApplicationState) {
        switchLayout(data.viewType)
    }

    private fun switchLayout(state: Int) {
        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = state
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    private fun getCollection(string: String?): ListCollection {
        return when (string) {
            "collection" -> ListCollectionItemOwned(repository)
            "wishlist" -> ListCollectionItemWanted(repository)
            "solo" -> ListCollectionItemSolo(repository)
            else -> { // Note the block
                ListCollection(repository)
            }
        }
    }
}
