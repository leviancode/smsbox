package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.data.repositories.*
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.DeletePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.FetchPlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.ReplacePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.SavePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.DeleteRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.FetchRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.groups.impl.SaveRecipientGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.DeleteRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.FetchRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.SaveRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.recipients.recipients.impl.UpdateRecipientsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.DeleteTemplateGroupUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.FetchTemplateGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.SaveTemplateGroupUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.groups.impl.UpdateTemplateGroupsUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.DeleteTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.SaveTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.UpdateTemplateUseCaseImpl
import org.koin.dsl.module

val useCasesModule = module {

    // ------TEMPLATES-----

    factory { FetchTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }
    factory { SaveTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }
    factory {
        UpdateTemplateUseCaseImpl(
            get<TemplatesRepositoryImpl>(),
            get<RecipientsRepositoryImpl>(),
            get<RecipientGroupsRepositoryImpl>()
        )
    }
    factory { DeleteTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }

    factory { FetchTemplateGroupsUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }
    factory { SaveTemplateGroupUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }
    factory { UpdateTemplateGroupsUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }
    factory { DeleteTemplateGroupUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }

    // -----RECIPIENTS-----

    factory { FetchRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }
    factory { SaveRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }
    factory { UpdateRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }
    factory { DeleteRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }

    factory { FetchRecipientGroupsUseCaseImpl(get<RecipientGroupsRepositoryImpl>()) }
    factory { SaveRecipientGroupsUseCaseImpl(get<RecipientGroupsRepositoryImpl>()) }
    factory { DeleteRecipientGroupsUseCaseImpl(get<RecipientGroupsRepositoryImpl>()) }

    // -----PLACEHOLDERS-----

    factory { FetchPlaceholdersUseCaseImpl(get<PlaceholdersRepositoryImpl>()) }
    factory { SavePlaceholdersUseCaseImpl(get<PlaceholdersRepositoryImpl>()) }
    factory { ReplacePlaceholdersUseCaseImpl(get<PlaceholdersRepositoryImpl>()) }
    factory { DeletePlaceholdersUseCaseImpl(get<PlaceholdersRepositoryImpl>()) }
}