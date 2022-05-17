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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.customview.widget.Openable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.common.ui.application.AppViewModel
import fr.boitakub.bogadex.databinding.ActivityMainBinding
import fr.boitakub.bogadex.filter.FilterBottomSheetDialog
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val drawer = FakeDrawer(supportFragmentManager, BottomNavigationDrawerFragment(navController))
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_boardgame_list
            ),
            drawer
        )
        binding.appBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        binding.appBar.setupWithNavController(navController, appBarConfiguration)
        observeFilters()
    }

    // https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/
    private fun showFilterBottomSheetDialog() {
        val filterBottomSheetDialog = FilterBottomSheetDialog(this, appViewModel.filterViewModel)
        filterBottomSheetDialog.show()
    }

    private fun observeFilters() {
        lifecycleScope.launchWhenStarted {
            appViewModel.applicationState.collect { state ->
                binding.appBar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_switch_layout -> {
                            appViewModel.switchLayout(state)
                            switchIcon(state.viewType, menuItem)
                            true
                        }
                        R.id.menu_settings -> {
                            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_settings)
                            true
                        }
                        else -> false
                    }
                }

                binding.fabFilters.setOnClickListener {
                    showFilterBottomSheetDialog()
                }
            }
        }
    }

    private fun switchIcon(currentState: Number, item: MenuItem) {
        if (currentState == 1) {
            item.icon = ResourcesCompat.getDrawable(resources, fr.boitakub.boardgame_list.R.drawable.ic_span_3, theme)
        } else {
            item.icon = ResourcesCompat.getDrawable(resources, fr.boitakub.boardgame_list.R.drawable.ic_span_1, theme)
        }
    }

    class FakeDrawer(
        private val supportFragmentManager: FragmentManager,
        private val bottomNavDrawerFragment: BottomNavigationDrawerFragment,
        private var isOpen: Boolean = false,
    ) :
        Openable {
        override fun isOpen(): Boolean {
            return isOpen
        }

        override fun open() {
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            isOpen = true
        }

        override fun close() {
            isOpen = false
        }
    }
}
