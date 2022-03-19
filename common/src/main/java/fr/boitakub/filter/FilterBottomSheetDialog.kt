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
package fr.boitakub.filter

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import fr.boitakub.common.databinding.MbsFiltersBinding

class FilterBottomSheetDialog(context: Context, filterViewModel: FilterViewModel) : BottomSheetDialog(context) {
    init {
        val binding: MbsFiltersBinding = MbsFiltersBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.rsMinRating.setValues(
            filterViewModel.get().value.minRatingValue,
            filterViewModel.get().value.maxRatingValue
        )
        binding.rsMinRating.addOnChangeListener { slider, _, _ ->
            filterViewModel.mutate(
                Filter(
                    slider.values[0],
                    slider.values[1],
                    filterViewModel.get().value.minWeightValue,
                    filterViewModel.get().value.maxWeightValue
                )
            )
        }
        binding.rsMinWeight.setValues(
            filterViewModel.get().value.minWeightValue,
            filterViewModel.get().value.maxWeightValue
        )
        binding.rsMinWeight.addOnChangeListener { slider, _, _ ->
            filterViewModel.mutate(
                Filter(
                    filterViewModel.get().value.minRatingValue,
                    filterViewModel.get().value.maxRatingValue,
                    slider.values[0],
                    slider.values[1]
                )
            )
        }
    }
}
