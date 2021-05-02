package com.leviancode.android.gsmbox.utils.managers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leviancode.android.gsmbox.R
import com.leviancode.android.gsmbox.ui.activities.dataStore
import com.leviancode.android.gsmbox.utils.PREF_KEY_DEFAULT_LANGUAGE
import com.leviancode.android.gsmbox.utils.extensions.indexOfIgnoreCase
import com.yariksoffice.lingver.Lingver
import java.util.*

object LanguageManager {

    fun getLanguages(context: Context) = getLanguagesWithCodes(context).map {
        it.split("|").last()
    }

    fun getCodes(context: Context) = getLanguagesWithCodes(context).map {
        it.split("|").first()
    }

    fun getCode(context: Context, index: Int) =
        getLanguagesWithCodes(context)[index]?.split("|")?.first()

    fun getLanguagesWithCodes(context: Context) =
        context.resources.getStringArray(R.array.languages)

    fun getCurrentLocale(context: Context): String {
        val primaryLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }

        return primaryLocale.language
    }

    fun getCurrentCodeIndex(context: Context): Int {
        val currentLang = getCurrentLocale(context)
        return getCodes(context).indexOfIgnoreCase(currentLang)
    }

    fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun updateAppLanguage(context: Context, lang: String?) {
        lang ?: return
        Lingver.getInstance().setLocale(context, lang)
/*
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics);*/
        //  createConfigurationContext(config)
    }

    fun getLanguageCode(context: Context, lang: String): String? {
        return getLanguagesWithCodes(context)
            .find { it.contains(lang) }
            ?.split("|")
            ?.first()
    }

    suspend fun setDefaultLanguage(context: Context, lang: String) {
        val code = getLanguageCode(context, lang) ?: return
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey(PREF_KEY_DEFAULT_LANGUAGE)] = code
        }
    }
}