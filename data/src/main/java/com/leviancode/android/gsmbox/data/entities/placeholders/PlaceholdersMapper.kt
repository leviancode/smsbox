package com.leviancode.android.gsmbox.data.entities.placeholders

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder

fun PlaceholderData.toDomainPlaceholder() = Placeholder(
    id, position, name, value, timestamp
)

fun Placeholder.toPlaceholderData() = PlaceholderData(
    id, position, name, value, timestamp
)

fun List<PlaceholderData>.toDomainPlaceholders() = map { it.toDomainPlaceholder() }
fun List<Placeholder>.toPlaceholdersData() = map { it.toPlaceholderData() }