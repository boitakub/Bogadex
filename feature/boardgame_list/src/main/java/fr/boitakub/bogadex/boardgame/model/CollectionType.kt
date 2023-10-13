/*
 * Copyright (c) 2023, Boitakub
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
package fr.boitakub.bogadex.boardgame.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.boitakub.boardgame_list.R

enum class CollectionType(
    val key: String,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
) {
    ALL("all", fr.boitakub.bogadex.common.R.string.display_all, R.drawable.ic_clear_all_24),
    MY_COLLECTION("collection", fr.boitakub.bogadex.common.R.string.display_collection, R.drawable.ic_outline_all_inbox_24),
    WISHLIST("wishlist", fr.boitakub.bogadex.common.R.string.display_wishlist, R.drawable.ic_outline_shopping_cart_24),
    SOLO("solo", fr.boitakub.bogadex.common.R.string.display_solo, fr.boitakub.bogadex.common.R.drawable.ic_user_solid),
    FILLER("filler", fr.boitakub.bogadex.common.R.string.display_filler, R.drawable.ic_stopwatch_solid),
    ;

    companion object {
        val DEFAULT = ALL
        fun from(type: String?): CollectionType =
            CollectionType.values().find { it.key == type } ?: DEFAULT
    }
}
