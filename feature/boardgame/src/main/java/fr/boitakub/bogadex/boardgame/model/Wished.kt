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
package fr.boitakub.bogadex.boardgame.model

enum class Wished(
    private val intensity: Int,
    val priority: Int
) {

    DON_T_BUY_THIS(5, -2),
    NOT_WISHED(-1, -1),
    WISHED(-2, 0),
    THINKING_ABOUT_IT(4, 1),
    LIKE_TO_HAVE(3, 2),
    LOVE_TO_HAVE(2, 3),
    MUST_HAVE(1, 4);

    companion object {
        fun from(intensity: Int): Wished? = values().find { it.intensity == intensity }
        fun Wished.toInt(): Int {
            return intensity
        }

        fun isWished(wishlist: Int): Wished {
            if (wishlist == 1) {
                return WISHED
            }
            return NOT_WISHED
        }
    }
}
