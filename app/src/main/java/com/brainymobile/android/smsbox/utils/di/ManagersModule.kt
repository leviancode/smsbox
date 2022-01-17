package com.brainymobile.android.smsbox.utils.di

import com.brainymobile.android.smsbox.domain.usecases.placeholders.impl.ReplacePlaceholdersUseCaseImpl
import com.brainymobile.android.smsbox.domain.usecases.templates.tempates.impl.FetchTemplatesUseCaseImpl
import com.brainymobile.android.smsbox.utils.managers.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val managersModule = module {
    factory {
        SmsManager(
            get<FetchTemplatesUseCaseImpl>(),
            get<ReplacePlaceholdersUseCaseImpl>()
        )
    }

    factory {
        ContactsManager(androidApplication().applicationContext)
    }

    factory {
        LanguageManager(androidApplication().applicationContext)
    }

    factory {
        FilesManager(androidApplication().applicationContext)
    }

    factory {
        BackupManager(androidApplication().applicationContext, get(), get())
    }
}