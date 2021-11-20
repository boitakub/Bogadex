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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import fr.boitakub.common.ui.application.AppViewModel

class BottomNavigationDrawerFragment(
    private val applicationViewModel: AppViewModel,
    private val navController: NavController
) : BottomSheetDialogFragment() {

    lateinit var navigationView: NavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        navigationView = view.findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.display_all -> applicationViewModel.filterCollectionWith("all")
                R.id.display_collection -> applicationViewModel.filterCollectionWith("collection")
                R.id.display_wishlist -> applicationViewModel.filterCollectionWith("wishlist")
            }
            val bundle = bundleOf("filter" to applicationViewModel.applicationState.value.collection)
            navController
                .navigate(fr.boitakub.boardgame_list.R.id.navigation_boardgame_list, bundle)
            dismiss()
            true
        }
        return view
    }
}
