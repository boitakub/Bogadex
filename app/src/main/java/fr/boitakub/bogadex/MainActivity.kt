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
package fr.boitakub.bogadex

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.customview.widget.Openable
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.databinding.ActivityMainBinding
import fr.boitakub.common.ui.application.AppViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val drawer = FakeDrawer(supportFragmentManager, BottomNavigationDrawerFragment(appViewModel, navController))
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

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_switch_layout -> {
                    appViewModel.switchLayout()
                    switchIcon(appViewModel.applicationState.value.viewType, menuItem)
                    true
                }
                else -> false
            }
        }
        binding.appBar.setupWithNavController(navController, appBarConfiguration)
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
