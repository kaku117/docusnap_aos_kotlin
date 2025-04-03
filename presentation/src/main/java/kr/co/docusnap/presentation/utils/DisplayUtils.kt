package kr.co.docusnap.presentation.utils

import android.content.Context

object DisplayUtils {
    private const val DEFAULT_COLUMN_COUNT = 120

    fun getColumnCount(context: Context): Int {
        return getDisplayWidthDp(context).toInt() / DEFAULT_COLUMN_COUNT
    }

    private fun getDisplayWidthDp(context: Context): Float {
        return context.resources.displayMetrics.run { widthPixels / density }
    }
}