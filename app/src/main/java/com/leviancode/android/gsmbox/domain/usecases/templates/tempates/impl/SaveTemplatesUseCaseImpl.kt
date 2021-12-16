package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.SaveTemplatesUseCase

class SaveTemplatesUseCaseImpl(private val repository: TemplatesRepository) :
    SaveTemplatesUseCase {
    override suspend fun save(item: Template) {
        val templateWithFilteredRecipients = removeBlankRecipients(item)
        repository.save(templateWithFilteredRecipients)
    }

    override suspend fun save(items: List<Template>) {
        items.forEach{ save(it) }
    }

    private fun removeBlankRecipients(item: Template): Template {
        val recipients = item.recipientGroup.recipients.toMutableList()
        recipients.removeAll { it.phoneNumber.isBlank() }
        return item.apply {
            recipientGroup.recipients = recipients
        }
    }
}