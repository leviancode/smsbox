package com.brainymobile.android.smsbox.ui.entities.placeholder

import com.brainymobile.android.smsbox.domain.entities.placeholder.Placeholder

fun Placeholder.toPlaceholderUI() = PlaceholderUI(
    id, position, name, value, timestamp
)

fun PlaceholderUI.toDomainPlaceholder() = Placeholder(
    id, position, getName(), getValue(), timestamp
)

fun List<Placeholder>.toUIPlaceholders() = map { it.toPlaceholderUI() }
fun List<PlaceholderUI>.toDomainPlaceholders() = map { it.toDomainPlaceholder() }