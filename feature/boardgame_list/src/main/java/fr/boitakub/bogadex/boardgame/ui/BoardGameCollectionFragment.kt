/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
            appViewModel.applicationState.asLiveData().observe(viewLifecycleOwner, {
                applyApplicationChanges(it)
            })
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
            else -> { // Note the block
                ListCollection(repository)
            }
        }
    }
}
