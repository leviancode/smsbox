package com.leviancode.android.gsmbox.domain.usecases.placeholders.impl

import com.leviancode.android.gsmbox.domain.repositories.PlaceholdersRepository
import com.leviancode.android.gsmbox.domain.usecases.placeholders.ReplacePlaceholdersUseCase
import com.leviancode.android.gsmbox.utils.HASHTAG_REGEX
import java.util.regex.Pattern

class ReplacePlaceholdersUseCaseImpl(private val repository: PlaceholdersRepository) :
    ReplacePlaceholdersUseCase {

    override suspend fun replace(str: String): String {
        val matcher = Pattern.compile(HASHTAG_REGEX).matcher(str)
        if (matcher.find()) {
            val hashTag = matcher.group(1) ?: ""
            val value = repository.getValue(hashTag) ?: return hashTag
            if (hashTag != value) {
                val newStr = str.replaceFirst(hashTag, value)
                return replace(newStr)
            }
        }
        return str
    }
}