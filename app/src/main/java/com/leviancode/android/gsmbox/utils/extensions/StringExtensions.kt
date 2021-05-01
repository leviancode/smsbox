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

inline fun String.findHashtag(found: (tag: String, startIndex: Int, endIndex: Int) -> Unit){
    val matcher = Pattern.compile(HASHTAG_REGEX).matcher(this)
    while(matcher.find()){
        val find = matcher.group(1) ?: ""
        found(find, matcher.start(), matcher.end())
    }
}

inline fun String.replaceHashtag(found: (tag: String, startIndex: Int, endIndex: Int) -> String): String{
    val sb = StringBuffer()
    val matcher = Pattern.compile(HASHTAG_REGEX).matcher(this)
    while(matcher.find()){
        val find = matcher.group(1) ?: ""
        val replacement = found(find, matcher.start(), matcher.end())
        matcher.appendReplacement(sb, "$replacement ")
    }
    matcher.appendTail(sb)
    return sb.toString().trim()
}
