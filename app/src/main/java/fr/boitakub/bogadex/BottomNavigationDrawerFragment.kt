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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.boitakub.bogadex.common.ui.application.AppViewModel
import fr.boitakub.bogadex.databinding.FragmentBottomsheetBinding

class BottomNavigationDrawerFragment(private val navController: NavController) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomsheetBinding? = null
    private val binding get() = _binding!!
    private val applicationViewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeApplicationState()
    }

    private fun observeApplicationState() {
        lifecycleScope.launchWhenCreated {
            applicationViewModel.applicationState.collect { state ->
                binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                    var dest = state.collection
                    when (menuItem.itemId) {
                        R.id.display_all -> dest = "all"
                        R.id.display_collection -> dest = "collection"
                        R.id.display_wishlist -> dest = "wishlist"
                        R.id.display_solo -> dest = "solo"
                        R.id.display_filler -> dest = "filler"
                    }
                    applicationViewModel.filterCollectionWith(state, dest)

                    val bundle = bundleOf("filter" to dest)
                    navController
                        .navigate(fr.boitakub.boardgame_list.R.id.navigation_boardgame_list, bundle)

                    dismiss()
                    true
                }
            }
        }
    }
}
