package com.serhat.autosub

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {
    private const val PREFS = "app_settings"
    private const val KEY_LANG = "app_lang"

    fun applyLocale(context: Context, langTag: String) {
        // Save selected language to SharedPreferences
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANG, langTag)
            .apply()

        // Apply locale globally (works from Android 5+ to Android 14+)
        val locales = LocaleListCompat.forLanguageTags(langTag)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun currentLangTag(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LANG, null)
    }
}
