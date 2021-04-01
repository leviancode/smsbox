package com.leviancode.android.gsmbox.helpers

import android.text.Editable
import android.text.TextWatcher
import com.leviancode.android.gsmbox.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TextUniqueChecker(
    var comparisonList: List<String> = listOf(),
    var callback: (Boolean) -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            if (isTextUnique(s.toString())) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    private fun isTextUnique(text: String): Boolean {
        return comparisonList.find { it == text } == null
    }
}