package com.brainymobile.android.smsbox.domain.usecases.placeholders.impl

import com.brainymobile.android.smsbox.domain.repositories.PlaceholdersRepository
import com.brainymobile.android.smsbox.domain.usecases.placeholders.ReplacePlaceholdersUseCase
import com.brainymobile.android.smsbox.utils.HASHTAG_REGEX
import java.util.regex.Pattern

class ReplacePlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    ReplacePlaceholdersUseCase {

    override suspend fun replace(str: String): String {
        val matcher = Pattern.compile(HASHTAG_REGEX).matcher(str)
        if (matcher.find()) {
            val hashTag = matcher.group(1) ?: ""
            val name = hashTag.removePrefix("#")
            val value = repository.getValue(name) ?: return str
            val newStr = str.replaceFirst(hashTag, value)
            return replace(newStr)
        }
        return str
    }
}