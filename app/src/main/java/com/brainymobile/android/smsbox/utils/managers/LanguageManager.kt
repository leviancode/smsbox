package com.brainymobile.android.smsbox.utils.managers

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brainymobile.android.smsbox.R
import com.brainymobile.android.smsbox.ui.activities.dataStore
import com.brainymobile.android.smsbox.utils.PREF_KEY_DEFAULT_LANGUAGE
import com.brainymobile.android.smsbox.utils.extensions.indexOfIgnoreCase
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val lingver: Lingver
) {
    // private val lingver = Lingver.getInstance()

    fun getLanguages() = getLanguagesWithCodes().map {
        it.split("|").last()
    }

    fun getCodes() = getLanguagesWithCodes().map {
        it.split("|").first()
    }

    fun getCode(index: Int) =
        getLanguagesWithCodes()[index]?.split("|")?.first()

    fun getLanguagesWithCodes() =
        context.resources.getStringArray(R.array.languages).apply { sort() }

    fun getCurrentLocale(): String {
        val primaryLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }

        return primaryLocale.language
    }

    fun getCurrentCodeIndex(): Int {
        val currentLang = getCurrentLocale()
        return getCodes().indexOfIgnoreCase(currentLang)
    }

    fun updateResources(language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun updateAppLanguage(lang: String?) {
        lang ?: return
        lingver.setLocale(context, lang)
    }

    fun setAutoLocale() {
        lingver.setFollowSystemLocale(context)
    }

    fun getLanguageCode(lang: String): String? {
        return getLanguagesWithCodes()
            .find { it.contains(lang) }
            ?.split("|")
            ?.first()
    }

    suspend fun setDefaultLanguage(lang: String) {
        val code = getLanguageCode(lang) ?: return
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey(PREF_KEY_DEFAULT_LANGUAGE)] = code
        }
    }
}