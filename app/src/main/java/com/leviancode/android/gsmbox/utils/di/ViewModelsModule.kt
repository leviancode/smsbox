package com.leviancode.android.gsmbox.utils.di

import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.DeletePlaceholdersUseCaseImpl
import com.leviancode.android.gsmbox.domain.usecases.placeholders.impl.FetchPlaceholdersUseCaseImpl
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
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.edit.RecipientGroupEditViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.list.RecipientGroupListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.select.RecipientGroupMultiSelectListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.groups.select.RecipientGroupSelectListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog.RecipientMultiSelectListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.dialog.RecipientSelectListViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.edit.RecipientEditViewModel
import com.leviancode.android.gsmbox.ui.screens.recipients.recipients.list.RecipientListViewModel
import com.leviancode.android.gsmbox.ui.screens.settings.SettingsViewModel
import com.leviancode.android.gsmbox.ui.screens.settings.languages.LanguagesViewModel
import com.leviancode.android.gsmbox.ui.screens.settings.placeholders.edit.PlaceholderEditViewModel
import com.leviancode.android.gsmbox.ui.screens.settings.placeholders.list.PlaceholdersViewModel
import com.leviancode.android.gsmbox.ui.screens.templates.groups.edit.TemplateGroupEditViewModel
import com.leviancode.android.gsmbox.ui.screens.templates.groups.list.TemplateGroupListViewModel
import com.leviancode.android.gsmbox.ui.screens.templates.templates.edit.TemplateEditViewModel
import com.leviancode.android.gsmbox.ui.screens.templates.templates.list.TemplateListViewModel
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
            get<FetchRecipientsUseCaseImpl>(),
            get<FetchRecipientGroupsUseCaseImpl>(),
            get<FetchPlaceholdersUseCaseImpl>()
        )
    }

    // -----RECIPIENTS------

    factory {
        RecipientListViewModel(
            get<FetchRecipientsUseCaseImpl>(),
            get<FetchRecipientGroupsUseCaseImpl>(),
            get<SaveRecipientsUseCaseImpl>(),
            get<DeleteRecipientsUseCaseImpl>()
        )
    }
    factory {
        RecipientEditViewModel(
            get<FetchRecipientsUseCaseImpl>(),
            get<SaveRecipientsUseCaseImpl>(),
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