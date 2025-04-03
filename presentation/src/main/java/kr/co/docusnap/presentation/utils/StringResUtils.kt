package kr.co.docusnap.presentation.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

object StringResUtils {

    @ApplicationContext
    private lateinit var context: Context

    fun getString(id: Int): String = context.getString(id)
}