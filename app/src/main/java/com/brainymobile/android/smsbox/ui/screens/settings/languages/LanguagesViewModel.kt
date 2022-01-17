package com.brainymobile.android.smsbox.ui.screens.settings.languages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainymobile.android.smsbox.utils.SingleLiveEvent
import com.brainymobile.android.smsbox.utils.managers.LanguageManager
import kotlinx.coroutines.launch

class LanguagesViewModel(
    private val languageManager: LanguageManager
) : ViewModel() {
    val restartApp = SingleLiveEvent<Unit>()

    fun getLanguages() = languageManager.getLanguages()

    fun getCurrentLangIndex() = languageManager.getCurrentCodeIndex()

    fun setDefaultLanguage(lang: String){
        viewModelScope.launch {
            languageManager.setDefaultLanguage(lang)
            restartApp.call()
        }
    }
}