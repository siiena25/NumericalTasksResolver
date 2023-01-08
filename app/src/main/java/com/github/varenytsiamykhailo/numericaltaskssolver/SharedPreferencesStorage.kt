package com.github.varenytsiamykhailo.numericaltaskssolver

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.component.KoinComponent


class SharedPreferencesStorage constructor(
    context: Context,
) : KoinComponent {

    companion object {
    }

    private val storage: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}