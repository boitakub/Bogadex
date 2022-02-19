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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import fr.boitakub.bogadex.databinding.ActivityMainBinding
import fr.boitakub.common.ui.Filter
import fr.boitakub.common.ui.application.AppViewModel
import fr.boitakub.common.ui.application.ApplicationState
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val appViewModel: AppViewModel by viewModels()
    private var currentTheme = AppCompatDelegate.MODE_NIGHT_NO

    companion object {
        private const val PREF_THEME_KEY: String = "ui_night_mode"
        private const val PREF_THEME_DAY_VALUE: String = "LIGHT"
        private const val PREF_THEME_NIGHT_VALUE: String = "NIGHT"
        private const val PREF_THEME_AUTO_VALUE: String = "AUTO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(PREF_THEME_KEY, PREF_THEME_DAY_VALUE).toNightMode()

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
        observeLightButton(binding.fabLight)
    }

    private fun observeLightButton(fabLight: FloatingActionButton) {
        applyTheme(fabLight)
        fabLight.setOnClickListener {
            applyTheme(it as FloatingActionButton)
            AppCompatDelegate.setDefaultNightMode(currentTheme)
            PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(PREF_THEME_KEY, currentTheme.toThemeValue())
                .apply()
        }
    }

    private fun applyTheme(button: FloatingActionButton) {
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            button.setImageResource(R.drawable.ic_moon_solid)
            button.contentDescription = getString(R.string.fab_theme_night_desc)
            currentTheme = AppCompatDelegate.MODE_NIGHT_NO
        } else if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            button.setImageResource(R.drawable.ic_sun_solid)
            button.contentDescription = getString(R.string.fab_theme_day_desc)
            currentTheme = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            button.setImageResource(R.drawable.ic_eclipse_solid)
            button.contentDescription = getString(R.string.fab_theme_auto_desc)
            currentTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    // https://www.section.io/engineering-education/bottom-sheet-dialogs-using-android-studio/
    private fun showFilterBottomSheetDialog(state: ApplicationState) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.mbs_filters)
        bottomSheetDialog.findViewById<RangeSlider>(R.id.rs_min_rating)
            ?.addOnChangeListener { slider, _, _ ->
                appViewModel.applyFilter(state, Filter(slider.values[0], slider.values[1]))
            }

        bottomSheetDialog.show()
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
                    showFilterBottomSheetDialog(state)
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

    private fun Int.toThemeValue(): String {
        when (this) {
            AppCompatDelegate.MODE_NIGHT_YES -> PREF_THEME_NIGHT_VALUE
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> PREF_THEME_AUTO_VALUE
        }
        return PREF_THEME_DAY_VALUE
    }

    private fun String?.toNightMode(): Int {
        when (this) {
            PREF_THEME_NIGHT_VALUE -> AppCompatDelegate.MODE_NIGHT_YES
            PREF_THEME_AUTO_VALUE -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        return AppCompatDelegate.MODE_NIGHT_NO
    }
}
