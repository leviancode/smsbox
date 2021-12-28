package com.leviancode.android.gsmbox.ui.entities.placeholder

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder

fun Placeholder.toPlaceholderUI() = PlaceholderUI(
    id, position, name, value, timestamp
)

fun PlaceholderUI.toDomainPlaceholder() = Placeholder(
    id, position, getName(), getValue(), timestamp
)

fun List<Placeholder>.toUIPlaceholders() = map { it.toPlaceholderUI() }
fun List<PlaceholderUI>.toDomainPlaceholders() = map { it.toDomainPlaceholder() }