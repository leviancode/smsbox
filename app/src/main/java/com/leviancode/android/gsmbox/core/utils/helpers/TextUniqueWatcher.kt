package com.leviancode.android.gsmbox.core.utils.helpers

import android.text.Editable
import android.text.TextWatcher

class TextUniqueWatcher(
    var wordList: List<String> = listOf(),
    var callback: (Boolean) -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        callback(isTextUnique(s.toString()))
    }

    private fun isTextUnique(text: String): Boolean {
        return wordList.filter { it.isNotBlank() }
            .find { it.equals(text, true) } == null
    }
}