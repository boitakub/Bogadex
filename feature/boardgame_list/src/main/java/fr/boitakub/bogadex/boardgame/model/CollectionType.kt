package fr.boitakub.bogadex.boardgame.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.boitakub.boardgame_list.R

enum class CollectionType(
    val key: String,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
) {
    ALL("all", R.string.display_all, R.drawable.ic_clear_all_24),
    MY_COLLECTION("collection", R.string.display_collection, R.drawable.ic_outline_all_inbox_24),
    WISHLIST("wishlist", R.string.display_wishlist, R.drawable.ic_outline_shopping_cart_24),
    SOLO("solo", R.string.display_solo, R.drawable.ic_user_solid),
    FILLER("filler", R.string.display_filler, R.drawable.ic_stopwatch_solid),
    ;

    companion object {
        val DEFAULT = ALL
        fun from(type: String?): CollectionType =
            CollectionType.values().find { it.key == type } ?: DEFAULT
    }
}
