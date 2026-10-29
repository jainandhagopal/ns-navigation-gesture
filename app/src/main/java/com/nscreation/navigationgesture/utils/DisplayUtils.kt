package com.nscreation.navigationgesture.utils

import android.content.Context

object DisplayUtils {
    fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
