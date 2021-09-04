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
