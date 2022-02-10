package com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl

import com.brainymobile.android.smsbox.domain.entities.template.Template
import com.brainymobile.android.smsbox.domain.repositories.RecipientGroupsRepository
import com.brainymobile.android.smsbox.domain.repositories.RecipientsRepository
import com.brainymobile.android.smsbox.domain.repositories.TemplatesRepository
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.UpdateTemplateUseCase
import javax.inject.Inject

class UpdateTemplateUseCaseImpl @Inject constructor(
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