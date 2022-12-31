/*
 * Copyright (c) 2021-2022, Boitakub
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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.boardgame_list.R
import fr.boitakub.boardgame_list.databinding.BoardgameListFragmentBinding
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionViewModel.Companion.provideFactory
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionFiller
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemSolo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.application.AppViewModel
import fr.boitakub.bogadex.common.ui.application.ApplicationState
import fr.boitakub.bogadex.filter.FilterBottomSheetDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardGameCollectionFragment :
    Fragment(),
    fr.boitakub.architecture.View<BoardGameCollectionViewModel> {

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @Inject
    lateinit var userSettings: UserSettings

    @Inject
    lateinit var imageLoaderViewModelFactory: BoardGameCollectionViewModel.BoardGameCollectionViewModelFactory

    lateinit var binding: BoardgameListFragmentBinding
    private lateinit var navController: NavController

    private val appViewModel: AppViewModel by activityViewModels()

    override val presenter: BoardGameCollectionViewModel by viewModels {
        provideFactory(
            imageLoaderViewModelFactory,
            getCollection(arguments?.getString("filter"))
        )
    }
    private lateinit var adapter: BoardGameCollectionListAdapter
    private var boardGameList: List<CollectionItemWithDetails> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BoardgameListFragmentBinding.inflate(inflater, container, false)
        adapter = BoardGameCollectionListAdapter((binding.recyclerView.layoutManager as GridLayoutManager))
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        setupNavigation()

        // Create a new coroutine in the lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                presenter.gameList.collect {
                    boardGameList = it
                    (binding.recyclerView.adapter as BoardGameCollectionListAdapter).setItems(it)
                }
            }
        }

        presenter.errorMessage.observe(
            viewLifecycleOwner
        ) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        presenter.loading.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

        observeApplicationState()
    }

    // Setting Up One Time Navigation

    private fun setupNavigation() {
        navController = findNavController()
        binding.searchBar.setupWithNavController(navController, binding.root)
        binding.navigationView.setupWithNavController(navController)
    }

    //endregion

    private fun observeApplicationState() {
        lifecycleScope.launchWhenStarted {
            appViewModel.applicationState.collect {
                applyApplicationChanges(it)
            }
        }
    }

    private fun applyApplicationChanges(state: ApplicationState) {
        switchLayout(state.viewType)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_switch_layout -> {
                    appViewModel.switchLayout(state)
                    switchIcon(state.viewType, menuItem)
                    true
                }
                else -> false
            }
        }

        binding.fabFilters.setOnClickListener {
            showFilterBottomSheetDialog()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            var dest = state.collection
            when (menuItem.itemId) {
                R.id.display_all -> dest = "all"
                R.id.display_collection -> dest = "collection"
                R.id.display_wishlist -> dest = "wishlist"
                R.id.display_solo -> dest = "solo"
                R.id.display_filler -> dest = "filler"
            }
            appViewModel.filterCollectionWith(state, dest)

            if (menuItem.itemId != R.id.menu_settings) {
                val bundle = bundleOf("filter" to dest)
                navController
                    .navigate(fr.boitakub.boardgame_list.R.id.navigation_boardgame_list, bundle)
            } else {
                navController.navigate(R.id.navigation_settings)
            }

            true
        }
    }

    private fun switchLayout(state: Int) {
        if (state == 1) {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = 1
        } else {
            (binding.recyclerView.layoutManager as GridLayoutManager).spanCount =
                resources.getInteger(R.integer.game_grid_columns)
        }
    }

    // https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/
    private fun showFilterBottomSheetDialog() {
        val filterBottomSheetDialog = FilterBottomSheetDialog(requireContext(), appViewModel.filterViewModel)
        filterBottomSheetDialog.show()
    }

    private fun switchIcon(currentState: Number, item: MenuItem) {
        if (currentState == 1) {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_span_1, requireActivity().theme)
        } else {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_span_3, requireActivity().theme)
        }
    }

    private fun getCollection(string: String?): ListCollection {
        return when (string) {
            "filler" -> ListCollectionFiller(repository, appViewModel.filterViewModel, userSettings)
            "collection" -> ListCollectionItemOwned(repository, appViewModel.filterViewModel, userSettings)
            "wishlist" -> ListCollectionItemWanted(repository, appViewModel.filterViewModel, userSettings)
            "solo" -> ListCollectionItemSolo(repository, appViewModel.filterViewModel, userSettings)
            else -> { // Note the block
                ListCollection(repository, appViewModel.filterViewModel, userSettings)
            }
        }
    }
}
