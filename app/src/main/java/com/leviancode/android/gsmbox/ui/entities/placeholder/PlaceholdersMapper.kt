package com.leviancode.android.gsmbox.ui.entities.placeholder

import com.leviancode.android.gsmbox.domain.entities.placeholder.Placeholder

fun Placeholder.toPlaceholderUI() = PlaceholderUI(
    id, name, value
)

fun PlaceholderUI.toDomainPlaceholder() = Placeholder(
    id, getName(), getValue()
)

fun List<Placeholder>.toUIPlaceholders() = map { it.toPlaceholderUI() }
fun List<PlaceholderUI>.toDomainPlaceholders() = map { it.toDomainPlaceholder() }