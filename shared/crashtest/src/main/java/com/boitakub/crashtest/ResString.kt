package com.boitakub.crashtest

import android.content.Context
import androidx.annotation.StringRes

/**
 * Representing a String value, which could be a resource id or actual String.
 */
data class ResString(@StringRes val res: Int? = null, val txt: String? = null) {

    /**
     * Get the string value independent from the actual source.
     *
     * @return a String either retrieved from the strings.xml through the resource id or from the txt fields string.
     */
    fun get(context: Context): String {
        return txt ?: res?.run { context.getString(this) } ?: ""
    }
}

fun @receiver:StringRes Int.toResString(): ResString {
    return ResString(res = this)
}

fun String.toResString(): ResString {
    return ResString(txt = this)
}
