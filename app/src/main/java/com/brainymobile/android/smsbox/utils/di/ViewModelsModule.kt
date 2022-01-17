package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.DeletePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.FetchPlaceholdersUseCaseImpl
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
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.edit.RecipientGroupEditViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.list.RecipientGroupListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.select.RecipientGroupMultiSelectListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.groups.select.RecipientGroupSelectListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.dialog.RecipientMultiSelectListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.dialog.RecipientSelectListViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.edit.RecipientEditViewModel
import com.brainymobile.android.smsbox.ui.screens.recipients.recipients.list.RecipientListViewModel
import com.brainymobile.android.smsbox.ui.screens.settings.SettingsViewModel
import com.brainymobile.android.smsbox.ui.screens.settings.languages.LanguagesViewModel
import com.brainymobile.android.smsbox.ui.screens.settings.placeholders.edit.PlaceholderEditViewModel
import com.brainymobile.android.smsbox.ui.screens.settings.placeholders.list.PlaceholdersViewModel
import com.brainymobile.android.smsbox.ui.screens.templates.groups.edit.TemplateGroupEditViewModel
import com.brainymobile.android.smsbox.ui.screens.templates.groups.list.TemplateGroupListViewModel
import com.brainymobile.android.smsbox.ui.screens.templates.templates.edit.TemplateEditViewModel
import com.brainymobile.android.smsbox.ui.screens.templates.templates.list.TemplateListViewModel
import org.koin.dsl.module

val viewModelsModule = module {

    // -----TEMPLATES-----

    factory {
        TemplateListViewModel(
            get<FetchTemplatesUseCaseImpl>(),
            get<DeleteTemplatesUseCaseImpl>(),
            get<UpdateTemplateUseCaseImpl>(),
            get()
        )
    }
    factory {
        TemplateGroupListViewModel(
            get<FetchTemplateGroupsUseCaseImpl>(),
            get<UpdateTemplateGroupsUseCaseImpl>(),
            get<DeleteTemplateGroupUseCaseImpl>()
        )
    }
    factory {
        TemplateGroupEditViewModel(
            get<SaveTemplateGroupUseCaseImpl>(),
            get<FetchTemplateGroupsUseCaseImpl>()
        )
    }
    factory {
        TemplateEditViewModel(
            get<FetchTemplatesUseCaseImpl>(),
            get<SaveTemplatesUseCaseImpl>(),
            get<UpdateTemplateUseCaseImpl>(),
            get<FetchRecipientsUseCaseImpl>(),
            get<FetchRecipientGroupsUseCaseImpl>(),
            get<FetchPlaceholdersUseCaseImpl>()
        )
    }

    // -----RECIPIENTS------

    factory {
        RecipientListViewModel(
            get<FetchRecipientsUseCaseImpl>(),
            get<UpdateRecipientsUseCaseImpl>(),
            get<DeleteRecipientsUseCaseImpl>()
        )
    }
    factory {
        RecipientEditViewModel(
            get<FetchRecipientsUseCaseImpl>(),
            get<SaveRecipientsUseCaseImpl>(),
            get<UpdateRecipientsUseCaseImpl>(),
            get()
        )
    }
    factory {
        RecipientGroupListViewModel(
            get<FetchRecipientGroupsUseCaseImpl>(),
            get<SaveRecipientGroupsUseCaseImpl>(),
            get<DeleteRecipientGroupsUseCaseImpl>(),
            get<DeleteRecipientsUseCaseImpl>()
        )
    }
    factory {
        RecipientGroupEditViewModel(
            get<FetchRecipientGroupsUseCaseImpl>(),
            get<SaveRecipientGroupsUseCaseImpl>()
        )
    }
    factory {
        RecipientGroupMultiSelectListViewModel(
            get<FetchRecipientGroupsUseCaseImpl>()
        )
    }
    factory {
        RecipientGroupSelectListViewModel(
            get<FetchRecipientGroupsUseCaseImpl>()
        )
    }
    factory {
        RecipientMultiSelectListViewModel(
            get<FetchRecipientsUseCaseImpl>()
        )
    }
    factory {
        RecipientSelectListViewModel(
            get<FetchRecipientsUseCaseImpl>(),
            get()
        )
    }

    // -----SETTINGS-----

    factory {
        SettingsViewModel(get())
    }

    factory {
        PlaceholdersViewModel(
            get<FetchPlaceholdersUseCaseImpl>(),
            get<DeletePlaceholdersUseCaseImpl>()
        )
    }

    factory {
        PlaceholderEditViewModel(
            get<FetchPlaceholdersUseCaseImpl>(),
            get<SavePlaceholdersUseCaseImpl>()
        )
    }

    factory {
        LanguagesViewModel(get())
    }
}