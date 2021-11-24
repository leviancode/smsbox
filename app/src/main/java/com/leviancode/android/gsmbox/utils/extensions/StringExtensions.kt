package com.leviancode.android.gsmbox.utils.extensions

import com.leviancode.android.gsmbox.utils.HASHTAG_REGEX
import java.io.File
import java.util.regex.Pattern

fun String.toFile(dir: String, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}

fun String.toFile(dir: File, fileName: String): File {
    val file = File(dir, fileName)
    file.writeText(this)
    return file
}

inline fun String.findHashtag(found: (tag: String, startIndex: Int, endIndex: Int) -> Unit) {
    var str: String? = this
    while (str != null) {
        val matcher = Pattern.compile(HASHTAG_REGEX).matcher(str)
        str = if (matcher.find()) {
            val foundTag = matcher.group(1) ?: ""
            found(foundTag, matcher.start(), matcher.end())
            str.replaceFirst("#", "$")
        } else {
            null
        }
    }
}

fun String.hasPlaceholder() = contains('#')

