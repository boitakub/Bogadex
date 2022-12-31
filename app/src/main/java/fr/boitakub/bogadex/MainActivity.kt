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
package fr.boitakub.bogadex

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.common.ui.application.AppViewModel
import fr.boitakub.bogadex.databinding.ActivityMainBinding
import fr.boitakub.bogadex.filter.FilterBottomSheetDialog

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()

        observeFilters()
    }

    //region Navigation

    // Setting Up One Time Navigation

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.searchBar.setupWithNavController(navController, binding.root)
        binding.navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(navController, binding.root)
    }

    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //endregion

    // https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/
    private fun showFilterBottomSheetDialog() {
        val filterBottomSheetDialog = FilterBottomSheetDialog(this, appViewModel.filterViewModel)
        filterBottomSheetDialog.show()
    }

    private fun observeFilters() {
        lifecycleScope.launchWhenStarted {
            appViewModel.applicationState.collect { state ->
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
        }
    }

    private fun switchIcon(currentState: Number, item: MenuItem) {
        if (currentState == 1) {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_span_1, theme)
        } else {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_span_3, theme)
        }
    }
}
