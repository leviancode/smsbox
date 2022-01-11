package com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl

import com.leviancode.android.gsmbox.domain.entities.template.Template
import com.leviancode.android.gsmbox.domain.repositories.RecipientGroupsRepository
import com.leviancode.android.gsmbox.domain.repositories.RecipientsRepository
import com.leviancode.android.gsmbox.domain.repositories.TemplatesRepository
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import com.leviancode.android.gsmbox.utils.logE

class UpdateTemplateUseCaseImpl(
    private val templatesRepository: TemplatesRepository,
    private val recipientsRepository: RecipientsRepository,
    private val recipientGroupsRepository: RecipientGroupsRepository,
) : UpdateTemplateUseCase {
    override suspend fun updateFavorite(templateId: Int, favorite: Boolean) {
        templatesRepository.updateFavorite(templateId, favorite)
    }

    override suspend fun update(items: List<Template>) {
        templatesRepository.update(items)
    }

    override suspend fun update(item: Template) {
        item.recipientGroup.recipients.forEach { recipient ->
            val foundByNumber = recipientsRepository.getRecipientByPhoneNumber(recipient.phoneNumber)
            if (foundByNumber == null && !recipient.name.isNullOrBlank()){
                recipient.name = null
            }
        }
        val recipientGroupId = recipientGroupsRepository.update(item.recipientGroup)
        templatesRepository.update(item.apply {
            recipientGroup.id = recipientGroupId
        })
    }
}