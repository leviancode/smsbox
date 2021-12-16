package com.leviancode.android.gsmbox.utils.di

import com.leviancode.android.gsmbox.data.repositories.*
import com.leviancode.android.gsmbox.domain.repositories.*
import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.DeletePlaceholdersUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.FetchPlaceholdersUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.ReplacePlaceholdersUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.SavePlaceholdersUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl.DeleteRecipientGroupsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl.FetchRecipientGroupsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.groups.impl.SaveRecipientGroupsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl.DeleteRecipientsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl.FetchRecipientsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.recipients.recipients.impl.SaveRecipientsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl.DeleteTemplateGroupUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl.FetchTemplateGroupsUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.groups.impl.SaveTemplateGroupUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.DeleteTemplatesUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.SaveTemplatesUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.templates.tempates.impl.UpdateTemplateUseCaseImpl
import org.koin.dsl.module

val useCasesModule = module {

    // ------TEMPLATES-----

    factory { FetchTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }
    factory { SaveTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }
    factory { UpdateTemplateUseCaseImpl(get<TemplatesRepositoryImpl>()) }
    factory { DeleteTemplatesUseCaseImpl(get<TemplatesRepositoryImpl>()) }

    factory { FetchTemplateGroupsUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }
    factory { SaveTemplateGroupUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }
    factory { DeleteTemplateGroupUseCaseImpl(get<TemplateGroupsRepositoryImpl>()) }

    // -----RECIPIENTS-----

    factory { FetchRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }
    factory { SaveRecipientsUseCaseImpl(get<RecipientsRepositoryImpl>()) }
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